package com.codeit.moim.web.dto.response.meeting;

import com.codeit.moim.domain.Meeting;
import lombok.Builder;

@Builder
public record CreateMeetingResponse (
        int meetingId
){

   public static CreateMeetingResponse fromEntity (Meeting savedMeeting){
       return CreateMeetingResponse.builder()
               .meetingId(savedMeeting.getMeetingId())
               .build();
   }
}
