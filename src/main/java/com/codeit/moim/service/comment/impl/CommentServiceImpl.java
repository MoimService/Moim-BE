package com.codeit.moim.service.comment.impl;

import com.codeit.moim.common.exception.auth.UserNotFoundException;
import com.codeit.moim.common.exception.comment.CommentAccessDeniedException;
import com.codeit.moim.common.exception.comment.CommentExistException;
import com.codeit.moim.common.exception.comment.CommentNotFoundException;
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
import com.codeit.moim.web.dto.request.comment.UpdateCommentRequest;
import com.codeit.moim.web.dto.response.comment.CreateCommentResponse;
import com.codeit.moim.web.dto.response.comment.DeleteCommentResponse;
import com.codeit.moim.web.dto.response.comment.ReadCommentAverageResponse;
import com.codeit.moim.web.dto.response.comment.UpdateCommentResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

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
        validateApprovedMember(user, meeting);

        //check if exists
        if(commentRepository.existsByUserAndMeeting(user, meeting)) throw new CommentExistException("Comment already exists");

        Comment comment = request.toEntity(user, meeting);
        Comment savedComment = commentRepository.save(comment);
        return new CreateCommentResponse(savedComment.getCommentId());
    }

    @Override
    public UpdateCommentResponse updateComment(int userId, int meetingId, UpdateCommentRequest request) {
        User user = getUser(userId);
        Meeting meeting = getMeeting(meetingId);
        validateApprovedMember(user, meeting);

        Comment comment = commentRepository.findByUserAndMeeting(user, meeting)
                .orElseThrow(()-> new CommentNotFoundException("Comment not found"));

        if (comment.getUser().getUserId() != userId) throw new CommentAccessDeniedException(String.valueOf(user.getUserId()));

        comment.update(request.score(), request.content());
        commentRepository.save(comment);
        return new UpdateCommentResponse(comment.getCommentId());
    }

    @Override
    public DeleteCommentResponse deleteComment(int userId, int meetingId) {
        User user = getUser(userId);
        Meeting meeting = getMeeting(meetingId);
        if( !memberRepository.existsByUserAndMeeting(user, meeting)
                || memberRepository.findByUserAndMeeting(user, meeting).getStatus() == MemberStatus.PENDING
                || memberRepository.findByUserAndMeeting(user, meeting).getStatus() == MemberStatus.REJECTED)  throw new CommentAccessDeniedException(String.valueOf(user.getUserId()));

        Comment comment = commentRepository.findByUserAndMeeting(user, meeting)
                .orElseThrow(()-> new CommentNotFoundException("Comment not found"));

        if (comment.getUser().getUserId() != userId) throw new CommentAccessDeniedException(String.valueOf(user.getUserId()));

        commentRepository.delete(comment);
        return DeleteCommentResponse.fromEntity(userId, meetingId);
    }

    @Override
    public ReadCommentAverageResponse getCommentAverage(int userId, int meetingId) {
        Meeting meeting = getMeeting(meetingId);
        List<Comment> commentList = commentRepository.findByMeeting(meeting);

        int scoreSum = commentList.stream()
                .mapToInt(Comment::getScore)
                .sum();
        int scoreCount = commentList.size();
        double scoreAvg = scoreCount == 0 ? 0.0: (double) scoreSum / scoreCount;
        double roundedAvg = Math.round(scoreAvg * 10.0) / 10.0;
        return new ReadCommentAverageResponse(roundedAvg);
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

    private void validateApprovedMember(User user, Meeting meeting){
        if(!memberRepository.existsByUserAndMeetingAndStatus(user, meeting, MemberStatus.APPROVED)) throw new CommentAccessDeniedException(String.valueOf(user.getUserId()));
    }
}
