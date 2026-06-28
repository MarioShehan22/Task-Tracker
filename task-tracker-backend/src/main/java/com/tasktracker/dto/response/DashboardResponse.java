package com.tasktracker.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DashboardResponse {
    private Long totalUsers;
    private Long activeUsers;
    private Long inactiveUsers;
    private Long totalTasks;
    private Long pendingTasks;
    private Long completedTasks;
    private Long totalLogs;
}