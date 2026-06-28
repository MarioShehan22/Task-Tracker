package com.tasktracker.service.impl;

import com.tasktracker.entity.RefreshToken;
import com.tasktracker.entity.User;
import com.tasktracker.enums.TokenType;
import com.tasktracker.exception.ResourceNotFoundException;
import com.tasktracker.repository.RefreshTokenRepository;
import com.tasktracker.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public RefreshToken createRefreshToken(User user) {

        refreshTokenRepository.deleteByUser(user);

        RefreshToken refreshToken = RefreshToken.builder()
                .token(UUID.randomUUID().toString())
                .tokenType(TokenType.REFRESH)
                .expiryDate(LocalDateTime.now().plusDays(7))
                .user(user)
                .expired(false)
                .revoked(false)
                .build();

        return refreshTokenRepository.save(refreshToken);
    }

    @Override
    public RefreshToken verifyRefreshToken(String token) {

        RefreshToken refreshToken =
                refreshTokenRepository.findByToken(token)
                        .orElseThrow(() ->
                                new ResourceNotFoundException("Refresh token not found"));

        if (refreshToken.getRevoked()) {
            throw new RuntimeException("Refresh token revoked");
        }

        if (refreshToken.getExpired()) {
            throw new RuntimeException("Refresh token expired");
        }

        if (refreshToken.getExpiryDate().isBefore(LocalDateTime.now())) {

            refreshToken.setExpired(true);
            refreshTokenRepository.save(refreshToken);

            throw new RuntimeException("Refresh token expired");
        }

        return refreshToken;
    }

    @Override
    public void revokeUserTokens(User user) {

        refreshTokenRepository.findByUser(user)
                .ifPresent(token -> {

                    token.setRevoked(true);
                    token.setExpired(true);

                    refreshTokenRepository.save(token);
                });
    }
}