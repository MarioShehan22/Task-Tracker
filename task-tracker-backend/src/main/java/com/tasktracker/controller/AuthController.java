package com.tasktracker.controller;

import com.tasktracker.dto.request.LoginRequest;
import com.tasktracker.dto.request.RegisterRequest;
import com.tasktracker.utill.ApiResponse;
import com.tasktracker.dto.response.AuthResponse;
import com.tasktracker.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthResponse>> register(
            @Valid @RequestBody RegisterRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(
                        authService.register(request),
                        "User registered successfully"
                ));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(
            @Valid @RequestBody LoginRequest request) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        authService.login(request),
                        "Login successful"
                )
        );
    }
}