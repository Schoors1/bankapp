package com.bank.bankapp.service;

import com.bank.bankapp.dto.ApiResponse;
import com.bank.bankapp.model.User;
import com.bank.bankapp.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

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
            return new ApiResponse(false, "User already exists", null);
        }

        String encoded = passwordEncoder.encode(password);
        repo.save(new User(username, encoded, 1000.0));

        return new ApiResponse(true, "User registered", null);
    }

    public ApiResponse login(String username, String password) {

        User user = repo.findByUsername(username);

        if (user == null) {
            return new ApiResponse(false, "User not found", null);
        }

        if (!passwordEncoder.matches(password, user.getPassword())) {
            return new ApiResponse(false, "Wrong password", null);
        }

        return new ApiResponse(true, "Login successful", null);
    }

    public ApiResponse getBalance(String username) {

        User user = repo.findByUsername(username);

        if (user == null) {
            return new ApiResponse(false, "User not found", null);
        }

        return new ApiResponse(true, "Balance fetched", user.getBalance());
    }

    @Transactional
    public ApiResponse transfer(String from, String to, double amount) {

        if (from.equals(to)) {
            return new ApiResponse(false, "Cannot transfer to yourself", null);
        }

        if (amount <= 0) {
            return new ApiResponse(false, "Invalid amount", null);
        }

        User sender = repo.findByUsername(from);
        User receiver = repo.findByUsername(to);

        if (sender == null || receiver == null) {
            return new ApiResponse(false, "User not found", null);
        }

        if (sender.getBalance() < amount) {
            return new ApiResponse(false, "Not enough money", null);
        }

        sender.setBalance(sender.getBalance() - amount);
        receiver.setBalance(receiver.getBalance() + amount);

        repo.save(sender);
        repo.save(receiver);

        return new ApiResponse(true, "Transfer successful", sender.getBalance());
    }
}