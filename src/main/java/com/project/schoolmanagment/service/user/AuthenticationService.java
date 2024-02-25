package com.project.schoolmanagment.service.user;

import com.project.schoolmanagment.payload.mappers.UserMapper;
import com.project.schoolmanagment.payload.request.Authentication.LoginRequest;
import com.project.schoolmanagment.payload.response.authentication.AuthResponse;
import com.project.schoolmanagment.repository.user.UserRepository;
import com.project.schoolmanagment.security.jwt.JwtUtils;
import com.project.schoolmanagment.security.service.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    public AuthResponse authenticateUser(LoginRequest request) {

        String username = request.getUsername();
        String password = request.getPassword();

       // injection of Spring Security in Service layer
        Authentication authentication =
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));

        //Validated authentication info is uploaded to Security Context
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // create JWT for USer
        String token = "Bearer " + authentication;

        //get all info for user
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();


        // roles from user
        Set<String> roles = userDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());

        // user can have nur ein role we get from role collection
        String userRole = roles.stream().findFirst().get();

        AuthResponse.AuthResponseBuilder responseBuilder = AuthResponse.builder();
        responseBuilder.username(userDetails.getUsername());
        responseBuilder.token(token);
        responseBuilder.name(userDetails.getName());
        responseBuilder.ssn(userDetails.getSsn());
        responseBuilder.role(userRole);

        return responseBuilder.build();
    }
}
