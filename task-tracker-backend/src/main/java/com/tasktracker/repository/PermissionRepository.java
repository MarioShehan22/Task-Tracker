package com.tasktracker.repository;

import com.tasktracker.entity.Permission;
import com.tasktracker.enums.PermissionType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PermissionRepository extends JpaRepository<Permission, Long> {
    Optional<Permission> findByPermissionName(PermissionType permissionName);
    boolean existsByPermissionName(PermissionType permissionName);
}