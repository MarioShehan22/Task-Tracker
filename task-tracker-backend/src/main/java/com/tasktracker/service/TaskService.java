package com.tasktracker.service;

import com.tasktracker.dto.request.CreateTaskRequest;
import com.tasktracker.dto.request.UpdateTaskRequest;
import com.tasktracker.dto.response.TaskResponse;
import com.tasktracker.enums.TaskStatus;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;

public interface TaskService {

    TaskResponse createTask(CreateTaskRequest request, Authentication authentication);

    TaskResponse getTaskById(Long id);

    Page<TaskResponse> getAllTasks(int page, int size, TaskStatus status, Long userId);

    TaskResponse updateTask(Long id, UpdateTaskRequest request, Authentication authentication);

    void deleteTask(Long id, Authentication authentication);
}