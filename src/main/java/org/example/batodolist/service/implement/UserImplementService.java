package org.example.batodolist.service.implement;

import org.example.batodolist.common.BadRequestException;
import org.example.batodolist.common.ErrorCode;
import org.example.batodolist.common.UserRole;
import org.example.batodolist.dto.request.ProfileUpdateRequest;
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
        
        // Only update fields that are provided (not null)
        // Check for duplicate username/email only if they are being changed
        if (userUpdateRequest.getUsername() != null && !userUpdateRequest.getUsername().equals(user.getUsername())) {
            // Username is being changed, check if new username already exists
            if (checkUserExistedExcludingCurrent(userUpdateRequest.getUsername(), null, id)) {
                throw new BadRequestException(ErrorCode.USER_IS_EXISTED);
            }
            user.setUsername(userUpdateRequest.getUsername());
        }
        
        if (userUpdateRequest.getEmail() != null && !userUpdateRequest.getEmail().equals(user.getEmail())) {
            // Email is being changed, check if new email already exists
            if (checkUserExistedExcludingCurrent(null, userUpdateRequest.getEmail(), id)) {
                throw new BadRequestException(ErrorCode.USER_IS_EXISTED);
            }
            user.setEmail(userUpdateRequest.getEmail());
        }
        
        if (userUpdateRequest.getFullName() != null) {
            user.setFullName(userUpdateRequest.getFullName());
        }
        
        if (userUpdateRequest.getPassword() != null && !userUpdateRequest.getPassword().isEmpty()) {
            // Only update password if it's different from current
            if (!passwordEncoder.matches(userUpdateRequest.getPassword(), user.getPasswordHash())) {
                user.setPasswordHash(encodePassword(userUpdateRequest.getPassword()));
            }
        }
        
        if (userUpdateRequest.getRole() != null) {
            user.setRole(userUpdateRequest.getRole());
        }
        
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);
        genericMapper.copy(user, userResponse);
        return userResponse;
    }

    @Override
    public UserResponse updateMyProfile(ProfileUpdateRequest profileUpdateRequest, String username) {
        UserResponse userResponse = new UserResponse();
        // Find user by username (from JWT token)
        User user = userRepository.findUserByUsername(username)
                .orElseThrow(() -> new BadRequestException(ErrorCode.NOT_FOUND));
        
        // Only update fields that are provided (not null)
        // Check for duplicate username/email only if they are being changed
        if (profileUpdateRequest.getUsername() != null && !profileUpdateRequest.getUsername().equals(user.getUsername())) {
            // Username is being changed, check if new username already exists
            if (checkUserExistedExcludingCurrent(profileUpdateRequest.getUsername(), null, user.getId())) {
                throw new BadRequestException(ErrorCode.USER_IS_EXISTED);
            }
            user.setUsername(profileUpdateRequest.getUsername());
        }
        
        if (profileUpdateRequest.getEmail() != null && !profileUpdateRequest.getEmail().equals(user.getEmail())) {
            // Email is being changed, check if new email already exists
            if (checkUserExistedExcludingCurrent(null, profileUpdateRequest.getEmail(), user.getId())) {
                throw new BadRequestException(ErrorCode.USER_IS_EXISTED);
            }
            user.setEmail(profileUpdateRequest.getEmail());
        }
        
        if (profileUpdateRequest.getFullName() != null) {
            user.setFullName(profileUpdateRequest.getFullName());
        }
        
        if (profileUpdateRequest.getPassword() != null && !profileUpdateRequest.getPassword().isEmpty()) {
            // Only update password if it's different from current
            if (!passwordEncoder.matches(profileUpdateRequest.getPassword(), user.getPasswordHash())) {
                user.setPasswordHash(encodePassword(profileUpdateRequest.getPassword()));
            }
        }
        
        // Note: Role is NOT updated here - users cannot change their own role
        
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

    /**
     * Check if username or email exists, excluding the current user being updated
     * @param username Username to check (can be null)
     * @param email Email to check (can be null)
     * @param excludeUserId User ID to exclude from check
     * @return true if username or email exists for another user
     */
    private boolean checkUserExistedExcludingCurrent(String username, String email, Long excludeUserId) {
        if (username != null && !username.isEmpty()) {
            java.util.Optional<User> existingUserByUsername = userRepository.findUserByUsername(username);
            if (existingUserByUsername.isPresent() && !existingUserByUsername.get().getId().equals(excludeUserId)) {
                return true;
            }
        }
        
        if (email != null && !email.isEmpty()) {
            java.util.Optional<User> existingUserByEmail = userRepository.findUserByEmail(email);
            if (existingUserByEmail.isPresent() && !existingUserByEmail.get().getId().equals(excludeUserId)) {
                return true;
            }
        }
        
        return false;
    }

}
