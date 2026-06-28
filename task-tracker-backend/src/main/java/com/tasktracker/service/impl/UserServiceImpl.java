package com.tasktracker.service.impl;

import com.tasktracker.entity.User;
import com.tasktracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) {

        User user = userRepository.findByEmailWithPermissions(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException(email));

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                getAuthorities(user)
        );
    }

    private List<GrantedAuthority> getAuthorities(User user) {

        List<GrantedAuthority> authorities = new ArrayList<>();

        // ROLE
        authorities.add(new SimpleGrantedAuthority(
                "ROLE_" + user.getRole().getRoleName().name()
        ));

        // PERMISSIONS
        user.getRole().getRolePermissions().forEach(rp ->
                authorities.add(new SimpleGrantedAuthority(
                        rp.getPermission().getPermissionName().name()
                ))
        );

        return authorities;
    }
}