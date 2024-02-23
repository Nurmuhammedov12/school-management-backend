package com.project.schoolmanagment.payload.request.user;

import com.project.schoolmanagment.payload.request.abstracts.AbstractUserRequest;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class UserWithoutPasswordRequest extends AbstractUserRequest {
}
