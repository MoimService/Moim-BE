package com.codeit.moim.service.mymeeting;

import com.codeit.moim.web.dto.request.likes.ReadLikeMeetingRequest;
import com.codeit.moim.web.dto.request.mymeeting.ReadAllMeetingRequest;
import com.codeit.moim.web.dto.request.mymeeting.ReadManageMeetingRequest;
import com.codeit.moim.web.dto.request.mymeeting.ReadMemberProfileRequest;
import com.codeit.moim.web.dto.request.mymeeting.UpdateMemberStatusRequest;
import com.codeit.moim.web.dto.response.member.DeleteMemberResponse;
import com.codeit.moim.web.dto.response.mymeeting.*;
import org.springframework.data.domain.Slice;

import java.util.List;

public interface MyMeetingService {
    UpdateMemberStatusResponse updateMemberStatus(int userId, UpdateMemberStatusRequest request);

    UpdateMemberToExpelResponse expelMember(int userId, UpdateMemberStatusRequest request);

    Slice<ReadAllMeetingResponse> findAllMyMeeting(int userId, ReadAllMeetingRequest request);

    Slice<ReadManageMeetingResponse> findManageMeeting(int userId, ReadManageMeetingRequest request);

    UpdateMeetingIsPublicResponse updateIsPublic(int userId, int meetingId);

    DeleteMemberResponse cancelMemberApply(int userId, int meetingId);

    DeleteMemberResponse quitMeeting(int userId, int meetingId);

    Slice<ReadLikeMeetingResponse> findLikeMeetings(int userId, ReadLikeMeetingRequest request);

    ReadMemberProfileResponse findMemberProfile(int userId, ReadMemberProfileRequest request);
}
