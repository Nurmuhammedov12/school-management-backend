package com.project.schoolmanagment.service.buisnes;

import com.project.schoolmanagment.entity.concretes.buisnes.EducationTerm;
import com.project.schoolmanagment.entity.concretes.buisnes.Lesson;
import com.project.schoolmanagment.entity.concretes.buisnes.LessonProgram;
import com.project.schoolmanagment.entity.concretes.user.User;
import com.project.schoolmanagment.entity.enums.RoleType;
import com.project.schoolmanagment.exception.BadRequest_exception;
import com.project.schoolmanagment.exception.NotFoundExceptions;
import com.project.schoolmanagment.payload.mappers.LessonProgramMapper;
import com.project.schoolmanagment.payload.messages.ErrorMessages;
import com.project.schoolmanagment.payload.messages.SuccesMessages;
import com.project.schoolmanagment.payload.request.buisnes.LessonProgramRequest;
import com.project.schoolmanagment.payload.response.businnes.LessonProgramResponse;
import com.project.schoolmanagment.payload.response.businnes.LessonResponse;
import com.project.schoolmanagment.payload.response.businnes.ResponseMessage;
import com.project.schoolmanagment.repository.buisnes.LessonProgramRepository;
import com.project.schoolmanagment.service.helper.MetodHelper;
import com.project.schoolmanagment.service.helper.PageableHelper;
import com.project.schoolmanagment.service.validator.DateTimeValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LessonProgramService {

    private final LessonProgramRepository lessonProgramRepository;
    private final EducationTermService educationTermService;
    private final LessonsService lessonsService;
    private final LessonProgramMapper lessonProgramMapper;
    private final DateTimeValidator dateTimeValidator;
    private final PageableHelper pageableHelper;
    private final MetodHelper metodHelper;

    public ResponseMessage<LessonProgramResponse> saveLessonProgram(LessonProgramRequest lessonProgramRequest) {

        // LessonssService den ID ny teilen weil braucht fur DB
        Set<Lesson> lessons = lessonsService.findLessonSetsById(lessonProgramRequest.getLessonIdList());

        // Nimmt Aus education Term and suchst ID
        EducationTerm educationTerm = educationTermService.isEducationTerm(lessonProgramRequest.getEducationTermId());

        //Date Validation
        dateTimeValidator.checkTimeWithException(lessonProgramRequest.getStartTime(), lessonProgramRequest.getStopTime());

        LessonProgram lessonProgram = lessonProgramMapper.mapLessonProgramRequestToLessonProgram(lessonProgramRequest,lessons,educationTerm);
        LessonProgram savedLessonProgram = lessonProgramRepository.save(lessonProgram);

        return ResponseMessage.<LessonProgramResponse>builder()
                .message(SuccesMessages.LESSON_PROGRAM_SAVE)
                .httpStatus(HttpStatus.CREATED)
                .object(lessonProgramMapper.mapLessonProgramToLessonProgramResponse(savedLessonProgram))
                .build();
    }

    public List<LessonProgramResponse> getAllByList() {
        return lessonProgramRepository.findAll()
                .stream()
                .map(lessonProgramMapper::mapLessonProgramToLessonProgramResponse)
                .collect(Collectors.toList());
    }

    public List<LessonProgramResponse> getAllUnassigned() {
        return lessonProgramRepository.findByUsers_IdNull()
                .stream()
                .map(lessonProgramMapper::mapLessonProgramToLessonProgramResponse)
                .collect(Collectors.toList());
    }

    public List<LessonProgramResponse> getAllAssigned() {
        return lessonProgramRepository.findByUsers_IdNotNull()
                .stream()
                .map(lessonProgramMapper::mapLessonProgramToLessonProgramResponse)
                .collect(Collectors.toList());
    }


    public LessonProgramResponse getLessonProgramById(Long id) {
        return lessonProgramMapper.mapLessonProgramToLessonProgramResponse(idexistLessonProgram(id));
    }

    public LessonProgram idexistLessonProgram(Long id){
        return lessonProgramRepository.findById(id).orElseThrow(
                ()-> new NotFoundExceptions(ErrorMessages.NOT_FOUND_LESSON_PROGRAM_MESSAGE)
        );
    }

    public ResponseMessage deleteById(Long id) {
        idexistLessonProgram(id);
        lessonProgramRepository.deleteById(id);
        return ResponseMessage.builder()
                .message(SuccesMessages.LESSON_PROGRAM_DELETE)
                .build();
    }

    public Page<LessonProgramResponse> findLessonProgramByPage(int page, int size, String sort, String type) {
        Pageable pageable = pageableHelper.getPageableWithProperties(page, size, sort, type);
        return lessonProgramRepository
                .findAll(pageable)
                .map(lessonProgramMapper::mapLessonProgramToLessonProgramResponse);
    }

    public Set<LessonProgram>getLessonProgramById(Set<Long>lessonIdSet){
        Set<LessonProgram>lessonProgramSet = lessonProgramRepository.getLessonProgramByIdList(lessonIdSet);
        if(lessonProgramSet.isEmpty()){
            throw new BadRequest_exception(ErrorMessages.NOT_FOUND_LESSON_PROGRAM_MESSAGE_WITHOUT_ID_INFO);
        }
        return lessonProgramSet;
    }

    public Set<LessonProgramResponse> getLessonProgrammByTeacherName(HttpServletRequest httpServletRequest) {
        String username = (String) httpServletRequest.getAttribute("username");
        return  lessonProgramRepository.getByTeacherName(username)
                .stream()
                .map(lessonProgramMapper::mapLessonProgramToLessonProgramResponse)
                .collect(Collectors.toSet());
    }

    public Set<LessonProgramResponse> getAllByTeacherId(Long id) {
        User teacher = metodHelper.idUserExist(id);
        metodHelper.checkRole(teacher, RoleType.TEACHER);


        return lessonProgramRepository.findByUsers_IdEquals(id)
                .stream()
                .map(lessonProgramMapper::mapLessonProgramToLessonProgramResponse)
                .collect(Collectors.toSet());

    }


    public Set<LessonProgramResponse> getAllByStudentId(Long student) {
        User user = metodHelper.idUserExist(student);
        metodHelper.checkRole(user, RoleType.STUDENT);

        return lessonProgramRepository.findByUsers_IdEquals(student)
                .stream()
                .map(lessonProgramMapper::mapLessonProgramToLessonProgramResponse)
                .collect(Collectors.toSet());
    }
}
