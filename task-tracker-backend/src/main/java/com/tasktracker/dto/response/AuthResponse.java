package com.tasktracker.dto.response;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthResponse {

    private String accessToken;

    private String tokenType;

    private Long userId;

    private String email;

    private String role;
}