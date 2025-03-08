package com.codeit.moim.service.meeting.impl;

import com.codeit.moim.common.exception.auth.UserNotFoundException;
import com.codeit.moim.common.exception.meeting.MeetingAccessDeniedException;
import com.codeit.moim.common.exception.meeting.MeetingNotFoundException;
import com.codeit.moim.domain.*;
import com.codeit.moim.domain.enums.MemberStatus;
import com.codeit.moim.domain.enums.SortField;
import com.codeit.moim.repository.*;
import com.codeit.moim.service.meeting.MeetingService;
import com.codeit.moim.service.storage.StorageService;
import com.codeit.moim.web.dto.request.meeting.CreateMeetingRequest;
import com.codeit.moim.web.dto.request.meeting.SearchMeetingRequest;
import com.codeit.moim.web.dto.response.meeting.*;
import com.codeit.moim.web.dto.response.slice.CustomSlice;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static com.codeit.moim.domain.enums.MemberStatus.APPROVED;

@Service
@RequiredArgsConstructor
@Slf4j
public class MeetingServiceImpl implements MeetingService {
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final MeetingRepository meetingRepository;
    private final SkillRepository skillRepository;
    private final MeetingSkillRepository meetingSkillRepository;
    private final MemberRepository memberRepository;
    private final LikesRepository likesRepository;
    private final StorageService storageService;
    @Override
    public CreateMeetingResponse saveMeeting(int userId, CreateMeetingRequest request) {
        // ⚡️image
        String uploadUrl = "";
        if(Objects.nonNull(request.imageEncodedBase64()) && !request.imageEncodedBase64().isEmpty()){
            uploadUrl = storageService.uploadFile(request.imageEncodedBase64(), request.imageName());
        }
        //category
        Category category = categoryRepository.findByCategoryTitle(request.categoryTitle());
        //user
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new UserNotFoundException("UserId: "+ userId));

        Meeting meeting = request.toEntity(uploadUrl, user, category);
        meeting.increaseMemberCount();
        Meeting savedMeeting = meetingRepository.save(meeting);

        //create member
        Member member = Member.toEntity(user, meeting, APPROVED, "모임 주최자 입니다");
        memberRepository.save(member);

        //create meeting skills
        List<String> skillList = Arrays.asList(request.skillArray());

        if( !skillList.isEmpty() ){
            for( String skillTitle: skillList ){
                Skill skill = skillRepository.findBySkillTitle(skillTitle);
                MeetingSkill meetingSkill = MeetingSkill.builder()
                        .meeting(savedMeeting)
                        .skill(skill)
                        .build();
                meetingSkillRepository.save(meetingSkill);
            }
        }

