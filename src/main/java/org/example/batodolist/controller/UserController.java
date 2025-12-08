package org.example.batodolist.controller;

import org.example.batodolist.common.ErrorCode;
import org.example.batodolist.dto.request.ProfileUpdateRequest;
import org.example.batodolist.dto.request.UserRequest;
import org.example.batodolist.dto.request.UserUpdateRequest;
import org.example.batodolist.dto.response.UserResponse;
import org.example.batodolist.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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
    public ResponseEntity<UserResponse> putUser(@RequestBody UserUpdateRequest userUpdateRequest, @RequestParam Long id) {
        return ResponseEntity.ok().body(userService.updateUser(userUpdateRequest, id));
    }

    /**
     * Endpoint for users to update their own profile
     * Gets username from JWT token (SecurityContext)
     */
    @PutMapping("/profile")
    public ResponseEntity<UserResponse> updateMyProfile(@RequestBody ProfileUpdateRequest profileUpdateRequest) {
        // Get username from SecurityContext (set by JWT filter)
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof UserDetails)) {
            return ResponseEntity.status(401).build();
        }
        
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();
        
        return ResponseEntity.ok().body(userService.updateMyProfile(profileUpdateRequest, username));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ErrorCode> deleteUser(@RequestParam Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok().body(ErrorCode.SUCCESS);
    }

    @GetMapping("/paging")
    public ResponseEntity<Page<UserResponse>> paging(@RequestParam(defaultValue = "0") int offset, @RequestParam(defaultValue = "10") int limit) {
        return ResponseEntity.ok().body(userService.paging(offset, limit));
    }
}
