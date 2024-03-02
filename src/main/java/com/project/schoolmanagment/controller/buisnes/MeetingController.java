package com.project.schoolmanagment.controller.buisnes;

import com.project.schoolmanagment.payload.request.buisnes.MeetingRequest;
import com.project.schoolmanagment.payload.response.businnes.MeetingResponse;
import com.project.schoolmanagment.payload.response.businnes.ResponseMessage;
import com.project.schoolmanagment.service.buisnes.MeetingService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("/meet")
@RequiredArgsConstructor
public class MeetingController {
    private final MeetingService meetingService;


    @PreAuthorize("hasAnyAuthority('Teacher')")
    @PostMapping("/save")
    public ResponseMessage<MeetingResponse> saveMeeting(HttpServletRequest httpServletRequest,
                                                        @RequestBody @Valid MeetingRequest meetingRequest) {
        return meetingService.saveMeeting(httpServletRequest, meetingRequest);
    }

    @PreAuthorize("hasAnyAuthority('Teacher')")
    @PostMapping("/update/{meetingId}")
    public ResponseMessage<MeetingResponse>updateMeeting(
            @RequestBody @Valid MeetingRequest meetingRequest,
            @PathVariable Long meetingId,
            HttpServletRequest request){
        return meetingService.updateMeeting(meetingRequest,meetingId,request);
    }
}
