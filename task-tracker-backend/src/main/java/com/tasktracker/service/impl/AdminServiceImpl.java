package com.tasktracker.service.impl;

import com.tasktracker.dto.response.DashboardResponse;
import com.tasktracker.dto.response.LogResponse;
import com.tasktracker.dto.response.UserResponse;
import com.tasktracker.entity.Log;
import com.tasktracker.entity.Role;
import com.tasktracker.entity.User;
import com.tasktracker.enums.RoleType;
import com.tasktracker.enums.TaskStatus;
import com.tasktracker.exception.ResourceNotFoundException;
import com.tasktracker.repository.LogRepository;
import com.tasktracker.repository.RoleRepository;
import com.tasktracker.repository.TaskRepository;
import com.tasktracker.repository.UserRepository;
import com.tasktracker.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;
    private final TaskRepository taskRepository;
    private final LogRepository logRepository;
    private final RoleRepository roleRepository;

    // ===================================================
    // Dashboard
    // ===================================================

    @Override
    public DashboardResponse getDashboard() {

        return DashboardResponse.builder()
                .totalUsers(userRepository.count())
                .activeUsers(userRepository.countByActiveTrue())
                .inactiveUsers(userRepository.countByActiveFalse())
                .totalTasks(taskRepository.count())
                .pendingTasks(taskRepository.countByStatus(TaskStatus.PENDING))
                .completedTasks(taskRepository.countByStatus(TaskStatus.COMPLETED))
                .totalLogs(logRepository.count())
                .build();
    }

    // ===================================================
    // Users
    // ===================================================

    @Override
    public Page<UserResponse> getUsers(int page, int size) {

        Pageable pageable = PageRequest.of(page, size);

        return userRepository.findAll(pageable)
                .map(this::mapUser);
    }

    // ===================================================
    // Enable User
    // ===================================================

    @Override
    public void enableUser(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        user.setActive(true);

        userRepository.save(user);
    }

    // ===================================================
    // Disable User
    // ===================================================

    @Override
    public void disableUser(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        user.setActive(false);

        userRepository.save(user);
    }

    // ===================================================
    // Change Role
    // ===================================================

    @Override
    public void changeRole(Long id, String roleName) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        Role role = roleRepository.findByRoleName(RoleType.valueOf(roleName))
                .orElseThrow(() ->
                        new ResourceNotFoundException("Role not found"));

        user.setRole(role);

        userRepository.save(user);
    }

    // ===================================================
    // Logs
    // ===================================================

    @Override
    public Page<LogResponse> getLogs(int page, int size) {

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by("createdAt").descending()
        );

        return logRepository.findAll(pageable)
                .map(this::mapLog);
    }

    // ===================================================
    // Mapper
    // ===================================================

    private UserResponse mapUser(User user) {

        return UserResponse.builder()
                .userId(user.getUserId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .role(RoleType.valueOf(user.getRole().getRoleName().name()))
                .active(user.getActive())
                .createdAt(user.getCreatedAt())
                .build();
    }

    private LogResponse mapLog(Log log) {

        return LogResponse.builder()
                .logId(log.getLogId())
                .actionType(log.getActionType())
                .description(log.getDescription())
                .userId(log.getUser().getUserId())
                .userName(log.getUser().getFirstName() + " " + log.getUser().getLastName())
                .email(log.getUser().getEmail())
                .createdAt(log.getCreatedAt())
                .build();
    }
}