package com.codeit.moim.service.meeting;

import com.codeit.moim.web.dto.request.meeting.CreateMeetingRequest;
import com.codeit.moim.web.dto.request.meeting.SearchMeetingRequest;
import com.codeit.moim.web.dto.response.meeting.*;
import org.springframework.data.domain.Slice;

import java.util.List;

public interface MeetingService {
    CreateMeetingResponse saveMeeting(int userId, CreateMeetingRequest request);

    List<ReadTopMeetingResponse> findTopMeetingList(String category);

    Slice<SearchMeetingResponse> findMeetingList(String categoryTitle, SearchMeetingRequest request);

    ReadMeetingDetailResponse findMeetingDetail(int meetingId);

    ReadMeetingManagerResponse findMeetingManagerDetail(int meetingId);

}
