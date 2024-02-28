package com.project.schoolmanagment.controller.buisnes;

import com.project.schoolmanagment.payload.request.buisnes.LessonProgramRequest;
import com.project.schoolmanagment.payload.response.businnes.LessonProgramResponse;
import com.project.schoolmanagment.payload.response.businnes.ResponseMessage;
import com.project.schoolmanagment.service.buisnes.LessonProgramService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

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

    @PreAuthorize("hasAnyAuthority('Admin','Dean','ViceDean')")
    @GetMapping("/getAll")
    public List<LessonProgramResponse> getAllLessonProgramByList(){
        return lessonProgramService.getAllByList();
    }


    @PreAuthorize("hasAnyAuthority('Admin','Dean','ViceDean','Student','Teacher')")
    @GetMapping("/getAllUnassigned")
    public List<LessonProgramResponse>getAllUnassigned(){
        return lessonProgramService.getAllUnassigned();
    }

    @PreAuthorize("hasAnyAuthority('Admin','Dean','ViceDean','Student','Teacher')")
    @GetMapping("/getAllAssigned")
    public List<LessonProgramResponse>getAllAssigned(){
        return lessonProgramService.getAllAssigned();
    }

    @PreAuthorize("hasAnyAuthority('Admin','Dean','ViceDean')")
    @GetMapping("/getById/{id}")
    public LessonProgramResponse getById(@PathVariable Long id){
        return lessonProgramService.getLessonProgramById(id);
    }

    @PreAuthorize("hasAnyAuthority('Admin','Dean','ViceDean')")
    @DeleteMapping("/delete/{id}")
    public ResponseMessage deleteById(@PathVariable Long id){
        return lessonProgramService.deleteById(id);
    }

    @PreAuthorize("hasAnyAuthority('Admin','Dean','ViceDean','Student','Teacher')")
    @GetMapping("/findLessonProgramByPage")
    public Page<LessonProgramResponse> findLessonProgramByPage(
            @RequestParam(value = "page",defaultValue = "0") int page,
            @RequestParam(value = "size",defaultValue = "10") int size,
            @RequestParam(value = "sort",defaultValue = "day") String sort,
            @RequestParam(value = "type",defaultValue = "desc") String type){
        return lessonProgramService.findLessonProgramByPage(page,size,sort,type);
    }
}
