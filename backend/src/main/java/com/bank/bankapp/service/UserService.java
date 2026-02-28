package com.bank.bankapp.service;

import com.bank.bankapp.dto.ApiResponse;
import com.bank.bankapp.model.User;
import com.bank.bankapp.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository repo;

    public UserService(UserRepository repo) {
        this.repo = repo;
    }

    public ApiResponse register(String username, String password) {

        if (repo.findByUsername(username) != null) {
            return new ApiResponse(false, "User already exists");
        }

        repo.save(new User(username, password, 1000.0));
        return new ApiResponse(true, "User registered", 1000.0);
    }

    public ApiResponse login(String username, String password) {

        User user = repo.findByUsername(username);

        if (user == null || !user.getPassword().equals(password)) {
            return new ApiResponse(false, "Invalid credentials");
        }

        return new ApiResponse(true, "Login successful", user.getBalance());
    }

    public ApiResponse getBalance(String username) {

        User user = repo.findByUsername(username);

        if (user == null) {
            return new ApiResponse(false, "User not found");
        }

        return new ApiResponse(true, "Balance fetched", user.getBalance());
    }

    public ApiResponse transfer(String from, String to, double amount) {

        User sender = repo.findByUsername(from);
        User receiver = repo.findByUsername(to);

        if (sender == null || receiver == null) {
            return new ApiResponse(false, "User not found");
        }

        if (amount <= 0) {
            return new ApiResponse(false, "Invalid amount");
        }

        if (sender.getBalance() < amount) {
            return new ApiResponse(false, "Not enough money");
        }

        sender.setBalance(sender.getBalance() - amount);
        receiver.setBalance(receiver.getBalance() + amount);

        repo.save(sender);
        repo.save(receiver);

        return new ApiResponse(true, "Transfer successful", sender.getBalance());
    }
}