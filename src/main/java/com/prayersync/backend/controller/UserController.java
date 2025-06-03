package com.prayersync.backend.controller;

import com.prayersync.backend.dto.UserDto;
import com.prayersync.backend.security.CustomUserDetails;
import com.prayersync.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*", maxAge = 3600)
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/me")
    public ResponseEntity<UserDto> getCurrentUser(Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        UserDto userDto = userService.getUserById(userDetails.getId());
        return ResponseEntity.ok(userDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        UserDto userDto = userService.getUserById(id);
        return ResponseEntity.ok(userDto);
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers() {
        List<UserDto> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/church/{churchId}")
    public ResponseEntity<List<UserDto>> getUsersByChurch(@PathVariable Long churchId) {
        List<UserDto> users = userService.getUsersByChurch(churchId);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/search")
    public ResponseEntity<List<UserDto>> searchUsers(@RequestParam String q) {
        List<UserDto> users = userService.searchUsers(q);
        return ResponseEntity.ok(users);
    }

    @PutMapping("/me")
    public ResponseEntity<UserDto> updateCurrentUser(@RequestBody UserDto userDto, 
                                                     Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        UserDto updatedUser = userService.updateUser(userDetails.getId(), userDto);
        return ResponseEntity.ok(updatedUser);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('CHURCH_ADMIN')")
    public ResponseEntity<UserDto> updateUser(@PathVariable Long id, @RequestBody UserDto userDto) {
        UserDto updatedUser = userService.updateUser(id, userDto);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('CHURCH_ADMIN')")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok("User deleted successfully");
    }

    @PostMapping("/{id}/promote")
    @PreAuthorize("hasRole('CHURCH_ADMIN')")
    public ResponseEntity<UserDto> promoteToChurchAdmin(@PathVariable Long id) {
        UserDto updatedUser = userService.promoteToChurchAdmin(id);
        return ResponseEntity.ok(updatedUser);
    }

    @PostMapping("/{id}/revoke")
    @PreAuthorize("hasRole('CHURCH_ADMIN')")
    public ResponseEntity<UserDto> revokeChurchAdmin(@PathVariable Long id) {
        UserDto updatedUser = userService.revokeChurchAdmin(id);
        return ResponseEntity.ok(updatedUser);
    }
}