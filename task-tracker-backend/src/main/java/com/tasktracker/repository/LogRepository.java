package com.tasktracker.repository;

import com.tasktracker.entity.Log;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LogRepository extends JpaRepository<Log, Long> {
    Page<Log> findByUserUserId(Long userId, Pageable pageable);
}