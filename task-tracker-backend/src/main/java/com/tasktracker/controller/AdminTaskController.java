package com.tasktracker.controller;

import com.tasktracker.dto.response.DashboardResponse;
import com.tasktracker.dto.response.LogResponse;
import com.tasktracker.dto.response.TaskResponse;
import com.tasktracker.dto.response.UserResponse;
import com.tasktracker.enums.TaskStatus;
import com.tasktracker.service.AdminService;
import com.tasktracker.service.TaskService;
import com.tasktracker.utill.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/tasks")
@RequiredArgsConstructor
public class AdminTaskController {

    private final TaskService taskService;
    private final AdminService adminService;

    // =====================================================
    // View All Tasks
    // =====================================================
    @GetMapping
    public ResponseEntity<ApiResponse<Page<TaskResponse>>> getAllTasksForAdmin(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) TaskStatus status,
            @RequestParam(required = false) Long userId
    ) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        taskService.getAllTasksForAdmin(page, size, status, userId),
                        "All tasks fetched successfully"
                )
        );
    }

    // =====================================================
    // Dashboard Statistics
    // =====================================================
    @GetMapping("/dashboard")
    public ResponseEntity<ApiResponse<DashboardResponse>> dashboard() {

        return ResponseEntity.ok(
                ApiResponse.success(
                        adminService.getDashboard(),
                        "Dashboard loaded successfully"
                )
        );
    }

    // =====================================================
    // View All Users
    // =====================================================
    @GetMapping("/users")
    public ResponseEntity<ApiResponse<Page<UserResponse>>> getUsers(

            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        adminService.getUsers(page, size),
                        "Users fetched successfully"
                )
        );
    }

    // =====================================================
    // Enable User
    // =====================================================
    @PatchMapping("/users/{id}/enable")
    public ResponseEntity<ApiResponse<Void>> enableUser(
            @PathVariable Long id
    ) {

        adminService.enableUser(id);

        return ResponseEntity.ok(
                ApiResponse.success(
                        null,
                        "User enabled successfully"
                )
        );
    }

    // =====================================================
    // Disable User
    // =====================================================
    @PatchMapping("/users/{id}/disable")
    public ResponseEntity<ApiResponse<Void>> disableUser(
            @PathVariable Long id
    ) {

        adminService.disableUser(id);

        return ResponseEntity.ok(
                ApiResponse.success(
                        null,
                        "User disabled successfully"
                )
        );
    }

    // =====================================================
    // Change User Role
    // =====================================================
    @PatchMapping("/users/{id}/role")
    public ResponseEntity<ApiResponse<Void>> changeRole(

            @PathVariable Long id,
            @RequestParam String role
    ) {

        adminService.changeRole(id, role);

        return ResponseEntity.ok(
                ApiResponse.success(
                        null,
                        "Role updated successfully"
                )
        );
    }

    // =====================================================
    // View Logs
    // =====================================================
    @GetMapping("/logs")
    public ResponseEntity<ApiResponse<Page<LogResponse>>> logs(

            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        adminService.getLogs(page, size),
                        "Logs fetched successfully"
                )
        );
    }
}