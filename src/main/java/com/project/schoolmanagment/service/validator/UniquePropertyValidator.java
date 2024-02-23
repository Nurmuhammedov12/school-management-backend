package com.project.schoolmanagment.service.validator;

import com.project.schoolmanagment.entity.concretes.user.User;
import com.project.schoolmanagment.exception.Conflikt;
import com.project.schoolmanagment.payload.messages.ErrorMessages;
import com.project.schoolmanagment.payload.request.abstracts.AbstractUserRequest;
import com.project.schoolmanagment.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UniquePropertyValidator {
    private final UserRepository userRepository;


    public void checkUniqueProperties(User user, AbstractUserRequest userRequest){
        String updatedUsername = "";
        String updatedSsn = "";
        String updatedPhone = "";
        String updatedEmail = "";
        boolean isUpdated = false;

        if (!user.getUsername().equalsIgnoreCase(userRequest.getUsername())){
            updatedUsername = userRequest.getUsername();
            isUpdated = true;
        }
        if (!user.getSsn().equalsIgnoreCase(userRequest.getSsn())){
            updatedSsn = userRequest.getSsn();
            isUpdated = true;
        }
        if (!user.getPhoneNumber().equalsIgnoreCase(userRequest.getPhoneNumber())){
            updatedPhone = userRequest.getPhoneNumber();
            isUpdated = true;
        }
        if (!user.getEmail().equalsIgnoreCase(userRequest.getEmail())){
            updatedEmail = userRequest.getEmail();
            isUpdated = true;
        }

        if (isUpdated){
            checkDuplicate(updatedUsername,updatedSsn,updatedPhone,updatedEmail);
        }
    }

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
