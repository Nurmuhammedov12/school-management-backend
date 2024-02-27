package com.project.schoolmanagment.repository.buisnes;

import com.project.schoolmanagment.entity.concretes.buisnes.LessonProgram;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LessonProgramRepository extends JpaRepository<LessonProgram, Long> {
}
