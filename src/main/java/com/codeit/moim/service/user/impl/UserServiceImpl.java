package com.codeit.moim.service.user.impl;

import com.codeit.moim.common.config.JwtTokenProvider;
import com.codeit.moim.common.exception.auth.LoginException;
import com.codeit.moim.common.exception.global.ApplicationException;
import com.codeit.moim.common.exception.auth.PasswordInvlaidException;
import com.codeit.moim.common.exception.auth.SignUpExistException;
import com.codeit.moim.common.exception.auth.UserNotFoundException;
import com.codeit.moim.common.exception.payload.ErrorStatus;
import com.codeit.moim.domain.Contact;
import com.codeit.moim.domain.RefreshToken;
import com.codeit.moim.domain.User;
import com.codeit.moim.repository.ContactRepository;
import com.codeit.moim.repository.RefreshTokenRepository;
import com.codeit.moim.repository.UserRepository;
import com.codeit.moim.service.user.UserService;
import com.codeit.moim.web.dto.request.auth.LoginRequest;
import com.codeit.moim.web.dto.request.auth.SignUpRequest;
import com.codeit.moim.web.dto.response.auth.LogoutResponse;
import com.codeit.moim.web.dto.response.auth.SignUpCheckResponse;
import com.codeit.moim.web.dto.response.auth.SignUpResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final ContactRepository contactRepository;
    private final RefreshTokenRepository refreshTokenRepository;



    @Override
    public SignUpResponse signUpUser(SignUpRequest signUpRequest) {
        userNameCheck(signUpRequest.name());
        userEmailCheck(signUpRequest.email());
        //password check
        passwordMatchValidation(signUpRequest.password(), signUpRequest.passwordCheck());

        //encode password
        String encodedPassword = passwordEncoder.encode(signUpRequest.password());

        //profile_pic, intro
        String profilePic = "https://deving-bucket.s3.ap-northeast-2.amazonaws.com/profile_img.png";
        String intro = "안녕하세요, 개발자 " + signUpRequest.name() + "입니다.";

        User user = signUpRequest.toEntity(encodedPassword, profilePic, intro);
        User savedUser = userRepository.save(user);

        //create contact
        Contact contact = Contact.toEntity(user);
        contactRepository.save(contact);

        return new SignUpResponse(savedUser.getUserId());
    }


    @Override
    public String login(LoginRequest loginRequest) {

        String email = loginRequest.email();
        String password = loginRequest.password();

        try {
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new UserNotFoundException("email: "+ email));

            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            return jwtTokenProvider.createToken(email);

        }catch(Exception e) {
            e.printStackTrace();
            throw new LoginException("Error while logging in. Email: " + email);
        }
    }

    @Override
    public SignUpCheckResponse userNameCheck(String name) {
        if(userRepository.existsByName(name)){
            throw new SignUpExistException("user name: " + name);
        }
        else{
            return new SignUpCheckResponse(true);
        }
    }

    @Override
    public SignUpCheckResponse userEmailCheck(String email) {
        if(userRepository.existsByEmail(email)){
            throw new SignUpExistException("user email: " + email);
        }
        else{
            return new SignUpCheckResponse(true);
        }
    }

    @Override
    public void logout(HttpServletRequest request, int userId) {
        //access token -> add to blacklist
        String accessToken = jwtTokenProvider.resolveToken(request);
        jwtTokenProvider.addToBlackList(accessToken);
        //refresh token -> delete from DB
        User user = userRepository.findById(userId)
                        .orElseThrow(()-> new UserNotFoundException("userId: " + userId));
        refreshTokenRepository.deleteByUser(user);
    }

    public void passwordMatchValidation(String password, String passwordCheck){
        if(! password.equals(passwordCheck) ) throw new PasswordInvlaidException("Password does not match");
    }




}
