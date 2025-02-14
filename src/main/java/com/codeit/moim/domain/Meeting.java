package com.codeit.moim.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Meeting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="meeting_id")
    private int meetingId;

    @Column(name= "meeting_title", nullable = false)
    private String meetingTitle;

    @Column(name= "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name= "thumbnail", nullable = false)
    private String thumbnail;

    @Column(name= "content", nullable = false)
    private String content;

    @Column(name= "location", nullable = false)
    private String location;

    @Column(name= "max_member", nullable = false)
    private int maxMember;

    @Column(name= "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name= "is_public", nullable = false)
    private boolean isPublic;

    @Column(name= "enroll", nullable = false)
    private boolean enroll;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @OneToMany(mappedBy = "meeting", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<MeetingSkill> meetingSkillList = new ArrayList<>();

    @OneToMany(mappedBy = "meeting", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Member> meetingMemberList = new ArrayList<>();

    @OneToMany(mappedBy = "meeting", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Likes> likesList = new ArrayList<>();

    @OneToMany(mappedBy = "meeting", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Comment> commentList = new ArrayList<>();


    @Builder
    public Meeting(int meetingId, String meetingTitle, LocalDateTime createdAt, String thumbnail, String content, String location, int maxMember, LocalDate startDate, boolean isPublic, boolean enroll, User user, Category category) {
        this.meetingId = meetingId;
        this.meetingTitle = meetingTitle;
        this.createdAt = createdAt;
        this.thumbnail = thumbnail;
        this.content = content;
        this.location = location;
        this.maxMember = maxMember;
        this.startDate = startDate;
        this.isPublic = isPublic;
        this.enroll = enroll;
        this.user = user;
        this.category = category;
    }

    public void updateIsPublic(){
        this.isPublic = false;
    }
}
