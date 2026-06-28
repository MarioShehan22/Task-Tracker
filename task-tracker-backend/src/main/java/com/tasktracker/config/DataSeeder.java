//package com.tasktracker.config;
//
//import com.tasktracker.entity.Permission;
//import com.tasktracker.entity.Role;
//import com.tasktracker.entity.RolePermission;
//import com.tasktracker.enums.ModuleType;
//import com.tasktracker.enums.PermissionType;
//import com.tasktracker.enums.RoleType;
//import com.tasktracker.repository.PermissionRepository;
//import com.tasktracker.repository.RolePermissionRepository;
//import com.tasktracker.repository.RoleRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.stereotype.Component;
//
//import java.util.List;
//
//@Component
//@RequiredArgsConstructor
//public class DataSeeder implements CommandLineRunner {
//
//    private final PermissionRepository permissionRepository;
//    private final RoleRepository roleRepository;
//    private final RolePermissionRepository rolePermissionRepository;
//
//    @Override
//    public void run(String... args) {
//        seedPermissions();
//        seedRolePermissions();
//    }
//
//    // ================= PERMISSIONS =================
//    private void seedPermissions() {
//
//        if (permissionRepository.count() == 0) {
//
//            permissionRepository.saveAll(List.of(
//
//                    Permission.builder()
//                            .permissionName(PermissionType.CREATE_TASK)
//                            .module(ModuleType.TASK)
//                            .description("Create Task Permission")
//                            .build(),
//
//                    Permission.builder()
//                            .permissionName(PermissionType.UPDATE_TASK)
//                            .module(ModuleType.TASK)
//                            .description("Update Task Permission")
//                            .build(),
//
//                    Permission.builder()
//                            .permissionName(PermissionType.DELETE_TASK)
//                            .module(ModuleType.TASK)
//                            .description("Delete Task Permission")
//                            .build(),
//
//                    Permission.builder()
//                            .permissionName(PermissionType.VIEW_TASK)
//                            .module(ModuleType.TASK)
//                            .description("Read Task Permission")
//                            .build()
//            ));
//        }
//    }
//
//    // ================= ROLE-PERMISSION =================
//    private void seedRolePermissions() {
//
//        Role userRole = roleRepository.findByRoleName(RoleType.ROLE_USER)
//                .orElseThrow();
//
//        Role adminRole = roleRepository.findByRoleName(RoleType.ROLE_ADMIN)
//                .orElseThrow();
//
//        List<Permission> allPermissions = permissionRepository.findAll();
//
//        Permission readTask = allPermissions.stream()
//                .filter(p -> p.getPermissionName() == PermissionType.VIEW_TASK)
//                .findFirst().orElseThrow();
//
//        Permission createTask = allPermissions.stream()
//                .filter(p -> p.getPermissionName() == PermissionType.CREATE_TASK)
//                .findFirst().orElseThrow();
//
//        Permission updateTask = allPermissions.stream()
//                .filter(p -> p.getPermissionName() == PermissionType.UPDATE_TASK)
//                .findFirst().orElseThrow();
//
//        Permission deleteTask = allPermissions.stream()
//                .filter(p -> p.getPermissionName() == PermissionType.DELETE_TASK)
//                .findFirst().orElseThrow();
//
//        // ================= USER ROLE (LIMITED) =================
//        rolePermissionRepository.save(RolePermission.builder()
//                .role(userRole)
//                .permission(readTask)
//                .build());
//
//        // ================= ADMIN ROLE (FULL ACCESS) =================
//        rolePermissionRepository.saveAll(List.of(
//                RolePermission.builder().role(adminRole).permission(readTask).build(),
//                RolePermission.builder().role(adminRole).permission(createTask).build(),
//                RolePermission.builder().role(adminRole).permission(updateTask).build(),
//                RolePermission.builder().role(adminRole).permission(deleteTask).build()
//        ));
//    }
//}