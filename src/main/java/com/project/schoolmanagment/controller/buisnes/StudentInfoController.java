package com.project.schoolmanagment.controller.buisnes;

import com.project.schoolmanagment.service.buisnes.StudentInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/studentInfo")
@RequiredArgsConstructor
public class StudentInfoController {
    private final StudentInfoService studentInfoService;

}
