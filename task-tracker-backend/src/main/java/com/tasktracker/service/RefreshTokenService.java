package com.tasktracker.service;

import com.tasktracker.entity.RefreshToken;
import com.tasktracker.entity.User;

public interface RefreshTokenService {
    RefreshToken createRefreshToken(User user);

    RefreshToken verifyRefreshToken(String token);

    void revokeUserTokens(User user);
}