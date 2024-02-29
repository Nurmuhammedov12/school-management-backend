package com.project.schoolmanagment.service.user;

import com.project.schoolmanagment.payload.mappers.UserMapper;
import com.project.schoolmanagment.repository.user.UserRepository;
import com.project.schoolmanagment.service.buisnes.LessonProgramService;
import com.project.schoolmanagment.service.helper.MetodHelper;
import com.project.schoolmanagment.service.validator.DateTimeValidator;
import com.project.schoolmanagment.service.validator.UniquePropertyValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StudentService {
    private final UserRepository userRepository;
    private final UserRoleService userRoleService;
    private final UniquePropertyValidator uniquePropertyValidator;
    private final PasswordEncoder passwordEncoder;
    private final LessonProgramService lessonProgramService;
    private final DateTimeValidator dateTimeValidator;
    private final MetodHelper methodHelper;
    private final UserMapper userMapper;
}
