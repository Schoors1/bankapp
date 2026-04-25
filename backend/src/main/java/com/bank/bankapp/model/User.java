package com.bank.bankapp.model;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username;

    private String password;

    private double balance;

    private String role;

    public User() {}

    public User(String username, String password, double balance, String role) {
        this.username = username;
        this.password = password;
        this.balance = balance;
        this.role = role;
    }

    public Long getId() { return id; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public double getBalance() { return balance; }
    public String getRole() { return role; }

    public void setBalance(double balance) { this.balance = balance; }
    public void setRole(String role) { this.role = role; }
}