        return CreateMeetingResponse.fromEntity(savedMeeting);
    }

    @Override
    public List<ReadTopMeetingResponse> findTopMeetingList(String categoryTitle) {
        List<Meeting> meetingList = meetingRepository.findPublicMeetingsByCategory(categoryTitle, true);

        List<ReadTopMeetingResponse> meetingResponseList = new ArrayList<>();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = (authentication instanceof AnonymousAuthenticationToken) ? "no user" : authentication.getName();


        if( meetingList.isEmpty() ) return meetingResponseList;


        List<Meeting> topMeetingList = meetingList.stream()
                   .sorted(Comparator.comparing(Meeting::getLikesCount).reversed())
                    .limit(4)
                    .collect(Collectors.toList());

        return topMeetingList.stream()
                .map(meeting -> {
                    boolean isLike = likesRepository.existsByUserEmailAndMeeting(email, meeting);
                    return ReadTopMeetingResponse.fromEntity(meeting, isLike);
                }
        ).toList();
    }

    @Override
    public Slice<SearchMeetingResponse> findMeetingList(String categoryTitle, SearchMeetingRequest request) {
        int pageSize = request.size();
        Integer lastMeetingId = request.lastMeetingId();
        Pageable pageable = PageRequest.of(0, pageSize);

        List<Meeting> meetingList = meetingRepository.findPublicMeetingsByCategory(categoryTitle, true);

        List<String> skillList = request.skillArray();
        if( request.keyword() != null && !skillList.isEmpty() ){
            meetingList = searchKeyword(request.keyword(), meetingList);
            meetingList = searchSkill(skillList, meetingList);
        }
        else if( request.keyword() != null && skillList.isEmpty()){
            meetingList = searchKeyword(request.keyword(), meetingList);
        }else if(request.keyword() == null && !skillList.isEmpty() ) {
            meetingList = searchSkill(skillList, meetingList);
        }

        //sort
        List<Meeting> sortedMeetingList = sortMeetings(meetingList, request.sortField());

        //infinite scroll
        List<Meeting> slicedList;
        if(Objects.isNull(lastMeetingId) || lastMeetingId <=0 ) {
            slicedList = sortedMeetingList.stream()
                    .limit(pageSize)
                    .collect(Collectors.toList());
        }else{
            slicedList = sortedMeetingList.stream()
                    .dropWhile(meeting -> meeting.getMeetingId() != lastMeetingId)
                    .skip(1)
                    .limit(pageSize)
                    .collect(Collectors.toList());
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = (authentication instanceof AnonymousAuthenticationToken) ? "no user" : authentication.getName();
        List<SearchMeetingResponse> meetingResponses = slicedList.stream()
                .map(meeting -> {
                    List<String> meetingSkillList = meetingSkillRepository.findSkillByMeeting(meeting)
                            .stream()
                            .map(meetingSkill -> meetingSkill.getSkill().getSkillTitle())
                            .collect(Collectors.toList());

                    String[] meetingSkillArray = meetingSkillList.stream().toArray(String[]::new);
                    boolean isLike = likesRepository.existsByUserEmailAndMeeting(email, meeting);
                    return SearchMeetingResponse.fromEntity(meeting, meetingSkillArray, meeting.getUser(), isLike);

                })
                .collect(Collectors.toList());



        Integer nextCursor = (meetingResponses.size() == pageSize)
                ? meetingResponses.get(meetingResponses.size() -1).meetingId()
                : null;
        return new CustomSlice<>(meetingResponses, pageable, nextCursor != null, nextCursor);
    }

    @Override
    public ReadMeetingDetailResponse findMeetingDetail(int meetingId) {
        Meeting meeting = meetingRepository.findByIdWithUser(meetingId);
        if( meeting == null ) throw new MeetingNotFoundException("MeetingId: " + meetingId);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = (authentication instanceof AnonymousAuthenticationToken) ? "no user" : authentication.getName();



        boolean isLike;
        boolean isMember;
        String memberStatus;

        if(!email.equals("no user")){
            User user = userRepository.findByEmail(email)
                    .orElseThrow(()-> new UserNotFoundException("user email: " + email));

            if(!meeting.isPublic() && !meeting.getUser().getEmail().equals(email)) throw new MeetingAccessDeniedException("Only meeting manager can access isPublic = false meeting. UserId: " + meeting.getUser().getEmail());

            isLike = likesRepository.existsByUserEmailAndMeeting(email, meeting);
            isMember = memberRepository.existsByUserEmailAndMeetingAndStatus(email, meeting, MemberStatus.APPROVED);
            if(memberRepository.existsByUserAndMeeting(user, meeting)){
                memberStatus = memberRepository.findByUserAndMeeting(user, meeting).getStatus().toString();
            }else{
                memberStatus = "new user";
            }

        }else{
            isLike = false;
            isMember = false;
            memberStatus = "false";
        }

        List<String> meetingSkillList = meetingSkillRepository.findSkillByMeeting(meeting)
                .stream()
                .map(meetingSkill -> meetingSkill.getSkill().getSkillTitle())
                .collect(Collectors.toList());

        String[] meetingSkillArray = meetingSkillList.stream().toArray(String[]::new);
        return ReadMeetingDetailResponse.fromEntity(meeting, isLike, isMember, memberStatus, meetingSkillArray);
    }

    @Override
    public ReadMeetingManagerResponse findMeetingManagerDetail(int meetingId) {
        Meeting meeting = meetingRepository.findMeetingWithManagerAndSkill(meetingId)
                .orElseThrow(()-> new MeetingNotFoundException("MeetingId: "+ meetingId));

        User user = meeting.getUser();
        String phone = (user.getContact() != null && user.getContact().getPhone() != null )
                ? user.getContact().getPhone()
                : null;

        List<UserSkill> userSkillList = user.getUserSkillList();
        String[] skillArray = userSkillList.stream()
                .map(s-> s.getSkill().getSkillTitle())
                .toArray(String[]::new);

        return ReadMeetingManagerResponse.fromEntity(user, phone, skillArray);
    }


    private List<Meeting> searchSkill(List<String> skillList, List<Meeting> meetingList) {
        return meetingList.stream()
                .filter(m -> {
                    List<String> meetingSkillList = m.getMeetingSkillList().stream()
                            .map( ms -> ms.getSkill().getSkillTitle())
                            .collect(Collectors.toList());
                    return meetingSkillList.containsAll(skillList);
                })
                .collect(Collectors.toList());
    }

    private List<Meeting> searchKeyword(String keyword ,List<Meeting> meetingList) {
        return meetingList.stream()
                .filter(meeting -> meeting.getMeetingTitle().contains(keyword))
                .collect(Collectors.toList());
    }


    public List<Meeting> sortMeetings(List<Meeting> meetingList, String sortField){

        SortField sortEnum = SortField.valueOf(sortField);

        switch(sortEnum){
            case NEW:
                meetingList.sort(Comparator.comparing(Meeting::getStartDate).reversed());
                break;
            case OLD:
                meetingList.sort(Comparator.comparing(Meeting::getStartDate));
                break;
            case LIKES:
                meetingList.sort(Comparator.comparing(Meeting::getLikesCount).reversed());
                break;
            }
        return meetingList;
    }


}