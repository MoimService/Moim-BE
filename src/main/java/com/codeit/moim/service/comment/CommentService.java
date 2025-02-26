package com.codeit.moim.service.comment;

import com.codeit.moim.web.dto.request.comment.CreateCommentRequest;
import com.codeit.moim.web.dto.request.comment.ReadMeetingCommentRequest;
import com.codeit.moim.web.dto.request.comment.ReadMyCommentRequest;
import com.codeit.moim.web.dto.request.comment.UpdateCommentRequest;
import com.codeit.moim.web.dto.response.comment.*;
import org.springframework.data.domain.Slice;

import java.util.List;

public interface CommentService {
    CreateCommentResponse saveComment(int userId, int meetingId, CreateCommentRequest request);

    UpdateCommentResponse updateComment(int userId, int meetingId, UpdateCommentRequest request);

    DeleteCommentResponse deleteComment(int userId, int meetingId);

    ReadCommentAverageResponse getCommentAverage(int meetingId);

    ReadCommentDistributionResponse getCommentDistribution(int meetingId);

    Slice<ReadMeetingCommentResponse> getMeetingComments(int meetingId, ReadMeetingCommentRequest request);

}
