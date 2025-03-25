package com.codeit.moim.service.mymeeting.impl;

import com.codeit.moim.common.exception.auth.MeetingManagerException;
import com.codeit.moim.common.exception.auth.UserNotFoundException;
import com.codeit.moim.common.exception.meeting.MaxMemberUpdateException;
import com.codeit.moim.common.exception.meeting.MeetingAccessDeniedException;
import com.codeit.moim.common.exception.meeting.MeetingNotFoundException;
import com.codeit.moim.common.exception.member.MemberNotFoundException;
import com.codeit.moim.common.exception.payload.ErrorStatus;
import com.codeit.moim.domain.*;
import com.codeit.moim.domain.enums.MemberStatus;
import com.codeit.moim.repository.*;
import com.codeit.moim.service.mymeeting.MyMeetingService;
import com.codeit.moim.service.storage.StorageService;
import com.codeit.moim.web.dto.request.likes.ReadLikeMeetingRequest;
import com.codeit.moim.web.dto.request.mymeeting.*;
import com.codeit.moim.web.dto.response.meeting.UpdateMeetingResponse;
import com.codeit.moim.web.dto.response.member.DeleteMemberResponse;
import com.codeit.moim.web.dto.response.mymeeting.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import com.codeit.moim.web.dto.response.slice.CustomSlice;
import org.springframework.transaction.annotation.Transactional;


