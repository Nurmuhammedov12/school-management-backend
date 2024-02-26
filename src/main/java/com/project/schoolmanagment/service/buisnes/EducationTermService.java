package com.project.schoolmanagment.service.buisnes;

import com.project.schoolmanagment.repository.buisnes.EducationTermRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EducationTermService {

    private final EducationTermRepository educationTermRepository;
}
