package com.codeit.moim.service.comment;

import com.codeit.moim.web.dto.request.comment.CreateCommentRequest;
import com.codeit.moim.web.dto.response.comment.CreateCommentResponse;

public interface CommentService {
    CreateCommentResponse saveComment(int userId, int meetingId, CreateCommentRequest request);
}
