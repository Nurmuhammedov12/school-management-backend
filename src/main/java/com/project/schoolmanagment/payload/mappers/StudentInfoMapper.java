package com.project.schoolmanagment.payload.mappers;

import com.project.schoolmanagment.entity.concretes.buisnes.EducationTerm;
import com.project.schoolmanagment.entity.concretes.buisnes.Lesson;
import com.project.schoolmanagment.entity.concretes.buisnes.StudentInfo;
import com.project.schoolmanagment.entity.enums.Note;
import com.project.schoolmanagment.payload.request.buisnes.StudentInfoRequest;
import com.project.schoolmanagment.payload.request.buisnes.StudentInfoUpdateRequest;
import com.project.schoolmanagment.payload.response.businnes.StudentInfoResponse;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@Data
@RequiredArgsConstructor
public class StudentInfoMapper {

    private final UserMapper userMapper;


    public StudentInfo mapStudentInfoRequestToStudentInfo(StudentInfoRequest studentInfoRequest,
                                                          Note note, Double average){
        return StudentInfo.builder()
                .infoNote(studentInfoRequest.getInfoNote())
                .absentee(studentInfoRequest.getAbsentee())
                .midterExam(studentInfoRequest.getMidtermExam())
                .finalExam(studentInfoRequest.getFinalExam())
                .examAverage(average)
                .letterGrade(note)
                .build();
    }

    public StudentInfoResponse mapStudentInfoToStudentInfoResponse(StudentInfo studentInfo){
        return StudentInfoResponse.builder()
                .lessonName(studentInfo.getLesson().getLessonName())
                .creditScore(studentInfo.getLesson().getCreditScore())
                .isCompulsory(studentInfo.getLesson().getIsCompulsory())
                .educationTerm(studentInfo.getEducationTerm().getTerm())
                .id(studentInfo.getId())
                .absentee(studentInfo.getAbsentee())
                .midtermExam(studentInfo.getMidterExam())
                .finalExam(studentInfo.getFinalExam())
                .infoNote(studentInfo.getInfoNote())
                .note(studentInfo.getLetterGrade())
                .average(studentInfo.getExamAverage())
                .studentResponse(userMapper.mapUserToStudentResponse(studentInfo.getStudent()))
                .build();
    }

    public StudentInfo mapStudentInfoUpdateRequestToStudentInfo(
            StudentInfoUpdateRequest studentInfoUpdateRequest,
            Long studentInfoRequestId,
            Lesson lesson,
            EducationTerm educationTerm,
            Note note,
            Double average){

        return StudentInfo.builder()
                .id(studentInfoRequestId)
                .infoNote(studentInfoUpdateRequest.getInfoNote())
                .midterExam(studentInfoUpdateRequest.getMidtermExam())
                .finalExam(studentInfoUpdateRequest.getFinalExam())
                .absentee(studentInfoUpdateRequest.getAbsentee())
                .lesson(lesson)
                .educationTerm(educationTerm)
                .examAverage(average)
                .letterGrade(note)
                .build();
    }

}
