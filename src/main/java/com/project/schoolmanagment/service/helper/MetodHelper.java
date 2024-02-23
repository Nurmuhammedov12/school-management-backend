package com.project.schoolmanagment.service.helper;

import com.project.schoolmanagment.entity.concretes.user.User;
import com.project.schoolmanagment.exception.BadRequest_exception;
import com.project.schoolmanagment.payload.messages.ErrorMessages;
import com.project.schoolmanagment.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MetodHelper {
    private final UserRepository userRepository;

    public void checkBuiltIn(User user){
        if(user.getBuiltIn()){
            throw new BadRequest_exception(ErrorMessages.NOT_PERMITTED);
        }
    }


}
