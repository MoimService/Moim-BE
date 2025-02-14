package com.codeit.moim.service.meeting;

import com.codeit.moim.web.dto.request.meeting.CreateMeetingRequest;
import com.codeit.moim.web.dto.response.meeting.CreateMeetingResponse;

public interface MeetingService {
    CreateMeetingResponse saveMeeting(int userId, CreateMeetingRequest request);
}
