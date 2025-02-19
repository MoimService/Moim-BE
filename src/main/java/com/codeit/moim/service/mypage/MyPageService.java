package com.codeit.moim.service.mypage;

import com.codeit.moim.web.dto.request.mypage.UpdateProfilePicRequest;
import com.codeit.moim.web.dto.response.member.CreateMemberResponse;
import com.codeit.moim.web.dto.response.mypage.ReadLoggedInUserResponse;
import com.codeit.moim.web.dto.response.mypage.UpdateProfilePicResponse;

public interface MyPageService {
    UpdateProfilePicResponse updateProfilePic(int userId, UpdateProfilePicRequest request);

    ReadLoggedInUserResponse getUserData(int userId);
}
