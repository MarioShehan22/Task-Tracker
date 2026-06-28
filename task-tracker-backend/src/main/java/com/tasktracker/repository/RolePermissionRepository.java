package com.tasktracker.repository;

import com.tasktracker.entity.Permission;
import com.tasktracker.entity.Role;
import com.tasktracker.entity.RolePermission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RolePermissionRepository extends JpaRepository<RolePermission, Long> {
    List<RolePermission> findByRoleRoleId(Long roleId);
    boolean existsByRoleAndPermission(Role role, Permission permission);
}