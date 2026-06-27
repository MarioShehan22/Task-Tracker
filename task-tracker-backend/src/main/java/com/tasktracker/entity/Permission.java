package com.tasktracker.entity;

import com.tasktracker.enums.ModuleType;
import com.tasktracker.enums.PermissionType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "permissions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Permission extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long permissionId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true)
    @NotNull
    private PermissionType permissionName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotNull
    private ModuleType module;

    @Column(length = 200)
    private String description;

    @OneToMany(mappedBy = "permission",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    @Builder.Default
    private Set<RolePermission> rolePermissions = new HashSet<>();
}