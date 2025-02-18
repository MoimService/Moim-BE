package com.codeit.moim.service.mymeeting.impl;

import com.codeit.moim.common.exception.auth.UserNotFoundException;
import com.codeit.moim.common.exception.meeting.MeetingAccessDeniedException;
import com.codeit.moim.common.exception.meeting.MeetingNotFoundException;
import com.codeit.moim.domain.Meeting;
import com.codeit.moim.domain.Member;
import com.codeit.moim.domain.User;
import com.codeit.moim.domain.enums.MemberStatus;
import com.codeit.moim.repository.MeetingRepository;
import com.codeit.moim.repository.MemberRepository;
import com.codeit.moim.repository.UserRepository;
import com.codeit.moim.service.mymeeting.MyMeetingService;
import com.codeit.moim.web.dto.request.mymeeting.UpdateMemberStatusRequest;
import com.codeit.moim.web.dto.response.mymeeting.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MyMeetingServiceImpl implements MyMeetingService {
    private final UserRepository userRepository;
    private final MeetingRepository meetingRepository;
    private final MemberRepository memberRepository;
    @Override
    public UpdateMemberStatusResponse updateMemberStatus(int userId, UpdateMemberStatusRequest request) {
        Meeting meeting = getMeeting(request.meetingId());
        //check if user is manager
        validateMeetingManager(userId, meeting);
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
        validateMeetingManager(userId, meeting);
        Member member = getMemberWithUserAndMeeting(request.userId(), meeting);
        if( !member.getStatus().equals(MemberStatus.APPROVED)) throw new MeetingAccessDeniedException(String.valueOf(request.userId()));

        meeting.decreaseMemberCount();
        meetingRepository.save(meeting);
        return saveUpdatedMember(member, MemberStatus.EXPEL);
    }

    @Override
    public List<ReadAllMeetingResponse> findAllMyMeeting(int userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new UserNotFoundException(String.valueOf(userId)));
        List<Meeting> meetingList = memberRepository.findMeetingsByUser(user);

        return meetingList.stream()
                .map(meeting -> {
                    String status = memberRepository.findByUserAndMeeting(user, meeting).getStatus().toString();
                    List<ReadAllMeetingMemberResponse> memberResponseList = memberRepository.findByMeeting(meeting)
                            .stream()
                            .map(member -> ReadAllMeetingMemberResponse.fromEntity(member.getUser()))
                            .collect(Collectors.toList());
                    return ReadAllMeetingResponse.fromEntity(meeting, status, memberResponseList);
                }).toList();
    }

    @Override
    public List<ReadManageMeetingResponse> findManageMeeting(int userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new UserNotFoundException(String.valueOf(userId)));
        List<Meeting> meetingList = meetingRepository.findByUser(user);
        return meetingList.stream()
                .map(ReadManageMeetingResponse::fromEntity)
                .collect(Collectors.toList());
    }

    private Meeting getMeeting(int meetingId){
        Meeting meeting = meetingRepository.findById(meetingId)
                .orElseThrow(()-> new MeetingNotFoundException(String.valueOf(meetingId)));
        return meeting;
    }

    private void validateMeetingManager(int userId, Meeting meeting){
        if(meeting.getUser().getUserId() != userId) throw new MeetingAccessDeniedException(String.valueOf(meeting.getMeetingId()));
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
