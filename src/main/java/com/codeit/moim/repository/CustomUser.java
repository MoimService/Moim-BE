package com.codeit.moim.repository;

import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.List;

@Getter
public class CustomUser extends User {
    private final com.codeit.moim.domain.User user;
    public CustomUser(com.codeit.moim.domain.User user) {
        super(user.getName(), user.getPassword(), List.of(new SimpleGrantedAuthority("USER")));
        this.user = user;
    }
}
