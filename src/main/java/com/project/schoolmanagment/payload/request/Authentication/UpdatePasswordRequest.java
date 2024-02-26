package com.project.schoolmanagment.payload.request.Authentication;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Setter
@Getter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePasswordRequest {
    @NotBlank(message = "old Password not Blank")
    private String oldPassword;

    @NotBlank(message = "New Password not Blank")
    private String newPassword;
}
