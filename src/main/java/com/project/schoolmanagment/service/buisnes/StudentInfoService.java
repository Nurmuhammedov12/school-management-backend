package com.project.schoolmanagment.service.buisnes;

import com.project.schoolmanagment.repository.buisnes.StudentInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StudentInfoService {
    private final StudentInfoRepository studentInfoRepository;
}
