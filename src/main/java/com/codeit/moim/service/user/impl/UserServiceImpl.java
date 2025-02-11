package com.codeit.moim.service.user.impl;

import com.codeit.moim.common.exception.ApplicationException;
import com.codeit.moim.common.exception.auth.UserNotFoundException;
import com.codeit.moim.common.exception.payload.ErrorStatus;
import com.codeit.moim.domain.User;
import com.codeit.moim.repository.UserRepository;
import com.codeit.moim.service.user.UserService;
import com.codeit.moim.web.dto.request.auth.LoginRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    @Override
    public String login(LoginRequest loginRequest) {

        String email = loginRequest.email();
        String password = loginRequest.password();

        try{
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new UserNotFoundException(email));

        }catch(Exception e){
            e.printStackTrace();
            throw new ApplicationException(new ErrorStatus(
                    "Error while logging in",
                    500,
                    LocalDateTime.now()
            ));
        }

        return null; //token
    }
}
