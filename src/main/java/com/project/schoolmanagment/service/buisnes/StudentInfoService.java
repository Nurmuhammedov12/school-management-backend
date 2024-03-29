package com.project.schoolmanagment.service.buisnes;

import com.project.schoolmanagment.entity.concretes.buisnes.EducationTerm;
import com.project.schoolmanagment.entity.concretes.buisnes.Lesson;
import com.project.schoolmanagment.entity.concretes.buisnes.StudentInfo;
import com.project.schoolmanagment.entity.concretes.user.User;
import com.project.schoolmanagment.entity.enums.Note;
import com.project.schoolmanagment.entity.enums.RoleType;
import com.project.schoolmanagment.exception.Conflikt;
import com.project.schoolmanagment.exception.NotFoundExceptions;
import com.project.schoolmanagment.payload.mappers.StudentInfoMapper;
import com.project.schoolmanagment.payload.messages.ErrorMessages;
import com.project.schoolmanagment.payload.messages.SuccesMessages;
import com.project.schoolmanagment.payload.request.buisnes.StudentInfoRequest;
import com.project.schoolmanagment.payload.request.buisnes.StudentInfoUpdateRequest;
import com.project.schoolmanagment.payload.response.businnes.ResponseMessage;
import com.project.schoolmanagment.payload.response.businnes.StudentInfoResponse;
import com.project.schoolmanagment.repository.buisnes.StudentInfoRepository;
import com.project.schoolmanagment.service.helper.MetodHelper;
import com.project.schoolmanagment.service.helper.PageableHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudentInfoService {
    private final StudentInfoRepository studentInfoRepository;
    private final MetodHelper methodHelper;
    private final LessonsService lessonService;
    private final EducationTermService educationTermService;
    private final StudentInfoMapper studentInfoMapper;
    private final PageableHelper pageableHelper;
    @Value("${midterm.exam.impact.percentage}")
    private Double midtermExamPercentage;
    @Value("${final.exam.impact.percentage}")
    private Double finalExamPercentage;

    public ResponseMessage<StudentInfoResponse> saveStudentInfo(HttpServletRequest httpServletRequest,
                                                                StudentInfoRequest studentInfoRequest) {

        String teacherUsername = (String) httpServletRequest.getAttribute("username");

        User student = methodHelper.idUserExist(studentInfoRequest.getStudentId());
        methodHelper.checkRole(student, RoleType.STUDENT);
        User teacher = methodHelper.loadUserByName(teacherUsername);

        //validate and fetch lesson
        Lesson lesson = lessonService.isLessonExistById(studentInfoRequest.getLessonId());
        //validate and fetch education term
        EducationTerm educationTerm = educationTermService.isEducationTerm(
                studentInfoRequest.getEducationTermId());

        validateLessonDuplication(studentInfoRequest.getStudentId(), lesson.getLessonName());
        Note note = checkLetterGrade(calculateExamAverage(studentInfoRequest.getMidtermExam(),
                studentInfoRequest.getFinalExam()));
        StudentInfo studentInfo = studentInfoMapper.mapStudentInfoRequestToStudentInfo(
                studentInfoRequest,
                note,
                calculateExamAverage(studentInfoRequest.getMidtermExam(),
                        studentInfoRequest.getFinalExam())
                );

        //set missing properties
        studentInfo.setStudent(student);
        studentInfo.setEducationTerm(educationTerm);
        studentInfo.setTeacher(teacher);
        studentInfo.setLesson(lesson);
        StudentInfo savedStudentInfo = studentInfoRepository.save(studentInfo);

        return ResponseMessage.<StudentInfoResponse>builder()
                .httpStatus(HttpStatus.CREATED)
                .message(SuccesMessages.STUDENT_INFO_SAVE)
                .object(studentInfoMapper.mapStudentInfoToStudentInfoResponse(savedStudentInfo))
                .build();
    }

    //each student can have only one studentInfo entry related to a lesson
    private void validateLessonDuplication(Long studentId,String lessonName){
        if(studentInfoRepository.giveMeDuplications(studentId,lessonName)){
            throw new Conflikt(String.format(ErrorMessages.ALREADY_REGISTER_LESSON_MESSAGE,lessonName));
        }

    }

    private Double calculateExamAverage(Double midtermExam,Double finalExam){
        return (midtermExam * midtermExamPercentage) + (finalExam * finalExamPercentage);
    }

    private Note checkLetterGrade(Double average){
        if(average<50.0) {
            return Note.FF;
        } else if (average<60) {
            return Note.DD;
        } else if (average<65) {
            return Note.CC;
        } else if (average<70) {
            return  Note.CB;
        } else if (average<75) {
            return  Note.BB;
        } else if (average<80) {
            return Note.BA;
        } else {
            return Note.AA;
        }
    }

    public StudentInfo isStudentInfoExist(Long id){
        Optional<StudentInfo> studentInfoOptional = studentInfoRepository.findById(id);
        if (studentInfoOptional.isPresent()) {
            return studentInfoOptional.get();
        } else {
            throw new NotFoundExceptions(String.format(ErrorMessages.STUDENT_INFO_NOT_FOUND,id));
        }
    }

    public ResponseMessage<StudentInfoResponse> updateStudentInfo(StudentInfoUpdateRequest studentInfoUpdateRequest, Long studentInfo) {
        //validate lesson existence
        Lesson lesson = lessonService.isLessonExistById(studentInfoUpdateRequest.getLessonId());

        StudentInfo studentInfoExist = isStudentInfoExist(studentInfo);
        //validate education term existence
        EducationTerm educationTerm = educationTermService.isEducationTerm(studentInfoUpdateRequest.getEducationTermId());

        Double noteAverage = calculateExamAverage(studentInfoUpdateRequest.getMidtermExam(),
                studentInfoUpdateRequest.getFinalExam());

        Note note = checkLetterGrade(noteAverage);

        StudentInfo studentInfoToUpdate = studentInfoMapper.mapStudentInfoUpdateRequestToStudentInfo(
                studentInfoUpdateRequest,
                studentInfo,
                lesson,
                educationTerm,
                note,
                noteAverage);
        //we are not updating teacher and student
        studentInfoToUpdate.setStudent(studentInfoExist.getStudent());
        studentInfoToUpdate.setTeacher(studentInfoExist.getTeacher());
        StudentInfo updatedStudentInfo = studentInfoRepository.save(studentInfoToUpdate);
        return ResponseMessage.<StudentInfoResponse>builder()
                .message(SuccesMessages.STUDENT_INFO_UPDATE)
                .httpStatus(HttpStatus.OK)
                .object(studentInfoMapper.mapStudentInfoToStudentInfoResponse(updatedStudentInfo))
                .build();
    }

    public ResponseMessage deleteStudentInfo(Long studentInfo) {
        StudentInfo studentInfo1 = isStudentInfoExist(studentInfo);
        studentInfoRepository.delete(studentInfo1);
        return ResponseMessage.builder()
                .message(SuccesMessages.STUDENT_INFO_DELETE)
                .httpStatus(HttpStatus.OK)
                .build();
    }

    public Page<StudentInfoResponse> findStudentInfoByPage(int page, int size, String sort, String type) {
        Pageable pageable = pageableHelper.getPageableWithProperties(size,page,type,sort);
        return studentInfoRepository.findAll(pageable).map(studentInfoMapper::mapStudentInfoToStudentInfoResponse);
    }

    public StudentInfoResponse findStudentInfoById(Long studentInfoId) {
        return studentInfoMapper.mapStudentInfoToStudentInfoResponse(isStudentInfoExist(studentInfoId));
    }


    public List<StudentInfoResponse> getByStudentId(Long studentId) {
        User user = methodHelper.idUserExist(studentId);
        methodHelper.checkRole(user, RoleType.STUDENT);

        return studentInfoRepository.getStudentBy_id(studentId)
                .stream()
                .map(studentInfoMapper::mapStudentInfoToStudentInfoResponse)
                .collect(Collectors.toList());
    }

    public Page<StudentInfoResponse> getAllByTeacher(HttpServletRequest httpServletRequest, int page, int size) {
        String username = (String) httpServletRequest.getAttribute("username");
        Pageable pageable =pageableHelper.getPageableWithProperties(size,page);
        return studentInfoRepository.findAllByTeacher(username, pageable)
                .map(studentInfoMapper::mapStudentInfoToStudentInfoResponse);

    }

    public Page<StudentInfoResponse> getAllByStudent(HttpServletRequest httpServletRequest, int page, int size) {
        String studentUsername = (String) httpServletRequest.getAttribute("username");
        Pageable pageable = pageableHelper.getPageableWithProperties(size,page);
        return studentInfoRepository.findAllByStudent(studentUsername, pageable)
                .map(studentInfoMapper::mapStudentInfoToStudentInfoResponse);
    }
}
