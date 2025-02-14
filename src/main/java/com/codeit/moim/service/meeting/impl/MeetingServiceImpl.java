package com.codeit.moim.service.meeting.impl;

import com.codeit.moim.common.exception.auth.UserNotFoundException;
import com.codeit.moim.domain.*;
import com.codeit.moim.domain.enums.MemberStatus;
import com.codeit.moim.repository.*;
import com.codeit.moim.service.meeting.MeetingService;
import com.codeit.moim.web.dto.request.meeting.CreateMeetingRequest;
import com.codeit.moim.web.dto.response.meeting.CreateMeetingResponse;
import com.codeit.moim.web.dto.response.meeting.ReadTopMeetingResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MeetingServiceImpl implements MeetingService {
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final MeetingRepository meetingRepository;
    private final SkillRepository skillRepository;
    private final MeetingSkillRepository meetingSkillRepository;
    private final MemberRepository memberRepository;
    private final LikesRepository likesRepository;
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

        if( !skillList.isEmpty() ){
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

    @Override
    public List<ReadTopMeetingResponse> findTopMeetingList(int userId, String categoryTitle) {
        Category category = categoryRepository.findByTitle(categoryTitle);
        List<Meeting> meetingList = meetingRepository.findByCategory(category);

        User user = userRepository.findById(userId)
                .orElseThrow(()-> new UserNotFoundException(String.valueOf(userId)));

        List<ReadTopMeetingResponse> meetingResponseList = new ArrayList<>();

        if( !meetingList.isEmpty() ){
            List<Meeting> topMeetingList = meetingList.stream()
                    .sorted((m1, m2) -> Integer.compare(likesRepository.countByMeeting(m2), likesRepository.countByMeeting(m1)))
                    .limit(4)
                    .collect(Collectors.toList());

            for( Meeting meeting : topMeetingList ){
                System.out.println("Counting members with status APPROVED...");
                int memberCount = memberRepository.countByMeetingAndStatus(meeting, MemberStatus.APPROVED);
                System.out.println("Member count: " + memberCount);

                Boolean isLike = likesRepository.existsByUserAndMeeting(user, meeting);


                ReadTopMeetingResponse response = ReadTopMeetingResponse.fromEntity(meeting, memberCount, isLike);
                meetingResponseList.add(response);
            }
        }
        return meetingResponseList;
    }
}
