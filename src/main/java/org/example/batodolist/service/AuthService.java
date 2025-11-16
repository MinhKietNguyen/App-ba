package org.example.batodolist.service;

import lombok.RequiredArgsConstructor;
import org.example.batodolist.common.BadRequestException;
import org.example.batodolist.common.ErrorCode;
import org.example.batodolist.common.UserRole;
import org.example.batodolist.dto.request.LoginRequest;
import org.example.batodolist.dto.request.RegisterRequest;
import org.example.batodolist.model.User;
import org.example.batodolist.repo.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTService jwtService;

    public String register(RegisterRequest request) {
        User user = User.builder()
                .username(request.getUsername())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .role(UserRole.member)
                .build();

        userRepository.save(user);
        return "Registered!";
    }

    public String login(LoginRequest request) {
        User user = userRepository.findUserByUsername(request.getUsername())
                .orElseThrow(() -> new BadRequestException(ErrorCode.NOT_FOUND));

        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash()))
            throw new RuntimeException("Wrong password");

        return jwtService.generateToken(user);
    }
}
