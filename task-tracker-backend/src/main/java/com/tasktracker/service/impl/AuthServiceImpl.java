package com.tasktracker.service.impl;

import com.tasktracker.dto.request.LoginRequest;
import com.tasktracker.dto.request.RegisterRequest;
import com.tasktracker.dto.response.AuthResponse;
import com.tasktracker.entity.RefreshToken;
import com.tasktracker.entity.Role;
import com.tasktracker.entity.User;
import com.tasktracker.enums.RoleType;
import com.tasktracker.exception.ResourceAlreadyExistsException;
import com.tasktracker.exception.ResourceNotFoundException;
import com.tasktracker.repository.RoleRepository;
import com.tasktracker.repository.UserRepository;
import com.tasktracker.security.JwtService;
import com.tasktracker.service.AdminService;
import com.tasktracker.service.AuthService;
import com.tasktracker.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;

    @Override
    public AuthResponse register(RegisterRequest request) {

        validateEmail(request.getEmail());

        Role role = roleRepository.findByRoleName(RoleType.ROLE_USER)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Default role not found"));

        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(role)
                .active(true)
                .build();

        userRepository.save(user);

        return buildAuthResponse(user);
    }

    @Override
    public AuthResponse login(LoginRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        if (!user.getActive()) {
            throw new RuntimeException("User is disabled");
        }

        return buildAuthResponse(user);
    }

    @Override
    public AuthResponse refreshToken(String token) {
        RefreshToken refreshToken = refreshTokenService.verifyRefreshToken(token);
        User user = refreshToken.getUser();
        String accessToken = jwtService.generateToken(user);
        RefreshToken newRefreshToken = refreshTokenService.createRefreshToken(user);
        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(newRefreshToken.getToken())
                .tokenType("Bearer")
                .userId(user.getUserId())
                .email(user.getEmail())
                .role(user.getRole().getRoleName().name())
                .permissions(extractPermissions(user))
                .build();
    }

    @Override
    public void logout(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        refreshTokenService.revokeUserTokens(user);
    }

    private void validateEmail(String email) {

        if (userRepository.existsByEmail(email)) {
            throw new ResourceAlreadyExistsException(
                    "Email already exists"
            );
        }
    }

    private AuthResponse buildAuthResponse(User user) {

        String accessToken =
                jwtService.generateToken(user);

        RefreshToken refreshToken =
                refreshTokenService.createRefreshToken(user);

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken.getToken())
                .tokenType("Bearer")
                .userId(user.getUserId())
                .email(user.getEmail())
                .role(user.getRole().getRoleName().name())
                .permissions(extractPermissions(user))
                .build();
    }

    private List<String> extractPermissions(User user) {
        return user.getRole()
                .getRolePermissions()
                .stream()
                .map(rp -> rp.getPermission().getPermissionName().name())
                .toList();
    }
}