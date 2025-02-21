package com.codeit.moim.service.mypage.impl;

import com.codeit.moim.common.exception.auth.PasswordInvlaidException;
import com.codeit.moim.common.exception.auth.UserNotFoundException;
import com.codeit.moim.common.exception.payload.ErrorStatus;
import com.codeit.moim.domain.Contact;
import com.codeit.moim.domain.Skill;
import com.codeit.moim.domain.User;
import com.codeit.moim.domain.UserSkill;
import com.codeit.moim.repository.ContactRepository;
import com.codeit.moim.repository.SkillRepository;
import com.codeit.moim.repository.UserRepository;
import com.codeit.moim.repository.UserSkillRepository;
import com.codeit.moim.service.mypage.MyPageService;
import com.codeit.moim.service.storage.StorageService;
import com.codeit.moim.service.user.impl.UserServiceImpl;
import com.codeit.moim.web.dto.request.mypage.*;
import com.codeit.moim.web.dto.response.member.CreateMemberResponse;
import com.codeit.moim.web.dto.response.mypage.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MyPageServiceImpl implements MyPageService {
    private final UserRepository userRepository;
    private final StorageService storageService;
    private final ContactRepository contactRepository;
    private final UserSkillRepository userSkillRepository;
    private final SkillRepository skillRepository;
    private final UserServiceImpl userServiceImpl;
    private final PasswordEncoder passwordEncoder;

    private static final int BAD_REQUEST = 400;

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

    @Override
    public ReadLoggedInUserResponse getUserData(int userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new UserNotFoundException(String.valueOf(userId)));
        String phone = (user.getContact() != null && user.getContact().getPhone() != null)
                ? user.getContact().getPhone()
                : null;
        return ReadLoggedInUserResponse.fromEntity(user, phone);
    }

    @Override
    public UpdateContactResponse updateUserContact(int userId, UpdateContactRequest request) {
        User user = getUser(userId);
        Contact contact = contactRepository.findByUser(user);
        contact.updateContact(request);
        contactRepository.save(contact);

        return new UpdateContactResponse(userId);
    }

    @Override
    @Transactional
    public CreateUserSkillResponse createUserSkill(int userId, CreateUserSkillRequest request) {
        User user = getUser(userId);

        userSkillRepository.deleteAllByUser(user);


        List<Skill> skillList= Arrays.asList(request.skillArray()).stream()
                .map(skillRepository::findBySkillTitle)
                .collect(Collectors.toList());

        List<UserSkill> userSkillList = skillList.stream()
                .map(skill-> request.toEntity(user, skill))
                .collect(Collectors.toList());

        userSkillRepository.saveAll(userSkillList);

        return new CreateUserSkillResponse(userId);
    }

    @Override
    public UpdateUserResponse updateUserInfo(int userId, UpdateUserRequest request) {
        User user = getUser(userId);
        user.updateUser(request);
        userRepository.save(user);
        return new UpdateUserResponse(userId);
    }

    @Override
    public UpdatePasswordResponse updateUserPassword(int userId, UpdatePasswordRequest request) {
        User user = getUser(userId);
        String dbPassword = user.getPassword();
        String currentPasswordRequest = request.currentPassword();
        if( ! passwordEncoder.matches(currentPasswordRequest, dbPassword)) throw new PasswordInvlaidException(ErrorStatus.toErrorStatus("Current password does not match",  BAD_REQUEST));

        userServiceImpl.passwordMatchValidation(request.newPassword(), request.passwordCheck());
        String encodedPassword= passwordEncoder.encode(request.newPassword());
        user.updatePassword(encodedPassword);
        userRepository.save(user);

        return new UpdatePasswordResponse(userId);
    }

    private User getUser(int userId){
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new UserNotFoundException(String.valueOf(userId)));
        return user;
    }
}
