package com.bank.bankapp.dto;

public class ApiResponse {

    private boolean success;
    private String message;
    private Double balance;
    private String role;

    public ApiResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public ApiResponse(boolean success, String message, Double balance, String role) {
        this.success = success;
        this.message = message;
        this.balance = balance;
        this.role = role;
    }

    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public Double getBalance() { return balance; }
    public String getRole() { return role; }
}