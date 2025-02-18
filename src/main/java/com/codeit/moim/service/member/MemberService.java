package com.codeit.moim.service.member;

import com.codeit.moim.web.dto.request.member.CreateMemberRequest;
import com.codeit.moim.web.dto.response.member.CreateMemberResponse;
import com.codeit.moim.web.dto.response.member.DeleteMemberResponse;

public interface MemberService {
    CreateMemberResponse saveMember(int meetingId, int userId, CreateMemberRequest request);

}
