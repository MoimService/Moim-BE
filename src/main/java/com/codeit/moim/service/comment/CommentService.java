package com.codeit.moim.service.comment;

import com.codeit.moim.web.dto.request.comment.CreateCommentRequest;
import com.codeit.moim.web.dto.request.comment.UpdateCommentRequest;
import com.codeit.moim.web.dto.response.comment.CreateCommentResponse;
import com.codeit.moim.web.dto.response.comment.UpdateCommentResponse;

public interface CommentService {
    CreateCommentResponse saveComment(int userId, int meetingId, CreateCommentRequest request);

    UpdateCommentResponse updateComment(int userId, int meetingId, UpdateCommentRequest request);
}
