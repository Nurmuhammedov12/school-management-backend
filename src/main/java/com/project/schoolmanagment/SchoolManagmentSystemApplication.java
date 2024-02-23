package com.project.schoolmanagment;

import com.project.schoolmanagment.entity.concretes.user.UserRole;
import com.project.schoolmanagment.entity.enums.RoleType;
import com.project.schoolmanagment.repository.user.UserRoleRepository;
import com.project.schoolmanagment.service.user.UserRoleService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SchoolManagmentSystemApplication implements CommandLineRunner {
	private final UserRoleService userRoleService;
	private final UserRoleRepository userRoleRepository;

	public SchoolManagmentSystemApplication(UserRoleService userRoleService, UserRoleRepository userRoleRepository) {
		this.userRoleService = userRoleService;
		this.userRoleRepository = userRoleRepository;
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
	}
}
