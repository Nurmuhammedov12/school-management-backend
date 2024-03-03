package com.project.schoolmanagment.controller.buisnes;

import com.project.schoolmanagment.payload.request.buisnes.StudentInfoRequest;
import com.project.schoolmanagment.payload.request.buisnes.StudentInfoUpdateRequest;
import com.project.schoolmanagment.payload.response.businnes.ResponseMessage;
import com.project.schoolmanagment.payload.response.businnes.StudentInfoResponse;
import com.project.schoolmanagment.service.buisnes.StudentInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("/studentInfo")
@RequiredArgsConstructor
public class StudentInfoController {
    private final StudentInfoService studentInfoService;


    @PreAuthorize("hasAnyAuthority('Teacher')")
    @PostMapping("/save")
    public ResponseMessage<StudentInfoResponse> saveStudentInfo(HttpServletRequest httpServletRequest,
    @RequestBody @Valid StudentInfoRequest studentInfoRequest){
        return studentInfoService.saveStudentInfo(httpServletRequest, studentInfoRequest);
    }

    @PreAuthorize("hasAnyAuthority('Admin','Teacher')")
    @PostMapping("/update/{studentInfo}")
    public ResponseMessage<StudentInfoResponse>updateStudentInfo(
            @RequestBody @Valid StudentInfoUpdateRequest studentInfoUpdateRequest,
            @PathVariable Long studentInfo){
        return studentInfoService.updateStudentInfo(studentInfoUpdateRequest,studentInfo);
    }
}
