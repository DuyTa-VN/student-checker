package com.example.demo.service.auth;

import com.example.demo.dto.LoginAccount;
import com.example.demo.dto.LoginResponse;
import jakarta.servlet.http.HttpServletRequest;

public interface AuthService {
    public LoginResponse login(LoginAccount loginAccount, HttpServletRequest request);

    public void logout();
}
