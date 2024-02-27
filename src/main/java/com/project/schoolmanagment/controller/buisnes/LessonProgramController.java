package com.project.schoolmanagment.controller.buisnes;

import com.project.schoolmanagment.payload.request.buisnes.LessonProgramRequest;
import com.project.schoolmanagment.payload.response.businnes.LessonProgramResponse;
import com.project.schoolmanagment.payload.response.businnes.ResponseMessage;
import com.project.schoolmanagment.service.buisnes.LessonProgramService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/lessonProgram")
@RequiredArgsConstructor
public class LessonProgramController {
    private final LessonProgramService lessonProgramService;

    @PreAuthorize("hasAnyAuthority('Admin','Dean','ViceDean')")
    @PostMapping("/save")
    public ResponseMessage<LessonProgramResponse> saveLessonProgram(@RequestBody @Valid LessonProgramRequest lessonProgramRequest){
        return lessonProgramService.saveLessonProgram(lessonProgramRequest);
    }
}
