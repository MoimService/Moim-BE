package com.codeit.moim.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MeetingSkill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="meeting_skill_id")
    private int meetingSkillId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meeting_id", nullable = false)
    private Meeting meeting;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "skill_id", nullable = false)
    private Skill skill;

    @Builder
    public MeetingSkill(int meetingSkillId, Meeting meeting, Skill skill) {
        this.meetingSkillId = meetingSkillId;
        this.meeting = meeting;
        this.skill = skill;
    }
}
