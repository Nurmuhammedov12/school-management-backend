package com.project.schoolmanagment.repository.buisnes;

import com.project.schoolmanagment.entity.concretes.buisnes.LessonProgram;
import com.project.schoolmanagment.payload.response.businnes.LessonProgramResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Set;

@Repository
public interface LessonProgramRepository extends JpaRepository<LessonProgram, Long> {

    List<LessonProgram> findByUsers_IdNull();

    List<LessonProgram>findByUsers_IdNotNull();


    @Query("SELECT l FROM LessonProgram l inner JOIN l.users users WHERE users.username = ?1")
    Set<LessonProgram> getByTeacherName(String username);

    Set<LessonProgram>findByUsers_IdEquals(Long userId);

    @Query("SELECT l FROM LessonProgram l WHERE l.id IN :idSet")
    Set<LessonProgram>getLessonProgramByIdList(Set<Long> idSet);
}
