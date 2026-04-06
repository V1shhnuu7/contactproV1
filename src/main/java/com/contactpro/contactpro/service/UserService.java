package com.contactpro.contactpro.service;

import com.contactpro.contactpro.exception.InvalidCredentialsException;
import org.springframework.stereotype.Service;
import com.contactpro.contactpro.repository.UserRepository;
import com.contactpro.contactpro.model.User;
import com.contactpro.contactpro.dto.UserRequest;
import com.contactpro.contactpro.dto.UserResponse;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.List;
import java.util.stream.Collectors;
import com.contactpro.contactpro.dto.LoginRequest;
import com.contactpro.contactpro.dto.LoginResponse;

/*
 * @Service means:
 * This class contains business logic.
 */
@Service
public class UserService {


    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /*
     * Constructor injection.
     * Spring automatically injects UserRepository.
     */
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /*
     * Create new user
     */
    public UserResponse createUser(UserRequest request) {

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        String hashedPassword = passwordEncoder.encode(request.getPassword());
        user.setPassword(hashedPassword);
        User saved = userRepository.save(user);

        return new UserResponse(
                saved.getId(),
                saved.getName(),
                saved.getEmail()
        );
    }
    public LoginResponse login(LoginRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        boolean isPasswordMatch =
                passwordEncoder.matches(request.getPassword(), user.getPassword());

        if (!isPasswordMatch) {
            throw new InvalidCredentialsException("Invalid email or password");
        }

        return new LoginResponse(
                "Login successful",
                user.getId(),
                user.getEmail()
        );
    }

    /*
     * Get all users
     */
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(user -> new UserResponse(
                        user.getId(),
                        user.getName(),
                        user.getEmail()
                ))
                .collect(Collectors.toList());
    }
}