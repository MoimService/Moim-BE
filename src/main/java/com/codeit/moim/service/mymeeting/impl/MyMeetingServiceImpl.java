package com.codeit.moim.service.mymeeting.impl;

import com.codeit.moim.common.exception.auth.MeetingManagerException;
import com.codeit.moim.common.exception.auth.UserNotFoundException;
import com.codeit.moim.common.exception.meeting.AlreadyIsPublicException;
import com.codeit.moim.common.exception.meeting.MeetingAccessDeniedException;
import com.codeit.moim.common.exception.meeting.MeetingNotFoundException;
import com.codeit.moim.common.exception.member.MemberNotFoundException;
import com.codeit.moim.common.exception.payload.ErrorStatus;
import com.codeit.moim.domain.*;
import com.codeit.moim.domain.enums.MemberStatus;
import com.codeit.moim.repository.*;
import com.codeit.moim.service.mymeeting.MyMeetingService;
import com.codeit.moim.web.dto.request.likes.ReadLikeMeetingRequest;
import com.codeit.moim.web.dto.request.mymeeting.ReadMemberProfileRequest;
import com.codeit.moim.web.dto.request.mymeeting.UpdateMemberStatusRequest;
import com.codeit.moim.web.dto.response.member.DeleteMemberResponse;
import com.codeit.moim.web.dto.response.mymeeting.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import com.codeit.moim.web.dto.response.slice.CustomSlice;


import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class MyMeetingServiceImpl implements MyMeetingService {
    private final UserRepository userRepository;
    private final MeetingRepository meetingRepository;
    private final MemberRepository memberRepository;
    private final LikesRepository likesRepository;
    private final UserSkillRepository userSkillRepository;

    private static final int BAD_REQUEST = 400;

    @Override
    public UpdateMemberStatusResponse updateMemberStatus(int userId, UpdateMemberStatusRequest request) {
        Meeting meeting = getMeeting(request.meetingId());
        //check if user is manager
        if(!validateMeetingManager(userId, meeting)) throw new MeetingAccessDeniedException(String.valueOf(meeting.getMeetingId()));

        //get member
        Member member = getMemberWithUserAndMeeting(request.userId(), meeting);
        //change member status, save
        MemberStatus status = MemberStatus.valueOf(request.setMemberStatus());
        if(!member.getStatus().equals(MemberStatus.PENDING)) throw new MeetingAccessDeniedException(String.valueOf(request.userId()));

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
        if(!validateMeetingManager(userId, meeting)) throw new MeetingAccessDeniedException(String.valueOf(meeting.getMeetingId()));
        Member member = getMemberWithUserAndMeeting(request.userId(), meeting);
        if( !member.getStatus().equals(MemberStatus.APPROVED)) throw new MeetingAccessDeniedException(String.valueOf(request.userId()));

        meeting.decreaseMemberCount();
        meetingRepository.save(meeting);
        return saveUpdatedMember(member, MemberStatus.EXPEL);
    }

    @Override
    public List<ReadAllMeetingResponse> findAllMyMeeting(int userId) {
        User user = getUser(userId);
        List<Meeting> meetingList = memberRepository.findMeetingsByUser(user);

        return meetingList.stream()
                .map(meeting -> {
                    String status = memberRepository.findByUserAndMeeting(user, meeting).getStatus().toString();
                    List<ReadAllMeetingMemberResponse> memberResponseList = memberRepository.findByMeeting(meeting)
                            .stream()
                            .filter(member -> member.getStatus().equals(MemberStatus.APPROVED))
                            .map(member -> ReadAllMeetingMemberResponse.fromEntity(member.getUser()))
                            .toList();
                    return ReadAllMeetingResponse.fromEntity(meeting, status, memberResponseList);
                }).toList();
    }

    @Override
    public List<ReadManageMeetingResponse> findManageMeeting(int userId) {
        User user = getUser(userId);
        List<Meeting> meetingList = meetingRepository.findByUser(user);
        return meetingList.stream()
                .map(meeting-> {
                    List<ReadManageMeetingMemberResponse> memberResponseList = memberRepository.findByMeeting(meeting)
                            .stream()
                            .map(member -> ReadManageMeetingMemberResponse.fromEntity(member.getUser(), member.getStatus().toString()))
                            .toList();
                    return ReadManageMeetingResponse.fromEntity(meeting, memberResponseList);
                })
                .toList();
    }

    @Override
    public UpdateMeetingIsPublicResponse updateIsPublic(int userId, int meetingId) {
        Meeting meeting = getMeeting(meetingId);
        if(validateMeetingManager(userId, meeting)) throw new MeetingAccessDeniedException(String.valueOf(meeting.getMeetingId()));
        if(meeting.isPublic()){
            meeting.updateIsPublic();
            meetingRepository.save(meeting);

            return new UpdateMeetingIsPublicResponse(meetingId);
        }else{
            throw new AlreadyIsPublicException(ErrorStatus.toErrorStatus("This meeting is already isPublic = false", BAD_REQUEST));
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
            throw new AccessDeniedException("Member");
        }
    }

    @Override
    public DeleteMemberResponse quitMeeting(int userId, int meetingId) {
        Meeting meeting = getMeeting(meetingId);
        User user = getUser(userId);
        if(validateMeetingManager(userId, meeting)) throw new MeetingManagerException(ErrorStatus.toErrorStatus("Meeting manager cannot quit the meeting", BAD_REQUEST));
        Member member = memberRepository.findByUserAndMeeting(user, meeting);
        if(member != null && member.getStatus().equals(MemberStatus.APPROVED)){
            memberRepository.delete(member);
            meeting.decreaseMemberCount();
            meetingRepository.save(meeting);
            return new DeleteMemberResponse(userId);
        }else{
            throw new AccessDeniedException("Member");
        }
    }

    @Override
    public Slice<ReadLikeMeetingResponse> findLikeMeetings(int userId, ReadLikeMeetingRequest request) {
        int pageSize = request.size();
        Pageable pageable = PageRequest.of(0, pageSize);

        User user = getUser(userId);

        Slice<Meeting> meetings;
        if(Objects.isNull(request.lastMeetingId()) || request.lastMeetingId() <=0 ){
            meetings = likesRepository.findLikedMeetings(user, pageable);
       }else{
            meetings = likesRepository.findLikeMeetingsLessThan(user, request.lastMeetingId(), pageable);
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
        if(requestedUserMember == null) throw new MemberNotFoundException(String.valueOf(request.userId()));
        ReadMemberMessageResponse memberResponse = ReadMemberMessageResponse.fromEntity(requestedUserMember);


        return ReadMemberProfileResponse.fromEntity(requestedUser, skillArray, contactResponse, memberResponse);
    }

    private User getUser(int userId){
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new UserNotFoundException(String.valueOf(userId)));
        return user;
    }

    private Meeting getMeeting(int meetingId){
        Meeting meeting = meetingRepository.findById(meetingId)
                .orElseThrow(()-> new MeetingNotFoundException(String.valueOf(meetingId)));
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
            return (T) new UpdateMemberToExpelResponse(savedMember.getMemberId());
        }else{
            return (T) UpdateMemberStatusResponse.fromEntity(savedMember);
        }
    }
}
