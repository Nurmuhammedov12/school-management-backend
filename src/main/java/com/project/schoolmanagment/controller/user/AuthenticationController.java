package com.project.schoolmanagment.controller.user;

import com.project.schoolmanagment.payload.messages.SuccesMessages;
import com.project.schoolmanagment.payload.request.Authentication.LoginRequest;
import com.project.schoolmanagment.payload.request.Authentication.UpdatePasswordRequest;
import com.project.schoolmanagment.payload.response.authentication.AuthResponse;
import com.project.schoolmanagment.payload.response.user.UserResponse;
import com.project.schoolmanagment.service.user.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse>authenticateUser(@RequestBody @Valid LoginRequest request){
        return ResponseEntity.ok(authenticationService.authenticateUser(request));
    }

    @PatchMapping("/updatePassword")
    public ResponseEntity<String> updatePassword(@Valid @RequestBody UpdatePasswordRequest updatePasswordRequest,
                                                 HttpServletRequest httpServletRequest){
        authenticationService.updatePassword(updatePasswordRequest, httpServletRequest);
        return ResponseEntity.ok(SuccesMessages.USER_UPDATE_PASSWORD);
    }

    @GetMapping("/user")
    public ResponseEntity<UserResponse> findByUsername(HttpServletRequest httpServletRequest){
        return ResponseEntity.ok(authenticationService.findbyUsername(httpServletRequest));
    }
}
