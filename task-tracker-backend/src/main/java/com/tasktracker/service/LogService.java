package com.tasktracker.service;

import com.tasktracker.entity.User;
import com.tasktracker.enums.ActionType;

public interface LogService {
    void log(ActionType actionType, String description, User user);
}