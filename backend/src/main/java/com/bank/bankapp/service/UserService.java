package com.bank.bankapp.service;

import com.bank.bankapp.dto.ApiResponse;
import com.bank.bankapp.model.User;
import com.bank.bankapp.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class UserService {

    private final UserRepository repo;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository repo, PasswordEncoder passwordEncoder) {
        this.repo = repo;
        this.passwordEncoder = passwordEncoder;
    }

    public ApiResponse register(String username, String password) {
        if (repo.findByUsername(username) != null) {
            return new ApiResponse(false, "User already exists");
        }
        String encoded = passwordEncoder.encode(password);
        repo.save(new User(username, encoded, 1000.0, "USER"));
        return new ApiResponse(true, "User registered");
    }

    public List<User> getAllUsers() {
        return repo.findAll();
    }

    public ApiResponse login(String username, String password) {
        User user = repo.findByUsername(username);
        if (user == null || !passwordEncoder.matches(password, user.getPassword())) {
            return new ApiResponse(false, "Invalid credentials");
        }
        return new ApiResponse(true, "Login successful", user.getBalance(), user.getRole());
    }

    public ApiResponse getBalance(String username) {
        User user = repo.findByUsername(username);
        if (user == null) return new ApiResponse(false, "User not found");
        return new ApiResponse(true, "Balance fetched", user.getBalance(), user.getRole());
    }

    @Transactional
    public ApiResponse transfer(String from, String to, double amount) {
        if (from.equals(to)) return new ApiResponse(false, "Cannot transfer to yourself");
        User sender = repo.findByUsername(from);
        User receiver = repo.findByUsername(to);
        if (sender == null || receiver == null || sender.getBalance() < amount) {
            return new ApiResponse(false, "Transfer failed");
        }
        sender.setBalance(sender.getBalance() - amount);
        receiver.setBalance(receiver.getBalance() + amount);
        repo.save(sender);
        repo.save(receiver);
        return new ApiResponse(true, "Transfer successful", sender.getBalance(), sender.getRole());
    }
}