import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MyMeetingServiceImpl implements MyMeetingService {
    private final UserRepository userRepository;
    private final MeetingRepository meetingRepository;
    private final MemberRepository memberRepository;
    private final UserSkillRepository userSkillRepository;
    private final CategoryRepository categoryRepository;
    private final StorageService storageService;
    private final MeetingSkillRepository meetingSkillRepository;
    private final SkillRepository skillRepository;



    @Override
    public UpdateMemberStatusResponse updateMemberStatus(int userId, UpdateMemberStatusRequest request) {
        Meeting meeting = getMeeting(request.meetingId());
        //check if user is manager
        if(!validateMeetingManager(userId, meeting)) throw new MeetingAccessDeniedException("Only meeting manager can update member status. UserId: "+ userId);

        //get member
        Member member = getMemberWithUserAndMeeting(request.userId(), meeting);
        //change member status, save
        MemberStatus status = MemberStatus.valueOf(request.setMemberStatus());
        if(!member.getStatus().equals(MemberStatus.PENDING)) throw new MeetingAccessDeniedException("Only member status PENDING can be updated. Current member status: "+ status);

        if(status.equals(MemberStatus.APPROVED)){
            meeting.increaseMemberCount();
            meetingRepository.save(meeting);
        }
        return saveUpdatedMember(member, status);
    }

    @Override
    public UpdateMemberToExpelResponse expelMember(int userId, UpdateMemberStatusRequest request) {
        Meeting meeting = getMeeting(request.meetingId());
        //check if user is manager
        if(!validateMeetingManager(userId, meeting)) throw new MeetingAccessDeniedException("Only meeting manager can expel member. UserId: "+ userId);
        Member member = getMemberWithUserAndMeeting(request.userId(), meeting);
        if( !member.getStatus().equals(MemberStatus.APPROVED)) throw new MeetingAccessDeniedException("Only member status APPROVED can be expelled. Current member status: "+ member.getStatus());

        meeting.decreaseMemberCount();
        meetingRepository.save(meeting);
        return saveUpdatedMember(member, MemberStatus.EXPEL);
    }

    @Override
    public Slice<ReadAllMeetingResponse> findAllMyMeeting(int userId, ReadAllMeetingRequest request) {
        int pageSize = request.size();
        Pageable pageable = PageRequest.of(0, pageSize);

        User user = getUser(userId);
        boolean isPublic = true;

        Slice<Meeting> meetings;
        if(Objects.isNull(request.lastMeetingId()) || request.lastMeetingId() <=0 ){
            meetings = memberRepository.findByUser_userOrderByMeetingIdDesc(user, isPublic, pageable);
        }else{
            meetings = memberRepository.findByUser_userLessThanOrderByMeetingIdDesc(user, isPublic, request.lastMeetingId(), pageable);
        }

        List<ReadAllMeetingResponse> meetingResponses = meetings.stream()
                .map(meeting -> {
                    String status = memberRepository.findByUserAndMeeting(user, meeting).getStatus().toString();
                    boolean isMeetingManager = meeting.getUser().getUserId() == userId;
                    List<ReadAllMeetingMemberResponse> memberResponseList = memberRepository.findByMeeting(meeting)
                            .stream()
                            .filter(member -> member.getStatus().equals(MemberStatus.APPROVED))
                            .map(member -> ReadAllMeetingMemberResponse.fromEntity(member.getUser()))
                            .toList();
                    return ReadAllMeetingResponse.fromEntity(meeting, status, isMeetingManager, memberResponseList);
                }).toList();

        Integer nextCursor = meetings.hasNext()
                ? meetings.getContent().get(meetings.getContent().size() -1).getMeetingId()
                : null;

        return new CustomSlice<>(meetingResponses, pageable, meetings.hasNext(), nextCursor);
    }

    @Override
    public Slice<ReadManageMeetingResponse> findManageMeeting(int userId, ReadManageMeetingRequest request) {
        int pageSize = request.size();
        Pageable pageable = PageRequest.of(0, pageSize);
        User user = getUser(userId);

        Slice<Meeting> meetings;
        if(Objects.isNull(request.lastMeetingId()) || request.lastMeetingId() <=0 ){
            meetings = meetingRepository.findByUserOrderByMeetingIdDesc(user, pageable);
        }else{
            meetings = meetingRepository.findByUserAndMeetingIdLessThanOrderByMeetingIdDesc(user, request.lastMeetingId(), pageable);
        }

        List<ReadManageMeetingResponse> meetingResponses = meetings.stream()
                .map(meeting-> {
                    List<ReadManageMeetingMemberResponse> memberResponseList = memberRepository.findByMeeting(meeting)
                            .stream()
                            .map(member -> ReadManageMeetingMemberResponse.fromEntity(member.getUser(), member.getStatus().toString()))
                            .toList();
                    return ReadManageMeetingResponse.fromEntity(meeting, memberResponseList);
                })
                .toList();

        Integer nextCursor = meetings.hasNext()
                ? meetings.getContent().get(meetings.getContent().size() -1).getMeetingId()
                : null;

        return new CustomSlice<>(meetingResponses, pageable, meetings.hasNext(), nextCursor);
    }

    @Override
    public UpdateMeetingIsPublicResponse updateIsPublic(int userId, int meetingId) {
        Meeting meeting = getMeeting(meetingId);
        if(!validateMeetingManager(userId, meeting)) throw new MeetingAccessDeniedException("Only meeting manager can update meeting isPublic. UserId: "+ userId);
        if(meeting.isPublic()){
            meeting.updateIsPublicToFalse();
            Meeting savedMeeting = meetingRepository.save(meeting);

            return UpdateMeetingIsPublicResponse.fromEntity(savedMeeting);
        }else{
            meeting.updateIsPublicToTrue();
            Meeting savedMeeting = meetingRepository.save(meeting);

            return UpdateMeetingIsPublicResponse.fromEntity(savedMeeting);
        }

    }

    @Override
    public DeleteMemberResponse cancelMemberApply(int userId, int meetingId) {
        Meeting meeting = getMeeting(meetingId);
        User user = getUser(userId);
        Member member = memberRepository.findByUserAndMeeting(user, meeting);
        if(member != null && member.getStatus().equals(MemberStatus.PENDING)){
            memberRepository.delete(member);
            return new DeleteMemberResponse(userId);
        }else{
            throw new MeetingAccessDeniedException("Meeting access denied. UserId: " + userId);
        }
    }

    @Override
    public DeleteMemberResponse quitMeeting(int userId, int meetingId) {
        Meeting meeting = getMeeting(meetingId);
        User user = getUser(userId);
        if(validateMeetingManager(userId, meeting)) throw new MeetingManagerException("Meeting manager cannot quit meeting. UserId: " + userId);
        Member member = memberRepository.findByUserAndMeeting(user, meeting);
        if(member == null) throw new MemberNotFoundException("Member not found by user and meeting. UserId: "+ userId + " MeetingId: "+ meetingId);
        if(member.getStatus().equals(MemberStatus.APPROVED)){
            memberRepository.delete(member);
            meeting.decreaseMemberCount();
            meetingRepository.save(meeting);
            return new DeleteMemberResponse(userId);
        }else{
            throw new MeetingAccessDeniedException("Member status: " + member.getStatus());
        }
    }

    @Override
    public Slice<ReadLikeMeetingResponse> findLikeMeetings(int userId, ReadLikeMeetingRequest request) {
        int pageSize = request.size();
        Pageable pageable = PageRequest.of(0, pageSize);

        User user = getUser(userId);

        Slice<Meeting> meetings;
        if(Objects.isNull(request.lastMeetingId()) || request.lastMeetingId() <=0 ){
            meetings = meetingRepository.findLikedMeetings(user, pageable);
       }else{
            meetings = meetingRepository.findLikeMeetingsLessThan(user, request.lastMeetingId(), pageable);
        }

        List<ReadLikeMeetingResponse> meetingResponses = meetings
                .map(ReadLikeMeetingResponse::fromEntity)
                .getContent();

        Integer nextCursor = meetings.hasNext()
                ? meetings.getContent().get(meetings.getContent().size() -1).getMeetingId()
                : null;
        return new CustomSlice<>(meetingResponses, pageable, meetings.hasNext(), nextCursor);
    }

    @Override
    public ReadMemberProfileResponse findMemberProfile(int userId, ReadMemberProfileRequest request) {
        User requestedUser = getUser(request.userId());

        List<UserSkill> skillList = userSkillRepository.findByUserWithSkill(requestedUser);
        String[] skillArray = skillList.stream()
                .map(userSkill -> userSkill.getSkill().getSkillTitle())
                .toArray(String[]::new);

        Meeting meeting = getMeeting(request.meetingId());

        Contact requestedUserContact = requestedUser.getContact();

        ReadMemberContactResponse contactResponse = (requestedUserContact != null)
                ?  ReadMemberContactResponse.fromEntity(requestedUserContact)
                : null;

        Member requestedUserMember =  memberRepository.findByUserAndMeeting(requestedUser, meeting);
        if(requestedUserMember == null) throw new MemberNotFoundException("UserId: "+ request.userId() + " MeetingId: " + request.meetingId());
        ReadMemberMessageResponse memberResponse = ReadMemberMessageResponse.fromEntity(requestedUserMember);


        return ReadMemberProfileResponse.fromEntity(requestedUser, skillArray, contactResponse, memberResponse);
    }

    @Override
    @Transactional
    public UpdateMeetingResponse updateMeetingInfo(int userId, int meetingId, UpdateMeetingRequest request) {
        Meeting meeting = meetingRepository.findByIdWithUser(meetingId);
        if( meeting == null ) throw new MeetingNotFoundException("MeetingId: " + meetingId);
        if(!validateMeetingManager(userId, meeting)) throw new MeetingAccessDeniedException("Only meeting manager can update meeting detail. UserId: "+ userId);

        Category category = null;
        if(Objects.nonNull(request.categoryTitle())  && !request.categoryTitle().isEmpty()) {
            category = categoryRepository.findByCategoryTitle(request.categoryTitle());
        }

        String uploadUrl = "";
        if(request.imageName() == null || request.imageName().isEmpty()) uploadUrl = meeting.getThumbnail();
        if(Objects.nonNull(request.imageEncodedBase64()) && !request.imageEncodedBase64().isEmpty()){
            uploadUrl = storageService.uploadFile(request.imageEncodedBase64(), request.imageName());
        }

        if(!Objects.nonNull(request.maxMember()) && request.maxMember() < meeting.getMaxMember()) throw new MaxMemberUpdateException("Max member should be bigger than current member count");

        meeting.updateMeeting(request, uploadUrl, category);
        meetingRepository.save(meeting);

        meetingSkillRepository.deleteAllByMeeting(meeting);

        List<Skill> skillList = Arrays.stream(request.skillArray())
                .map(skillRepository::findBySkillTitle)
                .collect(Collectors.toList());

        List<MeetingSkill> meetingSkillList = skillList.stream()
                .map(skill -> request.toEntity(meeting, skill))
                .collect(Collectors.toList());

        meetingSkillRepository.saveAll(meetingSkillList);

        return new UpdateMeetingResponse(meetingId);
    }

    @Override
    public Slice<ReadAllMeetingResponse> findPendingMeeting(int userId, ReadPendingMeetingRequest request) {
        int pageSize = request.size();
        Pageable pageable = PageRequest.of(0, pageSize);

        User user = getUser(userId);
        boolean isPublic = true;

        Slice<Meeting> meetings;
        MemberStatus pendingStatus = MemberStatus.PENDING;
        if(Objects.isNull(request.lastMeetingId()) || request.lastMeetingId() <=0 ){
            meetings = memberRepository.findPendingMeetingByUser_userOrderByMeetingIdDesc(user, pendingStatus, isPublic, pageable);
        }else{
            meetings = memberRepository.findPendingMeetingByUser_userLessThanOrderByMeetingIdDesc(user, pendingStatus, isPublic, request.lastMeetingId(), pageable);
        }

        List<ReadAllMeetingResponse> meetingResponses = meetings.stream()
                .map(meeting -> {
                    String status = memberRepository.findByUserAndMeeting(user, meeting).getStatus().toString();
                    boolean isMeetingManager = meeting.getUser().getUserId() == userId;
                    List<ReadAllMeetingMemberResponse> memberResponseList = memberRepository.findByMeeting(meeting)
                            .stream()
                            .filter(member -> member.getStatus().equals(MemberStatus.APPROVED))
                            .map(member -> ReadAllMeetingMemberResponse.fromEntity(member.getUser()))
                            .toList();
                    return ReadAllMeetingResponse.fromEntity(meeting, status, isMeetingManager, memberResponseList);
                }).toList();

        Integer nextCursor = meetings.hasNext()
                ? meetings.getContent().get(meetings.getContent().size() -1).getMeetingId()
                : null;

        return new CustomSlice<>(meetingResponses, pageable, meetings.hasNext(), nextCursor);
    }


    private User getUser(int userId){
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new UserNotFoundException("UserId: "+ userId));
        return user;
    }

    private Meeting getMeeting(int meetingId){
        Meeting meeting = meetingRepository.findById(meetingId)
                .orElseThrow(()-> new MeetingNotFoundException("MeetingId: "+ meetingId));
        return meeting;
    }

    private boolean validateMeetingManager(int userId, Meeting meeting){
        if(meeting.getUser().getUserId() == userId) return true;
        else return false;
    }

    private Member getMemberWithUserAndMeeting(int userId, Meeting meeting){
        User requestingUser= userRepository.findById(userId)
                .orElseThrow(()-> new UserNotFoundException(String.valueOf(userId)));
        Member member = memberRepository.findByUserAndMeeting(requestingUser, meeting);
        return member;
    }

    private <T> T saveUpdatedMember(Member member, MemberStatus status){
        member.updateStatus(status);
        Member savedMember = memberRepository.save(member);

        if(status == MemberStatus.EXPEL){
            return (T) UpdateMemberToExpelResponse.fromEntity(savedMember.getMemberId());
        }else{
            return (T) UpdateMemberStatusResponse.fromEntity(savedMember);
        }
    }
}
