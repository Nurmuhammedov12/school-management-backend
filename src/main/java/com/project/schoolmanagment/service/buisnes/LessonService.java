package com.project.schoolmanagment.service.buisnes;

import com.project.schoolmanagment.repository.buisnes.LessonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LessonService {

    private final LessonRepository lessonRepository;
}
