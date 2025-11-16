package org.example.batodolist.controller;

import org.example.batodolist.common.ErrorCode;
import org.example.batodolist.dto.request.UserRequest;
import org.example.batodolist.dto.request.UserUpdateRequest;
import org.example.batodolist.dto.response.UserResponse;
import org.example.batodolist.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/user")
@RestController
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/detail")
    public ResponseEntity<UserResponse> getUser(@RequestParam Long id) {
        return ResponseEntity.ok().body(userService.getUser(id));
    }

    @PostMapping("/create")
    public ResponseEntity<UserResponse> postUser(@RequestBody UserRequest userRequest) {
        return ResponseEntity.ok().body(userService.addUser(userRequest));
    }

    @PutMapping("/update")
    public ResponseEntity<UserResponse> putUser(@RequestBody UserUpdateRequest userUpdateRequest, Long id) {
        return ResponseEntity.ok().body(userService.updateUser(userUpdateRequest, id));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ErrorCode> deleteUser(@RequestParam Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok().body(ErrorCode.SUCCESS);
    }

    @GetMapping("/paging")
    public ResponseEntity<Page<UserResponse>> paging(@RequestParam int offset,@RequestParam int limit) {
        return ResponseEntity.ok().body(userService.paging(offset, limit));
    }
}
