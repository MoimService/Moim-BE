package com.codeit.moim.domain;

import com.codeit.moim.common.exception.likes.LikesCountException;
import com.codeit.moim.common.exception.member.MemberCountException;
import com.codeit.moim.web.dto.request.mymeeting.UpdateMeetingRequest;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

    @Column(name= "require_approval", nullable = false)
    private boolean requireApproval;

    @Column(name= "member_count", nullable = false)
    private int memberCount;

    @Column(name= "likes_count", nullable = false)
    private int likesCount;

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
    public Meeting(int meetingId, String meetingTitle, LocalDateTime createdAt, String thumbnail, String content, String location, int maxMember, LocalDate startDate, boolean isPublic, boolean requireApproval, int memberCount, int likesCount, User user, Category category) {
        this.meetingId = meetingId;
        this.meetingTitle = meetingTitle;
        this.createdAt = createdAt;
        this.thumbnail = thumbnail;
        this.content = content;
        this.location = location;
        this.maxMember = maxMember;
        this.startDate = startDate;
        this.isPublic = isPublic;
        this.requireApproval = requireApproval;
        this.memberCount = memberCount;
        this.likesCount = likesCount;
        this.user = user;
        this.category = category;
    }

    public void increaseMemberCount() {
        if(this.memberCount < this.maxMember) this.memberCount++;
        else throw new MemberCountException("Meeting member count is full", String.valueOf(meetingId), "member");
    }

    public void decreaseMemberCount() {
        if(this.memberCount == 0 ) throw new MemberCountException("Meeting member count is 0", String.valueOf(meetingId), "member");
        else this.memberCount--;
    }

    public void updateIsPublicToFalse(){
        this.isPublic = false;
    }
    public void updateIsPublicToTrue(){
        this.isPublic = true;
    }


    public void increaseLikesCount() {
        this.likesCount++;
    }

    public void decreaseLikesCount() {
        if(this.likesCount == 0 ) throw new LikesCountException("Likes count is 0", String.valueOf(meetingId), "likes");
        else this.likesCount--;
    }

    public void updateMeeting(UpdateMeetingRequest request, String uploadUrl, Category category){
        this.meetingTitle = (request.meetingTitle() != null && !request.meetingTitle().isEmpty()) ? request.meetingTitle() : this.getMeetingTitle();
        this.category = ( category != null ) ? category : this.category;
        this.thumbnail = (!uploadUrl.isEmpty()) ? uploadUrl : this.thumbnail;
        this.content = (request.content() != null && !request.content().isEmpty()) ? request.content() : this.getContent();
        this.location = (request.location() != null && !request.location().isEmpty()) ? request.location() : this.getLocation();
        this.maxMember = (!Objects.nonNull(request.maxMember()) && request.maxMember() >= this.maxMember) ? request.maxMember() : this.getMaxMember();
        this.startDate = (request.startDate() != null) ? request.startDate() : this.getStartDate();
        this.isPublic = (request.isPublic() != this.isPublic && !Objects.nonNull(request.isPublic())) ? request.isPublic() : this.isPublic;
        this.requireApproval = (request.requireApproval() != this.requireApproval && !Objects.nonNull(request.requireApproval())) ? request.requireApproval() : this.isRequireApproval();

    }
}
