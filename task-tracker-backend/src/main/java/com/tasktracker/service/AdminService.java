package com.tasktracker.service;

import com.tasktracker.dto.response.DashboardResponse;
import com.tasktracker.dto.response.LogResponse;
import com.tasktracker.dto.response.UserResponse;
import org.springframework.data.domain.Page;

public interface AdminService {

    DashboardResponse getDashboard();

    Page<UserResponse> getUsers(int page, int size);

    void enableUser(Long id);

    void disableUser(Long id);

    void changeRole(Long id, String role);

    Page<LogResponse> getLogs(int page, int size);
}