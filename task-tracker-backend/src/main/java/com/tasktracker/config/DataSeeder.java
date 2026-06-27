package com.tasktracker.config;

import com.tasktracker.entity.Role;
import com.tasktracker.enums.RoleType;
import com.tasktracker.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final RoleRepository roleRepository;

    @Override
    public void run(String... args) {

        if (roleRepository.count() == 0) {

            Role userRole = Role.builder()
                    .roleName(RoleType.ROLE_USER)
                    .description("Default user role")
                    .build();

            Role adminRole = Role.builder()
                    .roleName(RoleType.ROLE_ADMIN)
                    .description("Administrator role")
                    .build();

            roleRepository.save(userRole);
            roleRepository.save(adminRole);
        }
    }
}