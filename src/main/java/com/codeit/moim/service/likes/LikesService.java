package com.codeit.moim.service.likes;

import com.codeit.moim.web.dto.response.likes.CreateLikeResponse;

public interface LikesService {
    CreateLikeResponse createLikes(int userId, int meetingId);
}
