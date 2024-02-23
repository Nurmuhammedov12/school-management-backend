package com.project.schoolmanagment.service.user;

import com.project.schoolmanagment.entity.concretes.user.UserRole;
import com.project.schoolmanagment.entity.enums.RoleType;
import com.project.schoolmanagment.exception.NotFoundExceptions;
import com.project.schoolmanagment.payload.messages.ErrorMessages;
import com.project.schoolmanagment.repository.user.UserRoleRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserRoleService {
    private final UserRoleRepository roleRepository;


    public UserRole getUserRole(RoleType roleType){
        return roleRepository.findByEnumRoleEquals(roleType)
                .orElseThrow(
                        ()-> new NotFoundExceptions(ErrorMessages.ROLE_NOT_FOUND)
                );
    }

    public List<UserRole> getAllUserRole(){
        return roleRepository.findAll();
    }
}
