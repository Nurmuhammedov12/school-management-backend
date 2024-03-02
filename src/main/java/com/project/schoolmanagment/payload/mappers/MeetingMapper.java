package com.project.schoolmanagment.payload.mappers;

import com.project.schoolmanagment.entity.concretes.buisnes.Meet;
import com.project.schoolmanagment.payload.request.buisnes.MeetingRequest;
import com.project.schoolmanagment.payload.response.businnes.MeetingResponse;
import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class MeetingMapper {



    public Meet mapMeetingRequestToMeet(MeetingRequest meetingRequest){
        return Meet.builder()
                .date(meetingRequest.getDate())
                .startTime(meetingRequest.getStartTime())
                .stopTime(meetingRequest.getStopTime())
                .description(meetingRequest.getDescription())
                .build();
    }

    public MeetingResponse mapMeetToMeetingResponse(Meet meet){
        return MeetingResponse.builder()
                .id(meet.getId())
                .date(meet.getDate())
                .startTime(meet.getStartTime())
                .stopTime(meet.getStopTime())
                .description(meet.getDescription())
                .advisorTeacherId(meet.getAdvisoryTeacher().getId())
                .teacherSsn(meet.getAdvisoryTeacher().getSsn())
                .teacherName(meet.getAdvisoryTeacher().getName())
                .students(meet.getStudentList())
                .build();
    }

}
