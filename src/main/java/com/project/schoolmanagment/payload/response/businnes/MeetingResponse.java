package com.project.schoolmanagment.payload.response.businnes;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.project.schoolmanagment.entity.concretes.user.User;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MeetingResponse {

    private Long id;
    private String description;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime stopTime;
    private Long advisorTeacherId;
    private String teacherName;
    private String teacherSsn;
    private String username;
    private List<User> students;

}
