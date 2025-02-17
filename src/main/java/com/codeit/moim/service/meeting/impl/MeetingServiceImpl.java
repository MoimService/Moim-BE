package com.codeit.moim.service.meeting.impl;

import com.codeit.moim.common.exception.auth.UserContactNotFoundException;
import com.codeit.moim.common.exception.auth.UserNotFoundException;
import com.codeit.moim.common.exception.meeting.MeetingNotFoundException;
import com.codeit.moim.domain.*;
import com.codeit.moim.domain.enums.MemberStatus;
import com.codeit.moim.domain.enums.SortField;
import com.codeit.moim.repository.*;
import com.codeit.moim.service.meeting.MeetingService;
import com.codeit.moim.service.member.impl.MemberServiceImpl;
import com.codeit.moim.service.storage.StorageService;
import com.codeit.moim.web.dto.request.meeting.CreateMeetingRequest;
import com.codeit.moim.web.dto.request.meeting.SearchMeetingRequest;
import com.codeit.moim.web.dto.response.meeting.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static com.codeit.moim.domain.enums.MemberStatus.APPROVED;

@Service
@RequiredArgsConstructor
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
                .orElseThrow(()-> new UserNotFoundException(String.valueOf(userId)));
        //if user phone does not exist, throw exception
        if(user.getContact().getPhone() == null ){
            throw new UserContactNotFoundException(String.valueOf(userId));
        }

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
    public List<ReadTopMeetingResponse> findTopMeetingList(int userId, String categoryTitle) {
        List<Meeting> meetingList = meetingRepository.findPublicMeetingsByCategory(categoryTitle, true);

        User user = userRepository.findById(userId)
                .orElseThrow(()-> new UserNotFoundException(String.valueOf(userId)));

        List<ReadTopMeetingResponse> meetingResponseList = new ArrayList<>();

        if( !meetingList.isEmpty() ){
            List<Meeting> topMeetingList = meetingList.stream()
                    .sorted(Comparator.comparing(Meeting::getLikesCount).reversed())
                    .limit(4)
                    .collect(Collectors.toList());

            for( Meeting meeting : topMeetingList ){
                Boolean isLike = likesRepository.existsByUserAndMeeting(user, meeting);

                ReadTopMeetingResponse response = ReadTopMeetingResponse.fromEntity(meeting, isLike);
                meetingResponseList.add(response);
            }
        }
        return meetingResponseList;
    }

    @Override
    public List<SearchMeetingResponse> findMeetingList(int userId, String categoryTitle, SearchMeetingRequest request) {
       List<Meeting> meetingList = meetingRepository.findPublicMeetingsByCategory(categoryTitle, true);

        List<String> skillList = Arrays.asList(request.skillArray());
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

        return buildSearchResponse(sortedMeetingList);
    }

    @Override
    public ReadMeetingDetailResponse findMeetingDetail(int meetingId, int userId) {
        Meeting meeting = meetingRepository.findById(meetingId)
                .orElseThrow(()-> new MeetingNotFoundException(String.valueOf(meetingId)));

        User user = userRepository.findById(userId)
                .orElseThrow(()-> new UserNotFoundException(String.valueOf(userId)));

        boolean isLike = likesRepository.existsByUserAndMeeting(user, meeting);
        boolean isMember = memberRepository.existsByUserAndMeetingAndStatus(user, meeting, MemberStatus.APPROVED);
        return ReadMeetingDetailResponse.fromEntity(meeting, isLike, isMember);
    }

    @Override
    public ReadMeetingManagerResponse findMeetingManagerDetail(int meetingId, int userId) {
        Meeting meeting = meetingRepository.findMeetingWithManagerAndSkill(meetingId)
                .orElseThrow(()-> new MeetingNotFoundException(String.valueOf(meetingId)));

        User user = meeting.getUser();
        if(user.getContact().getPhone() == null){
            throw new UserContactNotFoundException(String.valueOf(user.getUserId()));
        }
        List<UserSkill> userSkillList = user.getUserSkillList();
        String[] skillArray = userSkillList.stream()
                .map(s-> s.getSkill().getSkillTitle())
                .toArray(String[]::new);

        return ReadMeetingManagerResponse.fromEntity(user, skillArray);
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

    public List<SearchMeetingResponse> buildSearchResponse(List<Meeting> finalMeetingList){
        List<SearchMeetingResponse> meetingResponseList = new ArrayList<>();
        for (Meeting meeting : finalMeetingList) {
            User user = meetingRepository.findUserByMeeting(meeting);
            SearchMeetingResponse response = SearchMeetingResponse.fromEntity(meeting, user);
            meetingResponseList.add(response);
        }
        return meetingResponseList;
    }

}