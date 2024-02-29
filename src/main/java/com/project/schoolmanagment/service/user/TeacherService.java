package com.project.schoolmanagment.service.user;

import com.project.schoolmanagment.entity.concretes.buisnes.LessonProgram;
import com.project.schoolmanagment.entity.concretes.user.User;
import com.project.schoolmanagment.entity.enums.RoleType;
import com.project.schoolmanagment.payload.mappers.UserMapper;
import com.project.schoolmanagment.payload.messages.SuccesMessages;
import com.project.schoolmanagment.payload.request.buisnes.AddLessonProgramToTeacherRequest;
import com.project.schoolmanagment.payload.request.user.TeacherRequest;
import com.project.schoolmanagment.payload.response.businnes.ResponseMessage;
import com.project.schoolmanagment.payload.response.user.StudentResponse;
import com.project.schoolmanagment.payload.response.user.TeacherResponse;
import com.project.schoolmanagment.payload.response.user.UserResponse;
import com.project.schoolmanagment.repository.user.UserRepository;
import com.project.schoolmanagment.service.buisnes.LessonProgramService;
import com.project.schoolmanagment.service.helper.MetodHelper;
import com.project.schoolmanagment.service.validator.DateTimeValidator;
import com.project.schoolmanagment.service.validator.UniquePropertyValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TeacherService {

    private final UserRepository userRepository;
    private final UserRoleService userRoleService;
    private final UniquePropertyValidator uniquePropertyValidator;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final LessonProgramService lessonProgramService;
    private final MetodHelper methodHelper;
    private final DateTimeValidator dateTimeValidator;

    @Transactional // Like threads learn more about
    public ResponseMessage<UserResponse> saveTeacher(TeacherRequest request) {

        //!!!! VORSICHT
        Set<LessonProgram> lessonProgramSet =
                lessonProgramService.getLessonProgramById(request.getLessonsProgramIdList());

        uniquePropertyValidator.checkDuplicate(
                request.getUsername(),
                request.getSsn(),
                request.getPhoneNumber(),
                request.getEmail());

        User teacher = userMapper.mapUser(request);
        teacher.setIsAdvisor(request.getIsAdvisorTeacher());
        teacher.setLessonProgramList(lessonProgramSet);
        teacher.setUserRole(userRoleService.getUserRole(RoleType.TEACHER));

        User savedTeacher = userRepository.save(teacher);

        return ResponseMessage.<UserResponse>builder()
                .httpStatus(HttpStatus.CREATED)
                .message(SuccesMessages.USER_CREATE)
                .object(userMapper.mapperResponseUser(savedTeacher))
                .build();

    }

    public ResponseMessage<UserResponse> deleteAdvissorTeacherById(Long id) {
        User teacher = methodHelper.idUserExist(id);
        methodHelper.checkRole(teacher, RoleType.TEACHER);
        methodHelper.isAdvisor(teacher);

        teacher.setIsAdvisor(false);
        userRepository.save(teacher);

        List<User> allStudents = userRepository.findByAdvisorTeacherId(id);
        if (!allStudents.isEmpty()){
            allStudents.forEach(students -> students.setAdvisorTeacherId(null));
        }
        return ResponseMessage.<UserResponse>builder()
                .message(SuccesMessages.ADVISOR_TEACHER_DELETE)
                .object(userMapper.mapperResponseUser(teacher))
                .httpStatus(HttpStatus.OK)
                .build();


    }

    public List<UserResponse> getAllAdvisorTeacher() {
        return userRepository.findAllBYAdvisorTeacher()
                .stream()
                .map(userMapper::mapperResponseUser)
                .collect(Collectors.toList());
    }


    public ResponseMessage<UserResponse> updateTeacherByManagers(TeacherRequest teacherRequest,
                                                                 Long userId) {

        User teacher = methodHelper.idUserExist(userId);
        methodHelper.checkRole(teacher,RoleType.TEACHER);
        Set<LessonProgram>lessonPrograms = lessonProgramService.getLessonProgramById(teacherRequest.getLessonsProgramIdList());
        User teacherToSave = userMapper.mapUser(teacherRequest);
        //we are setting teacher custom properties
        teacherToSave.setId(teacher.getId());
        teacherToSave.setLessonProgramList(lessonPrograms);
        teacherToSave.setUserRole(userRoleService.getUserRole(RoleType.TEACHER));

        User savedTeacher = userRepository.save(teacherToSave);

        return ResponseMessage.<UserResponse>builder()
                .message(SuccesMessages.TEACHER_UPDATE)
                .object(userMapper.mapperResponseUser(savedTeacher))
                .httpStatus(HttpStatus.OK)
                .build();
    }

    public List<StudentResponse> getAllStudentByAdvisorTeacher(HttpServletRequest httpServletRequest) {
        String username = (String) httpServletRequest.getAttribute("username");
        User teacher = methodHelper.loadUserByName(username);
        methodHelper.isAdvisor(teacher);
        return userRepository.findByAdvisorTeacherId(teacher.getId())
                .stream()
                .map(userMapper::mapUserToStudentResponse)
                .collect(Collectors.toList());
    }

    public ResponseMessage<UserResponse> addLessonProgramToTeacher(AddLessonProgramToTeacherRequest addLessonProgramToTeacherRequest) {
        User teacher = methodHelper.idUserExist(addLessonProgramToTeacherRequest.getTeacherId());
        methodHelper.checkRole(teacher, RoleType.TEACHER);

        //existing ones
        Set<LessonProgram> existingLessonPrograms = teacher.getLessonProgramList();
        //requested ones
        Set<LessonProgram> requestedLessonPrograms = lessonProgramService.getLessonProgramById(addLessonProgramToTeacherRequest.getLessonProgramId());
        dateTimeValidator.checkLessonPrograms(existingLessonPrograms,requestedLessonPrograms);
        existingLessonPrograms.addAll(requestedLessonPrograms);
        teacher.setLessonProgramList(existingLessonPrograms);
        User savedTeacher = userRepository.save(teacher);

        return ResponseMessage.<UserResponse>builder()
                .message(SuccesMessages.LESSON_PROGRAM_ADD_TO_TEACHER)
                .httpStatus(HttpStatus.OK)
                .object(userMapper.mapperResponseUser(savedTeacher))
                .build();
    }
}
