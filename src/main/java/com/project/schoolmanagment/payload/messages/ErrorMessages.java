package com.project.schoolmanagment.payload.messages;

public class ErrorMessages {

    private ErrorMessages(){

    }

    public static final String ROLE_NOT_FOUND = "Error: User with user role %s is already not FOUND";

    public static final String NOT_PERMITTED = "You can not use this function";
    public static final String USER_ID_NOT_FOUND = "Error: User with id %s is not exist";
    public static final String ALREADY_REGISTER_MESSAGE_USERNAME = "Error: User with username %s is already registered";
    public static final String ALREADY_REGISTER_MESSAGE_SSN = "Error: User with ssn %s is already registered";
    public static final String ALREADY_REGISTER_MESSAGE_PHONE_NUMBER = "Error: User with phone number %s is already registered";
    public static final String ALREADY_REGISTER_MESSAGE_EMAIL = "Error: User with email %s is already registered";
    public static final String PASSWORD_SHOULD_NOT_MATCHED = "Your passwords are not matched" ;


}
