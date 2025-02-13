package com.codeit.moim.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="user_id")
    private int userId;

    @Column(name= "name", nullable = false)
    private String name;

    @Column(name= "email", nullable = false)
    private String email;

    @Column(name= "position", nullable = false)
    private String position;

    @Column(name= "password", nullable = false)
    private String password;

    @Column(name= "profile_pic", nullable = false)
    private String profilePic;

    @Column(name= "intro", nullable = false)
    private String intro;

    @Column(name= "gender", nullable = false)
    private String gender;

    @Column(name= "location", nullable = false)
    private String location;

    @Column(name= "age", nullable = false)
    private String age;

    @OneToOne(mappedBy= "user", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private Contact contact;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<UserSkill> userSkillList = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Meeting> meetingLeaderList = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Member> userMemberList = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Likes> likesList = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Comment> commentList = new ArrayList<>();

    @Builder
    public User(int userId, String name, String email, String position, String password, String profilePic, String intro, String gender, String location, String age) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.position = position;
        this.password = password;
        this.profilePic = profilePic;
        this.intro = intro;
        this.gender = gender;
        this.location = location;
        this.age = age;
    }
}
