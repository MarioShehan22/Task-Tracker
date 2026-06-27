package com.tasktracker.dto.response;

import com.tasktracker.enums.RoleType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserResponse {

    private Long userId;
    private String firstName;
    private String lastName;
    private String email;
    private RoleType role;
}