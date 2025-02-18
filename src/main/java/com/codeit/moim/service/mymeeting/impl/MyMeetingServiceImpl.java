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
import com.codeit.moim.web.dto.response.mymeeting.UpdateMemberStatusResponse;
import com.codeit.moim.web.dto.response.mymeeting.UpdateMemberToExpelResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MyMeetingServiceImpl implements MyMeetingService {
    private final UserRepository userRepository;
    private final MeetingRepository meetingRepository;
    private final MemberRepository memberRepository;
    @Override
    public UpdateMemberStatusResponse updateMemberStatus(int userId, UpdateMemberStatusRequest request) {
        //check if user is manager
        if(!checkMeetingManager(userId, request.meetingId())) throw new MeetingAccessDeniedException(String.valueOf(request.meetingId()));

        //get member
        Meeting meeting = meetingRepository.findById(request.meetingId())
                .orElseThrow(()-> new MeetingNotFoundException(String.valueOf(request.meetingId())));
        Member member = getMemberWithUserAndMeeting(request.userId(), request.meetingId());
        //change member status, save
        MemberStatus status = MemberStatus.valueOf(request.setMemberStatus());
        if(member.getStatus().equals(MemberStatus.PENDING)){
            if(status.equals(MemberStatus.APPROVED)){
                meeting.increaseMemberCount();
                meetingRepository.save(meeting);
            }
            member.updateStatus(status);
            Member savedMember = memberRepository.save(member);
            return UpdateMemberStatusResponse.fromEntity(savedMember);
        }else{
            throw new MeetingAccessDeniedException(String.valueOf(request.userId()));
        }
    }

    @Override
    public UpdateMemberToExpelResponse expelMember(int userId, UpdateMemberStatusRequest request) {
        if(!checkMeetingManager(userId, request.meetingId())) throw new MeetingAccessDeniedException(String.valueOf(request.meetingId()));
        Meeting meeting = meetingRepository.findById(request.meetingId())
                .orElseThrow(()-> new MeetingNotFoundException(String.valueOf(request.meetingId())));
        Member member = getMemberWithUserAndMeeting(request.userId(), request.meetingId());
        if(member.getStatus().equals(MemberStatus.APPROVED)){
            member.updateStatus(MemberStatus.EXPEL);
            Member savedMember = memberRepository.save(member);
            meeting.decreaseMemberCount();
            meetingRepository.save(meeting);
            return new UpdateMemberToExpelResponse(savedMember.getMemberId());
        }else{
            throw new MeetingAccessDeniedException(String.valueOf(request.userId()));
        }
    }

    private boolean checkMeetingManager(int userId, int meetingId){
        User managerUser = userRepository.findById(userId)
                .orElseThrow(()-> new UserNotFoundException(String.valueOf(userId)));
        Meeting meeting = meetingRepository.findById(meetingId)
                .orElseThrow(()-> new MeetingNotFoundException(String.valueOf(meetingId)));
        if(meeting.getUser().getUserId() == managerUser.getUserId()) return true;
        else return false;
    }

    private Member getMemberWithUserAndMeeting(int userId, int meetingId){
        User requestingUser= userRepository.findById(userId)
                .orElseThrow(()-> new UserNotFoundException(String.valueOf(userId)));
        Meeting meeting = meetingRepository.findById(meetingId)
                .orElseThrow(()-> new MeetingNotFoundException(String.valueOf(meetingId)));
        Member member = memberRepository.findByUserAndMeeting(requestingUser, meeting);
        return member;
    }
}
