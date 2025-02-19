package com.codeit.moim.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="comment_id")
    private int commentId;

    @Column(name= "score", nullable = false)
    private int score;

    @Column(name= "content", nullable = false)
    private String content;

    @Column(name= "created_at", nullable = false)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meeting_id", nullable = false)
    private Meeting meeting;

    @Builder
    public Comment(int commentId, int score, String content, LocalDateTime createdAt, User user, Meeting meeting) {
        this.commentId = commentId;
        this.score = score;
        this.content = content;
        this.createdAt = createdAt;
        this.user = user;
        this.meeting = meeting;
    }
}
