package com.tasktracker.repository;

import com.tasktracker.entity.Task;
import com.tasktracker.enums.Priority;
import com.tasktracker.enums.TaskStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

public interface TaskRepository extends JpaRepository<Task, Long> {

    // User's Tasks
    Page<Task> findByUserUserId(Long userId, Pageable pageable);

    // Status Filter
    Page<Task> findByStatus(TaskStatus status, Pageable pageable);

    // Priority Filter
    Page<Task> findByPriority(Priority priority, Pageable pageable);

    // User + Status
    Page<Task> findByUserUserIdAndStatus(
            Long userId,
            TaskStatus status,
            Pageable pageable
    );

    // Search Title
    @Query("""
            SELECT t
            FROM Task t
            WHERE LOWER(t.title)
            LIKE LOWER(CONCAT('%',:keyword,'%'))
            """)
    Page<Task> searchByTitle(
            @Param("keyword") String keyword,
            Pageable pageable
    );

    // Search Description
    @Query("""
            SELECT t
            FROM Task t
            WHERE LOWER(t.description)
            LIKE LOWER(CONCAT('%',:keyword,'%'))
            """)
    Page<Task> searchByDescription(
            @Param("keyword") String keyword,
            Pageable pageable
    );

    // Search Both
    @Query("""
            SELECT t
            FROM Task t
            WHERE LOWER(t.title)
            LIKE LOWER(CONCAT('%',:keyword,'%'))
            OR LOWER(t.description)
            LIKE LOWER(CONCAT('%',:keyword,'%'))
            """)
    Page<Task> search(
            @Param("keyword") String keyword,
            Pageable pageable
    );

    @Query("""
        SELECT t
        FROM Task t
        WHERE
        (:status IS NULL OR t.status = :status)
        AND
        (:userId IS NULL OR t.user.userId = :userId)
        """)
    Page<Task> filterTasks(
            @Param("status") TaskStatus status,
            @Param("userId") Long userId,
            Pageable pageable
    );

    // Dashboard
    long countByStatus(TaskStatus status);

    long countByPriority(Priority priority);

    long countByUserUserId(Long userId);
}