package com.tasktracker.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DashboardResponse {

    private long totalTasks;

    private long pendingTasks;

    private long inProgressTasks;

    private long completedTasks;

    private long highPriorityTasks;
}