package com.project.schoolmanagment;

import com.project.schoolmanagment.entity.concretes.user.UserRole;
import com.project.schoolmanagment.entity.enums.Gender;
import com.project.schoolmanagment.entity.enums.RoleType;
import com.project.schoolmanagment.payload.request.user.UserRequest;
import com.project.schoolmanagment.repository.user.UserRoleRepository;
import com.project.schoolmanagment.service.user.UserRoleService;
import com.project.schoolmanagment.service.user.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDate;

@SpringBootApplication
public class SchoolManagmentSystemApplication implements CommandLineRunner {
	private final UserRoleService userRoleService;
	private final UserRoleRepository userRoleRepository;
	private final UserService userService;

	public SchoolManagmentSystemApplication(UserRoleService userRoleService, UserRoleRepository userRoleRepository, UserService userService) {
		this.userRoleService = userRoleService;
		this.userRoleRepository = userRoleRepository;
        this.userService = userService;
    }

	public static void main(String[] args) {
		SpringApplication.run(SchoolManagmentSystemApplication.class, args);
	}
	@Override
	public void run(String... args) throws Exception {
		if (userRoleService.getAllUserRole().isEmpty()){
			UserRole admin = new UserRole();
			admin.setRoleType(RoleType.ADMIN);
			admin.setRoleName("Admin");
			userRoleRepository.save(admin);

			UserRole dean = new UserRole();
			dean.setRoleType(RoleType.MANAGER);
			dean.setRoleName("Dean");
			userRoleRepository.save(dean);

			UserRole vicedean = new UserRole();
			vicedean.setRoleType(RoleType.ASSISTANT_MANAGER);
			vicedean.setRoleName("ViceDean");
			userRoleRepository.save(vicedean);

			UserRole student = new UserRole();
			student.setRoleType(RoleType.STUDENT);
			student.setRoleName("Student");
			userRoleRepository.save(student);

			UserRole teacher = new UserRole();
			teacher.setRoleType(RoleType.TEACHER);
			teacher.setRoleName("Teacher");
			userRoleRepository.save(teacher);
		}

		if(userService.allUser().isEmpty()){
			UserRequest userRequest =  getUserRequest();
			userService.saveUser(userRequest, "Admin");
		}


	}

	private static UserRequest getUserRequest(){
		UserRequest user = new UserRequest();
		user.setUsername("SuperAdmin");
		user.setEmail("admin@gmail.com");
		user.setSsn("111-11-1111");
		user.setPassword("München24*");
		user.setName("adminName");
		user.setSurname("adminSurname");
		user.setPhoneNumber("111-111-1111");
		user.setGender(Gender.FEMALE);
		user.setBirthDay(LocalDate.of(1980,2,2));
		user.setBirthPlace("München");
		return user;
	}
}
