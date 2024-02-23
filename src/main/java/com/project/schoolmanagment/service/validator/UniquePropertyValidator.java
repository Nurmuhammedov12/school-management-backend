package com.project.schoolmanagment.service.validator;

import com.project.schoolmanagment.exception.Conflikt;
import com.project.schoolmanagment.payload.messages.ErrorMessages;
import com.project.schoolmanagment.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UniquePropertyValidator {
    private final UserRepository userRepository;


    public void checkDuplicate(String username, String ssn, String phone, String email){

        if ((userRepository.existsByUsername(username))){
            throw new Conflikt(String.format(ErrorMessages.ALREADY_REGISTER_MESSAGE_USERNAME, username));
        }
        if ((userRepository.existsBySsn(ssn))){
            throw new Conflikt(String.format(ErrorMessages.ALREADY_REGISTER_MESSAGE_SSN, ssn));
        }
        if ((userRepository.existsByPhoneNumber(phone))){
            throw new Conflikt(String.format(ErrorMessages.ALREADY_REGISTER_MESSAGE_PHONE_NUMBER, phone));
        }
        if ((userRepository.existsByEmail(email))){
            throw new Conflikt(String.format(ErrorMessages.ALREADY_REGISTER_MESSAGE_EMAIL, email));
        }
    }
}
