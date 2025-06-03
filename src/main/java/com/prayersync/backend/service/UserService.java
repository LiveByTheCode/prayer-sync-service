package com.prayersync.backend.service;

import com.prayersync.backend.dto.UserDto;
import com.prayersync.backend.dto.UserRegistrationDto;
import com.prayersync.backend.entity.Church;
import com.prayersync.backend.entity.User;
import com.prayersync.backend.repository.ChurchRepository;
import com.prayersync.backend.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ChurchRepository churchRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ModelMapper modelMapper;

    public UserDto createUser(UserRegistrationDto registrationDto) {
        if (userRepository.existsByEmail(registrationDto.getEmail())) {
            throw new RuntimeException("Email is already taken!");
        }

        User user = new User();
        user.setEmail(registrationDto.getEmail());
        user.setPassword(passwordEncoder.encode(registrationDto.getPassword()));
        user.setFirstName(registrationDto.getFirstName());
        user.setLastName(registrationDto.getLastName());
        user.setPhone(registrationDto.getPhone());
        user.setBio(registrationDto.getBio());
        user.setProfileImageUrl(registrationDto.getProfileImageUrl());

        if (registrationDto.getChurchId() != null) {
            Church church = churchRepository.findById(registrationDto.getChurchId())
                    .orElseThrow(() -> new RuntimeException("Church not found"));
            user.setChurch(church);
        }

        User savedUser = userRepository.save(user);
        return convertToDto(savedUser);
    }

    public UserDto getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return convertToDto(user);
    }

    public UserDto getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return convertToDto(user);
    }

    public List<UserDto> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<UserDto> getUsersByChurch(Long churchId) {
        List<User> users = userRepository.findByChurchId(churchId);
        return users.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<UserDto> searchUsers(String searchTerm) {
        List<User> users = userRepository.searchUsers(searchTerm);
        return users.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public UserDto updateUser(Long id, UserDto userDto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setPhone(userDto.getPhone());
        user.setBio(userDto.getBio());
        user.setProfileImageUrl(userDto.getProfileImageUrl());

        if (userDto.getChurchId() != null) {
            Church church = churchRepository.findById(userDto.getChurchId())
                    .orElseThrow(() -> new RuntimeException("Church not found"));
            user.setChurch(church);
        }

        User updatedUser = userRepository.save(user);
        return convertToDto(updatedUser);
    }

    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        user.setIsActive(false);
        userRepository.save(user);
    }

    public UserDto promoteToChurchAdmin(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        user.setIsChurchAdmin(true);
        User updatedUser = userRepository.save(user);
        return convertToDto(updatedUser);
    }

    public UserDto revokeChurchAdmin(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        user.setIsChurchAdmin(false);
        User updatedUser = userRepository.save(user);
        return convertToDto(updatedUser);
    }

    private UserDto convertToDto(User user) {
        UserDto dto = modelMapper.map(user, UserDto.class);
        if (user.getChurch() != null) {
            dto.setChurchId(user.getChurch().getId());
            dto.setChurchName(user.getChurch().getName());
        }
        return dto;
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}