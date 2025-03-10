package com.codeit.moim.service.mypage;

import com.codeit.moim.web.dto.request.comment.ReadMyCommentRequest;
import com.codeit.moim.web.dto.request.comment.ReadMyMeetingCommentRequest;
import com.codeit.moim.web.dto.request.mypage.*;
import com.codeit.moim.web.dto.response.comment.ReadMyCommentResponse;
import com.codeit.moim.web.dto.response.comment.ReadMyMeetingCommentResponse;
import com.codeit.moim.web.dto.response.member.CreateMemberResponse;
import com.codeit.moim.web.dto.response.mypage.*;
import org.springframework.data.domain.Slice;

public interface MyPageService {
    UpdateProfilePicResponse updateProfilePic(int userId, UpdateProfilePicRequest request);

    ReadLoggedInUserResponse getUserData(int userId);

    UpdateContactResponse updateUserContact(int userId, UpdateContactRequest request);

    CreateUserSkillResponse createUserSkill(int userId, CreateUserSkillRequest request);

    UpdateUserResponse updateUserInfo(int userId, UpdateUserRequest request);

    UpdatePasswordResponse updateUserPassword(int userId, UpdatePasswordRequest request);

    Slice<ReadMyCommentResponse> getMyComments(int userId, ReadMyCommentRequest request);

    ReadUserResponse readUser(int userId);

    Slice<ReadMyMeetingCommentResponse> getMyCommentableMeeting(int userId, ReadMyMeetingCommentRequest request);
}
