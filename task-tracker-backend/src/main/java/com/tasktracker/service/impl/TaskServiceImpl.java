package com.tasktracker.service.impl;

import com.tasktracker.dto.request.CreateTaskRequest;
import com.tasktracker.dto.request.UpdateTaskRequest;
import com.tasktracker.dto.response.TaskResponse;
import com.tasktracker.entity.Task;
import com.tasktracker.entity.User;
import com.tasktracker.enums.ActionType;
import com.tasktracker.enums.TaskStatus;
import com.tasktracker.exception.ResourceNotFoundException;
import com.tasktracker.repository.TaskRepository;
import com.tasktracker.repository.UserRepository;
import com.tasktracker.service.LogService;
import com.tasktracker.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final LogService logService;

    // ================= CREATE =================
    @Override
    public TaskResponse createTask(CreateTaskRequest request, Authentication authentication) {

        User user = getCurrentUser(authentication);

        Task task = Task.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .priority(request.getPriority())
                .dueDate(request.getDueDate())
                .status(TaskStatus.PENDING)
                .user(user)
                .build();

        Task saved = taskRepository.save(task);

        logService.log(
                ActionType.CREATE_TASK,
                "Task created: " + saved.getTitle(),
                user
        );

        return mapToResponse(saved);
    }

    // ================= UPDATE =================
    @Override
    public TaskResponse updateTask(Long id, UpdateTaskRequest request, Authentication authentication) {

        User user = getCurrentUser(authentication);

        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));

        if (request.getTitle() != null) task.setTitle(request.getTitle());
        if (request.getDescription() != null) task.setDescription(request.getDescription());
        if (request.getStatus() != null) task.setStatus(request.getStatus());
        if (request.getPriority() != null) task.setPriority(request.getPriority());
        if (request.getDueDate() != null) task.setDueDate(request.getDueDate());

        Task updated = taskRepository.save(task);

        logService.log(
                ActionType.UPDATE_TASK,
                "Task updated: " + updated.getTaskId(),
                user
        );

        return mapToResponse(updated);
    }

    // ================= DELETE =================
    @Override
    public void deleteTask(Long id, Authentication authentication) {

        User user = getCurrentUser(authentication);

        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));

        taskRepository.delete(task);

        logService.log(
                ActionType.DELETE_TASK,
                "Task deleted: " + id,
                user
        );
    }

    // ================= OTHERS =================
    @Override
    public TaskResponse getTaskById(Long id) {

        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));

        return mapToResponse(task);
    }

    @Override
    public Page<TaskResponse> getAllTasks(
            int page,
            int size,
            TaskStatus status,
            Authentication authentication) {

        User user = getCurrentUser(authentication);

        Pageable pageable = PageRequest.of(page, size);

        Page<Task> tasks;

        if (status != null) {
            tasks = taskRepository.findByUserUserIdAndStatus(
                    user.getUserId(),
                    status,
                    pageable
            );
        } else {
            tasks = taskRepository.findByUserUserId(
                    user.getUserId(),
                    pageable
            );
        }

        return tasks.map(this::mapToResponse);
    }

    // ================= MAPPER =================
    private TaskResponse mapToResponse(Task task) {

        return TaskResponse.builder()
                .taskId(task.getTaskId())
                .title(task.getTitle())
                .description(task.getDescription())
                .status(task.getStatus())
                .priority(task.getPriority())
                .dueDate(task.getDueDate())
                .userId(task.getUser().getUserId())
                .userName(task.getUser().getFirstName())
                .createdAt(task.getCreatedAt())
                .updatedAt(task.getUpdatedAt())
                .build();
    }

    @Override
    public Page<TaskResponse> getAllTasksForAdmin(
            int page,
            int size,
            TaskStatus status,
            Long userId) {

        Pageable pageable = PageRequest.of(page, size);

        Page<Task> tasks;

        if (status != null && userId != null) {
            tasks = taskRepository.findByUserUserIdAndStatus(userId, status, pageable);

        } else if (status != null) {
            tasks = taskRepository.findByStatus(status, pageable);

        } else if (userId != null) {
            tasks = taskRepository.findByUserUserId(userId, pageable);

        } else {
            tasks = taskRepository.findAll(pageable);
        }

        return tasks.map(this::mapToResponse);
    }

    // ================= GET CURRENT USER =================
    private User getCurrentUser(Authentication authentication) {

        String email = authentication.getName();

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }
}