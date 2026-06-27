package com.tasktracker.dto.request;

import com.tasktracker.enums.Priority;
import com.tasktracker.enums.TaskStatus;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class UpdateTaskRequest {

    @Size(min = 3, max = 100)
    private String title;

    @Size(max = 1000)
    private String description;

    private TaskStatus status;

    private Priority priority;

    @Future
    private LocalDateTime dueDate;
}