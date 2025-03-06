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
import com.codeit.moim.web.dto.request.comment.ReadMeetingCommentRequest;
import com.codeit.moim.web.dto.request.comment.ReadMyCommentRequest;
import com.codeit.moim.web.dto.request.comment.UpdateCommentRequest;
import com.codeit.moim.web.dto.response.comment.*;
import com.codeit.moim.web.dto.response.slice.CustomSlice;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

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
        if(commentRepository.existsByUserAndMeeting(user, meeting)) throw new CommentExistException("Comment exists by user and meeting. UserId: " + userId + " MeetingId: "+ meetingId);

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
                .orElseThrow(()-> new CommentNotFoundException("Comment not found by user and meeting. UserId: "+ userId+ " MeetingId: "+ meetingId));

        if (comment.getUser().getUserId() != userId) throw new CommentAccessDeniedException("Only creator of this comment can update" + userId);

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
                || memberRepository.findByUserAndMeeting(user, meeting).getStatus() == MemberStatus.REJECTED)  throw new CommentAccessDeniedException("Only APPROVED/QUIT/EXPEL user can delete. UserId: "+ userId);

        Comment comment = commentRepository.findByUserAndMeeting(user, meeting)
                .orElseThrow(()-> new CommentNotFoundException("Comment not found by user and meeting. UserId: "+ userId+ " MeetingId: "+ meetingId));

        if (comment.getUser().getUserId() != userId) throw new CommentAccessDeniedException("Only creator of this comment can delete. UserId: " + userId);

        commentRepository.delete(comment);
        return DeleteCommentResponse.fromEntity(userId, meetingId);
    }

    @Override
    public ReadCommentAverageResponse getCommentAverage(int meetingId) {
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

    @Override
    public ReadCommentDistributionResponse getCommentDistribution(int meetingId) {
        Meeting meeting = getMeeting(meetingId);
        List<Comment> commentList = commentRepository.findByMeeting(meeting);

        Map<Integer, Long> getScoreDistribution = commentList.stream()
                .collect(Collectors.groupingBy(Comment::getScore, Collectors.counting()));
        long fives =  getScoreDistribution.getOrDefault(5, 0L);
        long fours =  getScoreDistribution.getOrDefault(4, 0L);
        long threes =  getScoreDistribution.getOrDefault(3, 0L);
        long twos =  getScoreDistribution.getOrDefault(2, 0L);
        long ones =  getScoreDistribution.getOrDefault(1, 0L);
        return ReadCommentDistributionResponse.fromEntity(fives, fours, threes, twos, ones);
    }

    @Override
    public Slice<ReadMeetingCommentResponse> getMeetingComments(int meetingId, ReadMeetingCommentRequest request) {
        int pageSize = request.size();
        Pageable pageable = PageRequest.of(0, pageSize);

        Meeting meeting = getMeeting(meetingId);

        Slice<Comment> comments;
        if(Objects.isNull(request.lastCommentId()) || request.lastCommentId() <=0 ) {
            comments = commentRepository.findByMeeting_MeetingIdOrderByCommentIdDesc(meetingId, pageable);
            //comments = commentRepository.findByMeetingAndUser(meeting, pageable);

        }else{
            comments = commentRepository.findByMeeting_MeetingIdAndCommentIdLessThanOrderByCommentIdDesc(meetingId, request.lastCommentId(), pageable);
            //comments = commentRepository.findByMeetingAndUserGreaterThan(meeting, request.lastCommentId(), pageable);
        }

        List<ReadMeetingCommentResponse> commentResponses = comments.stream()
                .map(
                        comment -> ReadMeetingCommentResponse.fromEntity(comment, meeting, comment.getUser())
                ).collect(Collectors.toList());

        Integer nextCursor = comments.hasNext()
                ? comments.getContent().get(comments.getContent().size() -1).getCommentId()
                : null;

        return new CustomSlice<>(commentResponses, pageable, comments.hasNext(), nextCursor);
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

    private void validateApprovedMember(User user, Meeting meeting){
        if(!memberRepository.existsByUserAndMeetingAndStatus(user, meeting, MemberStatus.APPROVED)) throw new CommentAccessDeniedException("Only approved member of this meeting can create comment. UserId: " + user.getUserId());
    }
}
