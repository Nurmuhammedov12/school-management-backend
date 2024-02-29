package com.project.schoolmanagment.controller.user;


import com.project.schoolmanagment.payload.request.user.TeacherRequest;
import com.project.schoolmanagment.payload.response.businnes.ResponseMessage;
import com.project.schoolmanagment.payload.response.user.UserResponse;
import com.project.schoolmanagment.service.user.TeacherService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/teacher")
@RequiredArgsConstructor
public class TeacherController {

    private final TeacherService teacherService;

    @PreAuthorize("hasAnyAuthority('Admin')")
    @PostMapping("/save")
    public ResponseEntity<ResponseMessage<UserResponse>> saveTeacher(@RequestBody @Valid TeacherRequest request){
        return ResponseEntity.ok(teacherService.saveTeacher(request));
    }

    @PreAuthorize("hasAnyAuthority('Admin','Dean','ViceDean')")
    @GetMapping("/deleteAdvisorTeacherById/{id}")
    public ResponseMessage<UserResponse> deleteAdvissorTeacheById(Long id){
        return teacherService.deleteAdvissorTeacherById(id);
    }


    @PreAuthorize("hasAnyAuthority('Admin','Dean','ViceDean')")
    @GetMapping("/getAllAdvisorTeacher")
    public List<UserResponse> getAllAdvisorTeacher(){
        return teacherService.getAllAdvisorTeacher();
    }

    @PreAuthorize("hasAnyAuthority('Admin','Dean','ViceDean')")
    @PutMapping("/update/{userId}")
    public ResponseMessage<UserResponse>updateTeacherByManagers(
            @RequestBody @Valid TeacherRequest teacherRequest,
            @PathVariable Long userId){
        return teacherService.updateTeacherByManagers(teacherRequest,userId);
    }


}
