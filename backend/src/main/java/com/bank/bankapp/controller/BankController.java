package com.bank.bankapp.controller;

import com.bank.bankapp.dto.*;
import com.bank.bankapp.service.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000")
public class BankController {

    private final UserService service;

    public BankController(UserService service) {
        this.service = service;
    }

    @PostMapping("/register")
    public ApiResponse register(@RequestBody RegisterRequest request) {
        return service.register(request.username, request.password);
    }

    @PostMapping("/login")
    public ApiResponse login(@RequestBody LoginRequest request) {
        return service.login(request.username, request.password);
    }

    @GetMapping("/balance/{username}")
    public ApiResponse balance(@PathVariable String username) {
        return service.getBalance(username);
    }

    @PostMapping("/transfer")
    public ApiResponse transfer(@RequestBody TransferRequest request) {
        return service.transfer(request.from, request.to, request.amount);
    }
}