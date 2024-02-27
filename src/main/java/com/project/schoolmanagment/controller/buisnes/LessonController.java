package com.project.schoolmanagment.controller.buisnes;

import com.project.schoolmanagment.payload.request.buisnes.LessonRequest;
import com.project.schoolmanagment.payload.response.businnes.LessonResponse;
import com.project.schoolmanagment.payload.response.businnes.ResponseMessage;
import com.project.schoolmanagment.service.buisnes.LessonService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/lesson")
@RequiredArgsConstructor
public class LessonController {

    private final LessonService lessonService;

    @PreAuthorize("hasAnyAuthority('Admin','Dean','ViceDean')")
    @PostMapping("/save")
    public ResponseMessage<LessonResponse> saveLesson(@RequestBody @Valid LessonRequest lessonRequest){
        return lessonService.saveLesson(lessonRequest);
    }

    @PreAuthorize("hasAnyAuthority('Admin','Dean','ViceDean')")
    @DeleteMapping("/delete/{id}")
    public ResponseMessage deleteLesson(@PathVariable Long id){
        return lessonService.deleteLesson(id);
    }

    @PreAuthorize("hasAnyAuthority('Admin','Dean','ViceDean')")
    @GetMapping("/getLessonByName")
    public ResponseMessage<LessonResponse> getLessonByName(@RequestParam(name = "name") String lessonName){
        return lessonService.getLessonByName(lessonName);
    }

}
