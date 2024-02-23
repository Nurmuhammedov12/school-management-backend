package com.project.schoolmanagment.controller.user;

import com.project.schoolmanagment.payload.request.user.UserRequest;
import com.project.schoolmanagment.payload.request.user.UserWithoutPasswordRequest;
import com.project.schoolmanagment.payload.response.abstracts.BaseUserResponse;
import com.project.schoolmanagment.payload.response.businnes.ResponseMessage;
import com.project.schoolmanagment.payload.response.user.UserResponse;
import com.project.schoolmanagment.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/save/{userRole}")
    public ResponseEntity<ResponseMessage<UserResponse>> saveUser(
            @RequestBody @Valid UserRequest userRequest,
            @PathVariable String userRole
            ){
        return ResponseEntity.ok(userService.saveUser(userRequest,userRole));
    }
    @GetMapping("/getAllUsersByPage/{userRole}")
    public ResponseEntity<Page<UserResponse>> getUserByPage(
            @PathVariable String userRole,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sort", defaultValue = "name") String sort,
            @RequestParam(value = "type", defaultValue = "desc") String type
    ){
        Page<UserResponse> userResponsePage = userService.getUserByPage(page,size,sort,type, userRole);
        return new ResponseEntity<>(userResponsePage, HttpStatus.OK);
    }

    @GetMapping("/getUserById/{userId}")
    public ResponseMessage<BaseUserResponse> getUserById(@PathVariable("userId") Long userId){
        return userService.findUserById(userId);
    }

    @GetMapping("/getUserByName")
    public List<UserResponse> getUserByName(@RequestParam(name = "userName") String userName){
        return userService.getUserByUserName(userName);
    }

    @PatchMapping("/updateUser")
    public ResponseEntity<String> updateUser(@RequestBody @Valid
                                                              UserWithoutPasswordRequest userWithoutPassword,
                                                          HttpServletRequest servletRequest){
        return userService.updateUser(userWithoutPassword, servletRequest);
    }

    public ResponseEntity<ResponseMessage<BaseUserResponse>> updateAdminDeanViceDean(@PathVariable Long userId, @RequestBody @Valid UserRequest userRequest){
        return userService.updateAdminViceDean(userId, userRequest);
    }

}
