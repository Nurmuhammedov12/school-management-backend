package com.project.schoolmanagment.payload.request.user;

import com.project.schoolmanagment.payload.request.abstracts.BaseUserRequest;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotNull;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class TeacherRequest extends BaseUserRequest {

    @NotNull(message = "Please select Lesson Program")
    private Set<Long> lessonsProgramIdList;

    @NotNull(message = "Please select isAdvisor Teacher")
    private Boolean isAdvisorTeacher;
}
