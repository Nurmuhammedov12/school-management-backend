package com.project.schoolmanagment.payload.mappers;

import com.project.schoolmanagment.entity.concretes.buisnes.EducationTerm;
import com.project.schoolmanagment.entity.concretes.buisnes.Lesson;
import com.project.schoolmanagment.entity.concretes.buisnes.LessonProgram;
import com.project.schoolmanagment.payload.request.buisnes.LessonProgramRequest;
import com.project.schoolmanagment.payload.response.businnes.LessonProgramResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;

@Data
@Component
public class LessonProgramMapper {

    public LessonProgram mapLessonProgramRequestToLessonProgram(LessonProgramRequest lessonProgramRequest,
                                                                Set<Lesson> lessonSet, EducationTerm educationTerm){
        return LessonProgram.builder()
                .startTime(lessonProgramRequest.getStartTime())
                .stopTime(lessonProgramRequest.getStopTime())
                .day(lessonProgramRequest.getDay())
                .lessons(lessonSet)
                .educationTerm(educationTerm)
                .build();
    }


    public LessonProgramResponse mapLessonProgramToLessonProgramResponse(LessonProgram lessonProgram){
        //TODO how to return teacher and students
        return LessonProgramResponse.builder()
                .day(lessonProgram.getDay())
                .educationTerm(lessonProgram.getEducationTerm())
                .startTime(lessonProgram.getStartTime())
                .stopTime(lessonProgram.getStopTime())
                .lessonProgramId(lessonProgram.getId())
                .lessonName(lessonProgram.getLessons())
//        .teachers(lessonProgram.getUsers().stream().filter(x->x.getUserRole().getRoleType().getName().equals("Teacher")).collect(
//            Collectors.toSet()))
                .build();
    }
}
