package com.codeit.moim.service.likes;

import com.codeit.moim.web.dto.response.likes.CreateLikeResponse;
import com.codeit.moim.web.dto.response.likes.DeleteLikeResponse;

public interface LikesService {
    CreateLikeResponse createLikes(int userId, int meetingId);

    DeleteLikeResponse deleteLikes(int userId, int meetingId);
}
