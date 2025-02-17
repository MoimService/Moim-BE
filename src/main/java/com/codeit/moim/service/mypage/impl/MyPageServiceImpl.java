package com.codeit.moim.service.mypage.impl;

import com.codeit.moim.common.exception.auth.UserNotFoundException;
import com.codeit.moim.domain.User;
import com.codeit.moim.repository.UserRepository;
import com.codeit.moim.service.mypage.MyPageService;
import com.codeit.moim.service.storage.StorageService;
import com.codeit.moim.web.dto.request.mypage.UpdateProfilePicRequest;
import com.codeit.moim.web.dto.response.member.CreateMemberResponse;
import com.codeit.moim.web.dto.response.mypage.UpdateProfilePicResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class MyPageServiceImpl implements MyPageService {
    private final UserRepository userRepository;
    private final StorageService storageService;
    @Override
    public UpdateProfilePicResponse updateProfilePic(int userId, UpdateProfilePicRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new UserNotFoundException(String.valueOf(userId)));

        String newProfilePicUrl = "";
        if(Objects.nonNull(request.profilePicBase64()) && !request.profilePicBase64().isEmpty()){

            newProfilePicUrl = storageService.uploadFile(request.profilePicBase64(), request.profilePicName());
        }

        user.updateProfilePic(newProfilePicUrl);
        userRepository.save(user);

        return new UpdateProfilePicResponse(userId);
    }
}
