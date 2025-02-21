package com.codeit.moim.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Contact {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="contact_id")
    private int contactId;

    @Column(name= "phone")
    private String phone;

    @Column(name= "kakao")
    private String kakao;

    @Column(name= "github")
    private String github;

    @Column(name= "blog")
    private String blog;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Builder
    public Contact(int contactId, String phone, String kakao, String github, String blog, User user) {
        this.contactId = contactId;
        this.phone = phone;
        this.kakao = kakao;
        this.github = github;
        this.blog = blog;
        this.user = user;
    }

    public static Contact toEntity(User user){
        return Contact.builder()
                .user(user)
                .build();
    }

}
