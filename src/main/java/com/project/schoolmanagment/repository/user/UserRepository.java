package com.project.schoolmanagment.repository.user;

import com.project.schoolmanagment.entity.concretes.user.User;
import com.project.schoolmanagment.entity.enums.RoleType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByUsername(String username);

    boolean existsBySsn(String ssn);

    boolean existsByPhoneNumber(String phoneNumber);

    boolean existsByEmail(String email);

    List<User> getUserByNameContaining(String userName);

    @Query("SELECT u FROM User u WHERE u.userRole.roleName = :roleName")
    Page<User> findByUserRole(@Param("roleName") String roleName, Pageable pageable);

    User findByUsername(String username);

    List<User> findByAdvisorTeacherId(Long id);

    @Query("SELECT u FROM User u WHERE u.isAdvisor IS true")
    List<User> findAllBYAdvisorTeacher();

    @Query("SELECT (count(u) > 0) FROM User u where u.userRole.roleType = ?1")
    boolean findStudent(RoleType roleType);

    @Query("SELECT max(u.studentNumber) FROM User u")
    int getMaxStudentNumber();
}
