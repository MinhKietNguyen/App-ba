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
        if(checkUserExisted(request.getEmail(), request.getUsername())) {
            throw new BadRequestException(ErrorCode.USER_IS_EXISTED);
        }
        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .fullName(request.getFullName())
                .role(UserRole.member)
                .build();

        userRepository.save(user);
        return "Registered!";
    }

    public String login(LoginRequest request) {
        User user = userRepository.findUserByUsername(request.getUsername())
                .orElseThrow(() -> new BadRequestException(ErrorCode.WRONG_USER_OR_PASSWORD));

        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash()))
            throw new BadRequestException(ErrorCode.WRONG_USER_OR_PASSWORD);

        return jwtService.generateToken(user.getUsername());
    }

    private boolean checkUserExisted(String email, String username) {
        return userRepository.findUserByUsername(username).isPresent() ||
                userRepository.findUserByEmail(email).isPresent();
    }
}
