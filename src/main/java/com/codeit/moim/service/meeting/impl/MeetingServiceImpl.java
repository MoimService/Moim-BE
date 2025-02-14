package com.codeit.moim.service.meeting.impl;

import com.codeit.moim.common.exception.auth.UserNotFoundException;
import com.codeit.moim.domain.*;
import com.codeit.moim.repository.*;
import com.codeit.moim.service.meeting.MeetingService;
import com.codeit.moim.web.dto.request.meeting.CreateMeetingRequest;
import com.codeit.moim.web.dto.response.meeting.CreateMeetingResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MeetingServiceImpl implements MeetingService {
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final MeetingRepository meetingRepository;
    private final SkillRepository skillRepository;
    private final MeetingSkillRepository meetingSkillRepository;
    @Override
    public CreateMeetingResponse saveMeeting(int userId, CreateMeetingRequest request) {
        //image
        String uploadUrl = "";
        //category
        Category category = categoryRepository.findByTitle(request.categoryTitle());
        //user
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new UserNotFoundException(String.valueOf(userId)));

        Meeting meeting = request.toEntity(uploadUrl, user, category);
        Meeting savedMeeting = meetingRepository.save(meeting);

        //create meeting skills
        List<String> skillList = Arrays.asList(request.skillArray());

        if( skillList != null ){
            for( String skillTitle: skillList ){
                Skill skill = skillRepository.findByTitle(skillTitle);
                MeetingSkill meetingSkill = MeetingSkill.builder()
                        .meeting(savedMeeting)
                        .skill(skill)
                        .build();
                meetingSkillRepository.save(meetingSkill);
            }
        }

        return CreateMeetingResponse.fromEntity(savedMeeting);
    }
}
