package com.example.demo.controller;

import com.example.demo.dto.LoginAccount;
import com.example.demo.dto.LoginResponse;
import com.example.demo.service.auth.AuthService;
import com.example.demo.ulti.SecurityHelper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/public/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthService authService;

    @GetMapping("/login")
    public String loginView(Model model) {
        if (SecurityHelper.logged()) {
            return "redirect:/";
        }
        model.addAttribute("formLogin", new LoginAccount());
        return "login";
    }

    @PostMapping("/post-login")
    public String loginPost(@ModelAttribute("formLogin") LoginAccount loginAccount, Model model, HttpServletRequest request) {
        try {
            if (SecurityHelper.logged()) {
                return "redirect:/";
            }
            LoginResponse isSuccess = authService.login(loginAccount, request);
            if (isSuccess.getCode() != 99999) {
                model.addAttribute("errorMessage", isSuccess.getMessage());
                return "login";
            }
            return "redirect:/";
        } catch (Exception e) {
            log.error(e.getMessage());
            return "redirect:/error";
        }
    }
}
