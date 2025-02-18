package com.codeit.moim.service.member.impl;

import com.codeit.moim.common.exception.auth.UserNotFoundException;
import com.codeit.moim.common.exception.member.AlreadyMemberException;
import com.codeit.moim.common.exception.meeting.MeetingAccessDeniedException;
import com.codeit.moim.common.exception.meeting.MeetingNotFoundException;
import com.codeit.moim.common.exception.meeting.MemberCountExistException;
import com.codeit.moim.common.exception.payload.ErrorStatus;
import com.codeit.moim.domain.Meeting;
import com.codeit.moim.domain.Member;
import com.codeit.moim.domain.User;
import com.codeit.moim.domain.enums.MemberStatus;
import com.codeit.moim.repository.MeetingRepository;
import com.codeit.moim.repository.MemberRepository;
import com.codeit.moim.repository.UserRepository;
import com.codeit.moim.service.member.MemberService;
import com.codeit.moim.web.dto.request.member.CreateMemberRequest;
import com.codeit.moim.web.dto.response.member.CreateMemberResponse;
import com.codeit.moim.web.dto.response.member.DeleteMemberResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
    private final MeetingRepository meetingRepository;
    private final UserRepository userRepository;
    private final MemberRepository memberRepository;

    private static final int BAD_REQUEST = 400;
    @Override
    public CreateMemberResponse saveMember(int meetingId, int userId, CreateMemberRequest request) {

        Meeting meeting = getMeeting(meetingId);
        User user = getUser(userId);

        //비공개 인지 아닌지 확인
        if(!meeting.isPublic()){
            throw new MeetingAccessDeniedException(String.valueOf(meetingId));
        }
        //정원 초과인지 아닌지 확인
        if(meeting.getMaxMember() <= meeting.getMemberCount()){
            throw new MemberCountExistException("Meeting member count is full", String.valueOf(meetingId), "member");
        }

        //이미 신청한 모임인지 아닌지
        if(memberRepository.existsByUserAndMeeting(user, meeting)){
            Member member = memberRepository.findByUserAndMeeting(user, meeting);
            throw new AlreadyMemberException(ErrorStatus.toErrorStatus("User is already member of meeting : " + member.getStatus(), BAD_REQUEST));
        }

        //주최자의 승인이 필요
        if(meeting.isRequireApproval()){
            //member 생성
            Member member =  Member.toEntity(user, meeting, MemberStatus.PENDING, request.message());
            Member savedMember = memberRepository.save(member);
            return CreateMemberResponse.fromEntity(savedMember);
        }else{
            Member member = Member.toEntity(user, meeting, MemberStatus.APPROVED, request.message());
            Member savedMember = memberRepository.save(member);
            //멤버수늘리기
            meeting.increaseMemberCount();
            meetingRepository.save(meeting);
            return CreateMemberResponse.fromEntity(savedMember);
        }
    }


    private Meeting getMeeting(int meetingId){
        Meeting meeting = meetingRepository.findById(meetingId)
                .orElseThrow(()-> new MeetingNotFoundException(String.valueOf(meetingId)));
        return meeting;
    }
    private User getUser(int userId){
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new UserNotFoundException(String.valueOf(userId)));
        return user;
    }
}
