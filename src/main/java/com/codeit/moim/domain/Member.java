package com.codeit.moim.domain;

import com.codeit.moim.domain.enums.MemberStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="member_id")
    private int memberId;

    @Enumerated(EnumType.STRING)
    @Column(name= "status", nullable = false)
    private MemberStatus status;

    @Column(name= "message", nullable = false)
    private String message;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meeting_id", nullable = false)
    private Meeting meeting;

    @Builder
    public Member(int memberId, MemberStatus status, String message, User user, Meeting meeting) {
        this.memberId = memberId;
        this.status = status;
        this.message = message;
        this.user = user;
        this.meeting = meeting;
    }

    public static Member toEntity(User user, Meeting meeting, MemberStatus status, String message){
        return Member.builder()
                .user(user)
                .meeting(meeting)
                .status(status)
                .message(message)
                .build();
    }
}
