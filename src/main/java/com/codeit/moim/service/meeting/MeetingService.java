package com.codeit.moim.service.meeting;

import com.codeit.moim.web.dto.request.meeting.CreateMeetingRequest;
import com.codeit.moim.web.dto.request.meeting.SearchMeetingRequest;
import com.codeit.moim.web.dto.response.meeting.CreateMeetingResponse;
import com.codeit.moim.web.dto.response.meeting.ReadMeetingDetailResponse;
import com.codeit.moim.web.dto.response.meeting.ReadTopMeetingResponse;
import com.codeit.moim.web.dto.response.meeting.SearchMeetingResponse;

import java.util.List;

public interface MeetingService {
    CreateMeetingResponse saveMeeting(int userId, CreateMeetingRequest request);

    List<ReadTopMeetingResponse> findTopMeetingList(int userId, String category);

    List<SearchMeetingResponse> findMeetingList(int userId, String categoryTitle, SearchMeetingRequest request);

    ReadMeetingDetailResponse findMeetingDetail(int meetingId, int userId);
}
