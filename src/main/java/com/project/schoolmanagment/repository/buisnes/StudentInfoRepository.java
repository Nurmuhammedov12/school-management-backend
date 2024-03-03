package com.project.schoolmanagment.repository.buisnes;

import com.project.schoolmanagment.entity.concretes.buisnes.StudentInfo;
import com.project.schoolmanagment.payload.response.businnes.StudentInfoResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentInfoRepository extends JpaRepository<StudentInfo, Long> {

    @Query("SELECT (count (s)>0) FROM StudentInfo s WHERE s.student.id= ?1 AND s.lesson.lessonName = ?2")
    boolean giveMeDuplications(Long studentId, String lessonName);

    @Query("SELECT s FROM StudentInfo s WHERE s.student.id = ?1")
    List<StudentInfo> getStudentBy_id(Long studentId);

    @Query("SELECT s FROM StudentInfo s WHERE s.teacher.username= ?1")
    Page<StudentInfo> findAllByTeacher(String username, Pageable pageable);

    @Query("SELECT s FROM StudentInfo s WHERE s.student.username= ?1")
    Page<StudentInfo> findAllByStudent(String studentUsername, Pageable pageable);
}
