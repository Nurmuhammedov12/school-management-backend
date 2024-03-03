package com.project.schoolmanagment.repository.buisnes;

import com.project.schoolmanagment.entity.concretes.buisnes.StudentInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentInfoRepository extends JpaRepository<StudentInfo, Long> {

    @Query("SELECT (count (s)>0) FROM StudentInfo s WHERE s.student.id= ?1 AND s.lesson.lessonName = ?2")
    boolean giveMeDuplications(Long studentId, String lessonName);
}
