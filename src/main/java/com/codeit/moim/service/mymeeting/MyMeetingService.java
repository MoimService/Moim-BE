package com.codeit.moim.service.mymeeting;

import com.codeit.moim.web.dto.request.mymeeting.UpdateMemberStatusRequest;
import com.codeit.moim.web.dto.response.mymeeting.UpdateMemberStatusResponse;

public interface MyMeetingService {
    UpdateMemberStatusResponse updateMemberStatus(int userId, UpdateMemberStatusRequest request);
}
