package com.project.schoolmanagment.payload.response.businnes;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.project.schoolmanagment.entity.concretes.buisnes.EducationTerm;
import com.project.schoolmanagment.entity.concretes.buisnes.Lesson;
import com.project.schoolmanagment.entity.enums.Day;
import com.project.schoolmanagment.payload.response.user.StudentResponse;
import com.project.schoolmanagment.payload.response.user.TeacherResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LessonProgramResponse {

    private Long lessonProgramId;
    private Day day;
    private LocalTime startTime;
    private LocalTime stopTime;
    private Set<Lesson> lessonName;
    private EducationTerm educationTerm;
    private Set<TeacherResponse>teachers;
    private Set<StudentResponse>student;
}
