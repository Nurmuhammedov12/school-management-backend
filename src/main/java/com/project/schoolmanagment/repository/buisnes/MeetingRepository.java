package com.project.schoolmanagment.repository.buisnes;

import com.project.schoolmanagment.entity.concretes.buisnes.Meet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MeetingRepository extends JpaRepository<Meet,Long> {

}
