package com.codeit.moim.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="token_id")
    private int tokenId;

    @Column(name= "token")
    private String token;

    @Column(name= "expiry_date")
    private Instant expiryDate;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Builder
    public RefreshToken(int tokenId, String token, Instant expiryDate, User user) {
        this.tokenId = tokenId;
        this.token = token;
        this.expiryDate = expiryDate;
        this.user = user;
    }

    public static RefreshToken toEntity(String token, Instant expiryDate, User user){
        return RefreshToken.builder()
                .token(token)
                .expiryDate(expiryDate)
                .user(user)
                .build();
    }

    public  void updateToken(String token, Instant expiryDate){
       this.token = token;
       this.expiryDate = expiryDate;
    }
}
