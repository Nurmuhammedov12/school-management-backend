package com.project.schoolmanagment.payload.mappers;

import com.project.schoolmanagment.entity.concretes.user.User;
import com.project.schoolmanagment.payload.request.abstracts.BaseUserRequest;
import com.project.schoolmanagment.payload.request.user.UserRequest;
import com.project.schoolmanagment.payload.response.user.StudentResponse;
import com.project.schoolmanagment.payload.response.user.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserMapper {

    private final PasswordEncoder passwordEncoder;

    public User mapUser(BaseUserRequest user){

        return User.builder()
                .username(user.getUsername())
                .email(user.getEmail())
                .ssn(user.getSsn())
                .surname(user.getSurname())
                .name(user.getName())
                .birthDay(user.getBirthDay())
                .gender(user.getGender())
                .builtIn(user.getBuildIn())
                .birthPlace(user.getBirthPlace())
                .password(passwordEncoder.encode(user.getPassword()))
                .phoneNumber(user.getPhoneNumber())
                .isAdvisor(false)
                .build();
    }

    public UserResponse mapperResponseUser(User user){
        return UserResponse.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .name(user.getName())
                .surname(user.getSurname())
                .phoneNumber(user.getPhoneNumber())
                .gender(user.getGender())
                .birthDay(user.getBirthDay())
                .birthPlace(user.getBirthPlace())
                .ssn(user.getSsn())
                .email(user.getEmail())
                .userRole(user.getUserRole().getRoleType().name())
                .build();
    }


    public StudentResponse mapUserToStudentResponse(User user){
        return StudentResponse.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .name(user.getName())
                .surname(user.getSurname())
                .birthDay(user.getBirthDay())
                .birthPlace(user.getBirthPlace())
                .phoneNumber(user.getPhoneNumber())
                .gender(user.getGender())
                .email(user.getEmail())
                .fatherName(user.getFatherName())
                .motherName(user.getMotherName())
                .studentNumber(user.getStudentNumber())
                .isActive(user.isActive())
                .build();
    }
}
