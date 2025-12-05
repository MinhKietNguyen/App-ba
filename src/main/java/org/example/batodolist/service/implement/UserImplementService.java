package org.example.batodolist.service.implement;

import org.example.batodolist.common.BadRequestException;
import org.example.batodolist.common.ErrorCode;
import org.example.batodolist.common.UserRole;
import org.example.batodolist.dto.request.UserRequest;
import org.example.batodolist.dto.request.UserUpdateRequest;
import org.example.batodolist.dto.response.UserResponse;
import org.example.batodolist.mapper.GenericMapper;
import org.example.batodolist.model.User;
import org.example.batodolist.repo.UserRepository;
import org.example.batodolist.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserImplementService implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private final GenericMapper genericMapper;

    @Autowired
    public UserImplementService(UserRepository userRepository, PasswordEncoder passwordEncoder,  GenericMapper genericMapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.genericMapper = genericMapper;
    }

    @Override
    public UserResponse getUser(Long id){
        User user = userRepository.findById(id).orElseThrow(() -> new BadRequestException(ErrorCode.NOT_FOUND));
        UserResponse userResponse = new UserResponse();
        genericMapper.copy(user, userResponse);
        return userResponse;
    }

    @Override
    public UserResponse addUser(UserRequest userRequest) {
        UserResponse userResponse = new UserResponse();
        User user = new User();
        if(checkUserExisted(userRequest.getEmail(), userRequest.getUsername())) {
            throw new BadRequestException(ErrorCode.USER_IS_EXISTED);
        }
        genericMapper.copy(userRequest, user);
        user.setPasswordHash(encodePassword(userRequest.getPassword()));
        user.setRole(UserRole.member);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(null);
        userRepository.save(user);
        genericMapper.copy(user, userResponse);
        return userResponse;
    }

    @Override
    public UserResponse updateUser(UserUpdateRequest userUpdateRequest, Long id) {
        UserResponse userResponse = new UserResponse();
        User user = userRepository.findById(id).orElseThrow(() -> new BadRequestException(ErrorCode.NOT_FOUND));
        if(userUpdateRequest.getPassword() != null) {
            if(!passwordEncoder.matches(userUpdateRequest.getPassword(), user.getPasswordHash())) {
                user.setPasswordHash(encodePassword(userUpdateRequest.getPassword()));
            }
        }
        if(checkUserExisted(userUpdateRequest.getEmail(), userUpdateRequest.getUsername())) {
            throw new BadRequestException(ErrorCode.USER_IS_EXISTED);
        }
        genericMapper.copy(userUpdateRequest, user);
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);
        genericMapper.copy(user, userResponse);
        return userResponse;
    }

    @Override
    public void deleteUser(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new BadRequestException(ErrorCode.NOT_FOUND));
        userRepository.delete(user);
    }

    @Override
    public Page<UserResponse> paging(int offset, int limit) {
        Pageable pageable = PageRequest.of(offset, limit);
        return userRepository.findAll(pageable).map(
                x -> {
                    UserResponse userResponse = new UserResponse();
                    genericMapper.copy(x, userResponse);
                    return userResponse;
                });
    }

    private String encodePassword(String password) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.encode(password);
    }

    private boolean checkUserExisted(String email, String username) {
        return userRepository.findUserByUsername(username).isPresent() ||
                userRepository.findUserByEmail(email).isPresent();
    }

}
