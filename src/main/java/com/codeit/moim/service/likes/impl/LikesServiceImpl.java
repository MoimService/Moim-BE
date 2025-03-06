package com.codeit.moim.service.likes.impl;

import com.codeit.moim.common.exception.auth.UserNotFoundException;
import com.codeit.moim.common.exception.likes.LikeExistException;
import com.codeit.moim.common.exception.likes.LikeNotFoundException;
import com.codeit.moim.common.exception.meeting.MeetingAccessDeniedException;
import com.codeit.moim.common.exception.meeting.MeetingNotFoundException;
import com.codeit.moim.domain.Likes;
import com.codeit.moim.domain.Meeting;
import com.codeit.moim.domain.User;
import com.codeit.moim.repository.LikesRepository;
import com.codeit.moim.repository.MeetingRepository;
import com.codeit.moim.repository.UserRepository;
import com.codeit.moim.service.likes.LikesService;
import com.codeit.moim.web.dto.response.likes.CreateLikeResponse;
import com.codeit.moim.web.dto.response.likes.DeleteLikeResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LikesServiceImpl implements LikesService {
    private final MeetingRepository meetingRepository;
    private final UserRepository userRepository;
    private final LikesRepository likesRepository;
    @Override
    public CreateLikeResponse createLikes(int userId, int meetingId) {
        User user = getUser(userId);
        Meeting meeting = getMeeting(meetingId);

        if(likesRepository.existsByUserAndMeeting(user, meeting)) throw new LikeExistException("Like exists by user and meeting. UserId: " + userId + " MeetingId: "+ meetingId);
        if(meeting.getUser().getUserId() == userId) throw new MeetingAccessDeniedException("This user is meeting manager. UserId: " + userId);


        Likes likes  = Likes.toEntity(user, meeting);
        Likes createdLikes = likesRepository.save(likes);
        meeting.increaseLikesCount();
        meetingRepository.save(meeting);
        return CreateLikeResponse.fromEntity(createdLikes);
    }

    @Override
    public DeleteLikeResponse deleteLikes(int userId, int meetingId) {
        User user = getUser(userId);
        Meeting meeting = getMeeting(meetingId);

        Likes likes = likesRepository.findByUserAndMeeting(user, meeting)
                .orElseThrow(()-> new LikeNotFoundException("Like does not exist by user and meeting. UserId: "+ userId + " MeetingId: "+ meetingId));
        likesRepository.delete(likes);
        meeting.decreaseLikesCount();
        meetingRepository.save(meeting);
        return DeleteLikeResponse.fromEntity(meetingId);
    }

    private Meeting getMeeting(int meetingId){
        Meeting meeting = meetingRepository.findById(meetingId)
                .orElseThrow(()-> new MeetingNotFoundException("MeetingId: "+ meetingId));
        return meeting;
    }
    private User getUser(int userId){
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new UserNotFoundException("UserId: "+ userId));
        return user;
    }
}
