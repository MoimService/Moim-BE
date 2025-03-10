package com.codeit.moim.service.mypage.impl;

import com.codeit.moim.common.exception.auth.PasswordInvlaidException;
import com.codeit.moim.common.exception.auth.UserNotFoundException;
import com.codeit.moim.common.exception.payload.ErrorStatus;
import com.codeit.moim.domain.*;
import com.codeit.moim.repository.*;
import com.codeit.moim.service.mypage.MyPageService;
import com.codeit.moim.service.storage.StorageService;
import com.codeit.moim.service.user.impl.UserServiceImpl;
import com.codeit.moim.web.dto.request.comment.ReadMyCommentRequest;
import com.codeit.moim.web.dto.request.comment.ReadMyMeetingCommentRequest;
import com.codeit.moim.web.dto.request.mypage.*;
import com.codeit.moim.web.dto.response.comment.ReadMyCommentResponse;
import com.codeit.moim.web.dto.response.comment.ReadMyMeetingCommentResponse;
import com.codeit.moim.web.dto.response.member.CreateMemberResponse;
import com.codeit.moim.web.dto.response.mymeeting.ReadMemberContactResponse;
import com.codeit.moim.web.dto.response.mypage.*;
import com.codeit.moim.web.dto.response.slice.CustomSlice;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
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
    private final CommentRepository commentRepository;
    private final UserServiceImpl userServiceImpl;
    private final PasswordEncoder passwordEncoder;

    private static final int BAD_REQUEST = 400;

    @Override
    public UpdateProfilePicResponse updateProfilePic(int userId, UpdateProfilePicRequest request) {
        User user = getUser(userId);

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
        User user = getUser(userId);
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
        if( ! passwordEncoder.matches(currentPasswordRequest, dbPassword)) throw new PasswordInvlaidException("Current password does not match");

        userServiceImpl.passwordMatchValidation(request.newPassword(), request.passwordCheck());
        String encodedPassword= passwordEncoder.encode(request.newPassword());
        user.updatePassword(encodedPassword);
        userRepository.save(user);

        return new UpdatePasswordResponse(userId);
    }

    @Override
    public Slice<ReadMyCommentResponse> getMyComments(int userId, ReadMyCommentRequest request) {
        int pageSize = request.size();
        Pageable pageable = PageRequest.of(0, pageSize);

        Slice<Comment> comments;
        if(Objects.isNull(request.lastCommentId()) || request.lastCommentId() <=0 ) {
            comments = commentRepository.findByUser_userIdOrderByCommentIdDesc(userId, pageable);
        }else{
            comments = commentRepository.findByUser_UserIdAndCommentIdLessThanOrderByCommentIdDesc(userId, request.lastCommentId(), pageable);
        }

        List<ReadMyCommentResponse> commentResponses = comments.stream()
                .map(comment ->
                        ReadMyCommentResponse.fromEntity(comment, comment.getMeeting())
                ).collect(Collectors.toList());

        Integer nextCursor = comments.hasNext()
                ? comments.getContent().get(comments.getContent().size() -1).getCommentId()
                : null;

        return new CustomSlice<>(commentResponses, pageable, comments.hasNext(), nextCursor);
    }

    @Override
    public ReadUserResponse readUser(int userId) {
        User user =getUser(userId);
        String[] userSkillArray = userSkillRepository.findByUserWithSkill(user)
                .stream().map(userSkill -> userSkill.getSkill().getSkillTitle())
                .toArray(String[]::new);

        Contact requestedUserContact = user.getContact();

        ReadMemberContactResponse contactResponse = (requestedUserContact != null)
                ?  ReadMemberContactResponse.fromEntity(requestedUserContact)
                : null;

        return ReadUserResponse.fromEntity(user, userSkillArray, contactResponse);
    }

    @Override
    public Slice<ReadMyMeetingCommentResponse> getMyMeetingForComment(int userId, ReadMyMeetingCommentRequest request) {
        //member 중 approved가 된 meeting
        //List<Meeting> meetingList =
        //중에서 comment에 없는 meeting들을 반환하면 되겠다


        return null;
    }

    private User getUser(int userId){
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new UserNotFoundException("UserId: "+ userId));
        return user;
    }
}
