package com.codeit.moim.service.mypage;

import com.codeit.moim.web.dto.request.mypage.*;
import com.codeit.moim.web.dto.response.member.CreateMemberResponse;
import com.codeit.moim.web.dto.response.mypage.*;

public interface MyPageService {
    UpdateProfilePicResponse updateProfilePic(int userId, UpdateProfilePicRequest request);

    ReadLoggedInUserResponse getUserData(int userId);

    UpdateContactResponse updateUserContact(int userId, UpdateContactRequest request);

    CreateUserSkillResponse createUserSkill(int userId, CreateUserSkillRequest request);

    UpdateUserResponse updateUserInfo(int userId, UpdateUserRequest request);

    UpdatePasswordResponse updateUserPassword(int userId, UpdatePasswordRequest request);
}
