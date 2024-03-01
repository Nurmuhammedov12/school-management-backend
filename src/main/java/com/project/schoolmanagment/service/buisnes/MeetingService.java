package com.project.schoolmanagment.service.buisnes;

import com.project.schoolmanagment.entity.concretes.user.User;
import com.project.schoolmanagment.payload.request.buisnes.MeetingRequest;
import com.project.schoolmanagment.payload.response.businnes.MeetingResponse;
import com.project.schoolmanagment.payload.response.businnes.ResponseMessage;
import com.project.schoolmanagment.repository.buisnes.MeetingRepository;
import com.project.schoolmanagment.service.helper.MetodHelper;
import com.project.schoolmanagment.service.user.UserService;
import com.project.schoolmanagment.service.validator.DateTimeValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
@RequiredArgsConstructor
public class MeetingService {

    private final MeetingRepository meetingRepository;
    private final UserService userService;
    private final MetodHelper metodHelper;
    private final DateTimeValidator dateTimeValidator;

    public ResponseMessage<MeetingResponse> saveMeeting(HttpServletRequest httpServletRequest, MeetingRequest meetingRequest) {

        String username = (String) httpServletRequest.getAttribute("username");
        User teacher = metodHelper.loadUserByName(username);
        metodHelper.isAdvisor(teacher);
        dateTimeValidator.checkTimeWithException(meetingRequest.getStartTime(), meetingRequest.getStopTime());

        // validate meeting conflicts

    }
}
