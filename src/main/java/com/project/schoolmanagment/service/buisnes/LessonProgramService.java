package com.project.schoolmanagment.service.buisnes;

import com.project.schoolmanagment.payload.request.buisnes.LessonProgramRequest;
import com.project.schoolmanagment.payload.response.businnes.LessonProgramResponse;
import com.project.schoolmanagment.payload.response.businnes.ResponseMessage;
import com.project.schoolmanagment.repository.buisnes.LessonProgramRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LessonProgramService {

    private final LessonProgramRepository lessonProgramRepository;
    private final  EducationTermService educationTermService;
    private final LessonsService lessonsService;

    public ResponseMessage<LessonProgramResponse> saveLessonProgram(LessonProgramRequest lessonProgramRequest) {

    }
}
