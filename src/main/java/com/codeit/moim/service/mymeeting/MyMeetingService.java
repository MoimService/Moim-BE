package com.codeit.moim.service.mymeeting;

import com.codeit.moim.web.dto.request.mymeeting.UpdateMemberStatusRequest;
import com.codeit.moim.web.dto.response.mymeeting.ReadManageMeetingResponse;
import com.codeit.moim.web.dto.response.mymeeting.ReadAllMeetingResponse;
import com.codeit.moim.web.dto.response.mymeeting.UpdateMemberStatusResponse;
import com.codeit.moim.web.dto.response.mymeeting.UpdateMemberToExpelResponse;

import java.util.List;

public interface MyMeetingService {
    UpdateMemberStatusResponse updateMemberStatus(int userId, UpdateMemberStatusRequest request);

    UpdateMemberToExpelResponse expelMember(int userId, UpdateMemberStatusRequest request);

    List<ReadAllMeetingResponse> findAllMyMeeting(int userId);

    List<ReadManageMeetingResponse> findManageMeeting(int userId);
}
