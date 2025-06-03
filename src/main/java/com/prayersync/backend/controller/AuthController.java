package com.prayersync.backend.controller;

import com.prayersync.backend.dto.AuthResponseDto;
import com.prayersync.backend.dto.LoginRequestDto;
import com.prayersync.backend.dto.UserDto;
import com.prayersync.backend.dto.UserRegistrationDto;
import com.prayersync.backend.security.JwtTokenProvider;
import com.prayersync.backend.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequestDto loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(),
                            loginRequest.getPassword()
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = tokenProvider.generateToken(authentication);
            
            UserDto userDto = userService.getUserByEmail(loginRequest.getEmail());
            
            return ResponseEntity.ok(new AuthResponseDto(jwt, userDto));
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Authentication failed");
            errorResponse.put("message", "Invalid email or password");
            
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(errorResponse);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserRegistrationDto registrationRequest) {
        try {
            System.out.println("Registration request received for email: " + registrationRequest.getEmail());
            
            if (userService.existsByEmail(registrationRequest.getEmail())) {
                System.out.println("Email already exists: " + registrationRequest.getEmail());
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("error", "Email already exists");
                errorResponse.put("message", "An account with this email already exists");
                
                return ResponseEntity.badRequest()
                        .body(errorResponse);
            }

            System.out.println("Creating user...");
            UserDto userDto = userService.createUser(registrationRequest);
            System.out.println("User created with ID: " + userDto.getId());
            
            // Automatically login the user after registration
            System.out.println("Authenticating user...");
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            registrationRequest.getEmail(),
                            registrationRequest.getPassword()
                    )
            );

            System.out.println("Authentication successful");
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = tokenProvider.generateToken(authentication);
            System.out.println("JWT token generated");
            
            return ResponseEntity.ok(new AuthResponseDto(jwt, userDto));
        } catch (Exception e) {
            e.printStackTrace(); // Add stack trace for debugging
            
            // Return JSON error response
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Registration failed");
            errorResponse.put("message", e.getMessage());
            
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(errorResponse);
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser() {
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok("User logged out successfully");
    }
}