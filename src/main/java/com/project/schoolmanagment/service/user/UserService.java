package com.project.schoolmanagment.service.user;

import com.project.schoolmanagment.entity.concretes.user.User;
import com.project.schoolmanagment.entity.concretes.user.UserRole;
import com.project.schoolmanagment.entity.enums.RoleType;
import com.project.schoolmanagment.exception.BadRequest_exception;
import com.project.schoolmanagment.exception.NotFoundExceptions;
import com.project.schoolmanagment.payload.mappers.UserMapper;
import com.project.schoolmanagment.payload.messages.ErrorMessages;
import com.project.schoolmanagment.payload.messages.SuccesMessages;
import com.project.schoolmanagment.payload.request.user.UserRequest;
import com.project.schoolmanagment.payload.request.user.UserWithoutPasswordRequest;
import com.project.schoolmanagment.payload.response.abstracts.BaseUserResponse;
import com.project.schoolmanagment.payload.response.businnes.ResponseMessage;
import com.project.schoolmanagment.payload.response.user.UserResponse;
import com.project.schoolmanagment.repository.user.UserRepository;
import com.project.schoolmanagment.service.helper.MetodHelper;
import com.project.schoolmanagment.service.helper.PageableHelper;
import com.project.schoolmanagment.service.validator.UniquePropertyValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.management.relation.Role;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UniquePropertyValidator uniquePropertyValidator;
    private final UserMapper userMapper;
    private final UserRoleService roleService;
    private final PageableHelper pageableHelper;
    private final MetodHelper metodHelper;


    // Save User method
    public ResponseMessage<UserResponse> saveUser(UserRequest userRequest, String userRole) {
       // we need a validator
        uniquePropertyValidator.checkDuplicate(
                userRequest.getUsername(),
                userRequest.getSsn(),
                userRequest.getPhoneNumber(),
                userRequest.getEmail());

        User user = userMapper.mapUser(userRequest);
// Role giving
        if (userRole.equalsIgnoreCase(RoleType.ADMIN.getName())){
            if (Objects.equals(userRequest.getUsername(), "Admin")){
                user.setBuiltIn(true);
            }
            user.setUserRole(roleService.getUserRole(RoleType.ADMIN));
        } else if (userRole.equalsIgnoreCase("Dean")) {
            user.setUserRole(roleService.getUserRole(RoleType.MANAGER));
        } else if (userRole.equalsIgnoreCase("ViceDean")) {
            user.setUserRole(roleService.getUserRole(RoleType.ASSISTANT_MANAGER));
        }else throw new NotFoundExceptions(ErrorMessages.ROLE_NOT_FOUND);

    User savedUser = userRepository.save(user);

    return ResponseMessage.<UserResponse>builder()
            .message(SuccesMessages.USER_CREATE)
            .object(userMapper.mapperResponseUser(savedUser))
            .build();
    }

    //Pageable method
    public Page<UserResponse> getUserByPage(int page, int size, String sort, String type, String userRole) {
        Pageable pageable = pageableHelper.getPageableWithProperties(size, page, type, sort);
        return userRepository.findByUserRole(userRole, pageable).map(userMapper::mapperResponseUser);
    }

    // FindByUser Id
    public ResponseMessage<BaseUserResponse> findUserById(Long userId) {
        // need check id is given id has in database
        User user = userRepository.findById(userId).orElseThrow(
                ()-> new NotFoundExceptions(String.format(ErrorMessages.USER_ID_NOT_FOUND, userId)));

        return ResponseMessage.<BaseUserResponse>builder()
                .object(userMapper.mapperResponseUser(user))
                .message(SuccesMessages.USER_FOUND)
                .httpStatus(HttpStatus.FOUND)
                .build();
    }


    // Get User By Username
    public List<UserResponse> getUserByUserName(String userName) {

        return userRepository.getUserByNameContaining(userName)
                .stream()
                .map(userMapper::mapperResponseUser)
                .collect(Collectors.toList());

    }

    // Update User
    public ResponseEntity<String> updateUser(UserWithoutPasswordRequest userWithoutPassword, HttpServletRequest servletRequest) {
    String username = (String) servletRequest.getAttribute("username");
    User user = userRepository.findByUsername(username);

    //checkBuiltIn

        metodHelper.checkBuiltIn(user);

        // Uniqueness
        uniquePropertyValidator.checkUniqueProperties(user, userWithoutPassword);

        user.setName(userWithoutPassword.getName());
        user.setSurname(userWithoutPassword.getSurname());
        user.setUsername(userWithoutPassword.getUsername());
        user.setBirthDay(userWithoutPassword.getBirthDay());
        user.setBirthPlace(userWithoutPassword.getBirthPlace());
        user.setEmail(userWithoutPassword.getEmail());
        user.setPhoneNumber(userWithoutPassword.getPhoneNumber());
        user.setGender(userWithoutPassword.getGender());
        user.setSsn(userWithoutPassword.getSsn());

        userRepository.save(user);
        return  ResponseEntity.ok(SuccesMessages.USER_UPDATE_MESSAGE);

    }

    // Update Admin Vice Dean and Teacher
    public ResponseMessage<BaseUserResponse> updateAdminViceDean(Long userId, UserRequest userRequest) {
        //check Id exist in database
       User user = metodHelper.idUserExist(userId);
       // check user builtIn
       metodHelper.checkBuiltIn(user);
       uniquePropertyValidator.checkUniqueProperties(user, userRequest);
       User userToSave = userMapper.mapUser(userRequest);
       userToSave.setId(user.getId());
       userToSave.setUserRole(user.getUserRole());

       User savedUser = userRepository.save(userToSave);

       return ResponseMessage.<BaseUserResponse>builder()
               .message(SuccesMessages.USER_UPDATE_MESSAGE)
               .object(userMapper.mapperResponseUser(savedUser))
               .httpStatus(HttpStatus.OK)
               .build();
    }

    // Delete User
    public String deleteUser(Long userId, HttpServletRequest httpServletRequest) {
        User user = metodHelper.idUserExist(userId);

        String username = (String) httpServletRequest.getAttribute("username");

        User loggedInUser = userRepository.findByUsername(username);

        RoleType loggedInUserRole = loggedInUser.getUserRole().getRoleType();
        RoleType deletedUserRole = user.getUserRole().getRoleType();

        if (loggedInUser.getBuiltIn()){
            throw new BadRequest_exception(ErrorMessages.NOT_PERMITTED);
        }else if (loggedInUserRole == RoleType.MANAGER){
                if (!(deletedUserRole == RoleType.STUDENT || deletedUserRole == RoleType.TEACHER || deletedUserRole == RoleType.ASSISTANT_MANAGER)){
                    throw new BadRequest_exception(ErrorMessages.NOT_PERMITTED);
                }
            } else if (loggedInUserRole == RoleType.ASSISTANT_MANAGER){
            if (!(deletedUserRole == RoleType.STUDENT || deletedUserRole == RoleType.TEACHER)){
                throw new BadRequest_exception(ErrorMessages.NOT_PERMITTED);
            }
        }
        userRepository.deleteById(userId);
        return SuccesMessages.USER_DELETE;
    }

    public List<User> allUser(){
        return userRepository.findAll();
    }
}
