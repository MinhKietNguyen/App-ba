package org.example.batodolist.service;

import org.example.batodolist.dto.request.UserRequest;
import org.example.batodolist.dto.request.UserUpdateRequest;
import org.example.batodolist.dto.response.UserResponse;
import org.springframework.data.domain.Page;

public interface UserService {
    UserResponse getUser(Long id);
    UserResponse addUser(UserRequest userRequest);
    UserResponse updateUser(UserUpdateRequest userUpdateRequest, Long id);
    void deleteUser(Long id);

    Page<UserResponse> paging(int offset, int limit);
}
