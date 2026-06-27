package com.tasktracker.service.impl;


import com.tasktracker.entity.Log;
import com.tasktracker.entity.User;
import com.tasktracker.enums.ActionType;
import com.tasktracker.exception.ResourceNotFoundException;
import com.tasktracker.repository.LogRepository;
import com.tasktracker.repository.UserRepository;
import com.tasktracker.service.LogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class LogServiceImpl implements LogService {

    private final LogRepository logRepository;
    private final UserRepository userRepository;

    @Override
    public void log(ActionType actionType, String description, User user) {

        Log log = Log.builder()
                .actionType(actionType)
                .description(description)
                .user(user)
                .build();

        logRepository.save(log);
    }
}