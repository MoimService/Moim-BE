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
import com.codeit.moim.service.member.impl.MemberServiceImpl;
import com.codeit.moim.service.mymeeting.MyMeetingService;
import com.codeit.moim.web.dto.request.mymeeting.UpdateMemberStatusRequest;
import com.codeit.moim.web.dto.response.mymeeting.UpdateMemberStatusResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
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
        User managerUser = userRepository.findById(userId)
                .orElseThrow(()-> new UserNotFoundException(String.valueOf(userId)));
        Meeting meeting = meetingRepository.findById(request.meetingId())
                .orElseThrow(()-> new MeetingNotFoundException(String.valueOf(request.meetingId())));
        if(meeting.getUser().getUserId() != managerUser.getUserId()) throw new MeetingAccessDeniedException(String.valueOf(request.meetingId()));

        //get member
        User requestingUser= userRepository.findById(request.userId())
                .orElseThrow(()-> new UserNotFoundException(String.valueOf(request.userId())));
        Member member = memberRepository.findByUserAndMeeting(requestingUser, meeting);
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
}
