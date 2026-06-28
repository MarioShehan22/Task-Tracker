package com.tasktracker.dto.response;

import com.tasktracker.enums.ActionType;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LogResponse {
    private Long logId;
    private ActionType actionType;
    private String description;
    private Long userId;
    private String userName;
    private String email;
    private LocalDateTime createdAt;
}