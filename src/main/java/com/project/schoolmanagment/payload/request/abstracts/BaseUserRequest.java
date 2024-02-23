package com.project.schoolmanagment.payload.request.abstracts;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@SuperBuilder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BaseUserRequest extends AbstractUserRequest{

    @NotNull(message = "Enter password")
    @Size(min = 4, max = 60, message = "Enter password between ${min} and ${max}.")
    private String password;

    private Boolean buildIn = false;
}
