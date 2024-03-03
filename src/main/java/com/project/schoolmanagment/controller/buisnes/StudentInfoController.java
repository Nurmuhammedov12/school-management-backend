package com.project.schoolmanagment.controller.buisnes;

import com.project.schoolmanagment.payload.request.buisnes.StudentInfoRequest;
import com.project.schoolmanagment.payload.request.buisnes.StudentInfoUpdateRequest;
import com.project.schoolmanagment.payload.response.businnes.ResponseMessage;
import com.project.schoolmanagment.payload.response.businnes.StudentInfoResponse;
import com.project.schoolmanagment.service.buisnes.StudentInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/studentInfo")
@RequiredArgsConstructor
public class StudentInfoController {
    private final StudentInfoService studentInfoService;


    @PreAuthorize("hasAnyAuthority('Teacher')")
    @PostMapping("/save")
    public ResponseMessage<StudentInfoResponse> saveStudentInfo(HttpServletRequest httpServletRequest,
                                                                @RequestBody @Valid StudentInfoRequest studentInfoRequest) {
        return studentInfoService.saveStudentInfo(httpServletRequest, studentInfoRequest);
    }

    @PreAuthorize("hasAnyAuthority('Admin','Teacher')")
    @PostMapping("/update/{studentInfo}")
    public ResponseMessage<StudentInfoResponse> updateStudentInfo(
            @RequestBody @Valid StudentInfoUpdateRequest studentInfoUpdateRequest,
            @PathVariable Long studentInfo) {
        return studentInfoService.updateStudentInfo(studentInfoUpdateRequest, studentInfo);
    }

    @PreAuthorize("hasAnyAuthority('Admin','Teacher')")
    @DeleteMapping("/delete/{studentInfo}")
    public ResponseMessage delete(@PathVariable Long studentInfo) {
        return studentInfoService.deleteStudentInfo(studentInfo);
    }


    @PreAuthorize("hasAnyAuthority('Admin','Dean','ViceDean')")
    @GetMapping("/getAllStudentInfoByPage")
    public Page<StudentInfoResponse> findStudentInfoByPage(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sort", defaultValue = "absentee") String sort,
            @RequestParam(value = "type", defaultValue = "desc") String type) {
        return studentInfoService.findStudentInfoByPage(page, size, sort, type);
    }

    @PreAuthorize("hasAnyAuthority('Admin','Dean','ViceDean')")
    @GetMapping("/get/{studentInfoId}")
    public ResponseEntity<StudentInfoResponse> getStudentInfoById(@PathVariable Long studentInfoId) {
        return ResponseEntity.ok(studentInfoService.findStudentInfoById(studentInfoId));
    }

    @PreAuthorize("hasAnyAuthority('Admin','Dean','ViceDean')")
    @GetMapping("/getByStudentId/{studentId}")
    public ResponseEntity<List<StudentInfoResponse>> getByStudentId(@PathVariable Long studentId) {
        return ResponseEntity.ok(studentInfoService.getByStudentId(studentId));
    }

    @PreAuthorize("hasAnyAuthority('Teacher')")
    @GetMapping("/getAllByTeacher")
    public ResponseEntity<Page<StudentInfoResponse>> getAllByTeacher(
            HttpServletRequest httpServletRequest,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        return ResponseEntity.ok(studentInfoService.getAllByTeacher(httpServletRequest, page, size));
    }

    @PreAuthorize("hasAnyAuthority('Student')")
    @GetMapping("/getAllByStudent")
    public ResponseEntity<Page<StudentInfoResponse>> getAllByStudent(
            HttpServletRequest httpServletRequest,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        return ResponseEntity.ok(studentInfoService.getAllByStudent(httpServletRequest, page, size));

    }
}
