package com.codeit.moim.service.comment.impl;

import com.codeit.moim.common.exception.auth.UserNotFoundException;
import com.codeit.moim.common.exception.comment.CommentAccessDeniedException;
import com.codeit.moim.common.exception.meeting.MeetingNotFoundException;
import com.codeit.moim.domain.Comment;
import com.codeit.moim.domain.Meeting;
import com.codeit.moim.domain.User;
import com.codeit.moim.domain.enums.MemberStatus;
import com.codeit.moim.repository.CommentRepository;
import com.codeit.moim.repository.MeetingRepository;
import com.codeit.moim.repository.MemberRepository;
import com.codeit.moim.repository.UserRepository;
import com.codeit.moim.service.comment.CommentService;
import com.codeit.moim.web.dto.request.comment.CreateCommentRequest;
import com.codeit.moim.web.dto.response.comment.CreateCommentResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final MemberRepository memberRepository;
    private final UserRepository userRepository;
    private final MeetingRepository meetingRepository;
    private final CommentRepository commentRepository;
    @Override
    public CreateCommentResponse saveComment(int userId, int meetingId, CreateCommentRequest request) {
        User user = getUser(userId);
        Meeting meeting = getMeeting(meetingId);
        //check if approved member
        if(!memberRepository.existsByUserAndMeetingAndStatus(user, meeting, MemberStatus.APPROVED)) throw new CommentAccessDeniedException(String.valueOf(userId));

        Comment comment = request.toEntity(user, meeting);
        Comment savedComment = commentRepository.save(comment);

        return new CreateCommentResponse(savedComment.getCommentId());
    }

    //update, delete -> check if my comment

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
}
