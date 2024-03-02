package com.project.schoolmanagment.service.buisnes;

import com.project.schoolmanagment.entity.concretes.buisnes.Meet;
import com.project.schoolmanagment.entity.concretes.user.User;
import com.project.schoolmanagment.payload.mappers.MeetingMapper;
import com.project.schoolmanagment.payload.messages.SuccesMessages;
import com.project.schoolmanagment.payload.request.buisnes.MeetingRequest;
import com.project.schoolmanagment.payload.response.businnes.MeetingResponse;
import com.project.schoolmanagment.payload.response.businnes.ResponseMessage;
import com.project.schoolmanagment.repository.buisnes.MeetingRepository;
import com.project.schoolmanagment.service.helper.MeetingHelper;
import com.project.schoolmanagment.service.helper.MetodHelper;
import com.project.schoolmanagment.service.user.UserService;
import com.project.schoolmanagment.service.validator.DateTimeValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MeetingService {

    private final MeetingRepository meetingRepository;
    private final UserService userService;
    private final MetodHelper metodHelper;
    private final DateTimeValidator dateTimeValidator;
    private  final MeetingHelper meetingHelper;
    private final MeetingMapper meetingMapper;

    public ResponseMessage<MeetingResponse> saveMeeting(HttpServletRequest httpServletRequest, MeetingRequest meetingRequest) {

        String username = (String) httpServletRequest.getAttribute("username");
        User teacher = metodHelper.loadUserByName(username);
        metodHelper.isAdvisor(teacher);
        dateTimeValidator.checkTimeWithException(meetingRequest.getStartTime(), meetingRequest.getStopTime());

        // validate meeting conflicts
        meetingHelper.checkMeetingConflicts(meetingRequest.getStudentIds(),
                teacher.getId(),
                meetingRequest.getDate(),
                meetingRequest.getStartTime(),
                meetingRequest.getStopTime());

        List<User> students = metodHelper.getUserList(meetingRequest.getStudentIds());
        Meet meet = meetingMapper.mapMeetingRequestToMeet(meetingRequest);
        meet.setStudentList(students);
        meet.setAdvisoryTeacher(teacher);
        Meet savedMeet = meetingRepository.save(meet);
        return ResponseMessage.<MeetingResponse>builder()
                .message(SuccesMessages.MEET_SAVE)
                .object(meetingMapper.mapMeetToMeetingResponse(savedMeet))
                .httpStatus(HttpStatus.OK)
                .build();
    }

    public ResponseMessage<MeetingResponse> updateMeeting(MeetingRequest meetingRequest, Long meetingId, HttpServletRequest request) {
        Meet meet = meetingHelper.isMeetingExistById(meetingId);
        //validating teacher and meeting are matched
        meetingHelper.isMeetingMatchedWithTeacher(meet,request);
        dateTimeValidator.checkTimeWithException(meetingRequest.getStartTime(),meetingRequest.getStopTime());
        meetingHelper.checkMeetingConflicts(meetingRequest.getStudentIds(),
                meet.getAdvisoryTeacher().getId(),
                meetingRequest.getDate(),
                meetingRequest.getStartTime(),
                meetingRequest.getStopTime());

        List<User>students = metodHelper.getUserList(meetingRequest.getStudentIds());
        Meet meetToUpdate = meetingMapper.mapMeetingRequestToMeet(meetingRequest);
        //need to set missing parameters
        meetToUpdate.setStudentList(students);
        meetToUpdate.setAdvisoryTeacher(meet.getAdvisoryTeacher());
        meetToUpdate.setId(meetingId);
        Meet updatedMeet = meetingRepository.save(meetToUpdate);
        return ResponseMessage.<MeetingResponse>builder()
                .message(SuccesMessages.MEET_SAVE)
                .object(meetingMapper.mapMeetToMeetingResponse(updatedMeet))
                .httpStatus(HttpStatus.OK)
                .build();
    }

    public List<MeetingResponse> getAll() {
        return meetingRepository.findAll()
                .stream()
                .map(meetingMapper::mapMeetToMeetingResponse)
                .collect(Collectors.toList());
    }
}
