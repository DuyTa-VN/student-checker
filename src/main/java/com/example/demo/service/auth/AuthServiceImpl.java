package com.example.demo.service.auth;

import com.example.demo.dto.LoginAccount;
import com.example.demo.dto.LoginResponse;
import com.example.demo.entity.Account;
import com.example.demo.repotitory.AccountRepository;
import com.example.demo.ulti.SecurityHelper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {
    private final AccountRepository accountRepository;
    private final AuthenticationManager authenticationManager;

    @Override
    public LoginResponse login(LoginAccount loginAccount, HttpServletRequest request) {
        Account account = accountRepository.findByUsername(loginAccount.getUsername()).orElse(null);
        if (account == null) {
            return LoginResponse.builder().message("Tài khoản không đúng").code(-11111).build();
        }
        if (!new BCryptPasswordEncoder().matches(loginAccount.getPassword(), account.getPassword())) {
            return LoginResponse.builder().message("Mật khẩu không đúng").build();
        }
        // set context
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                account, loginAccount.getPassword(), account.getAuthorities());
        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        Authentication authentication = authenticationManager.authenticate(authToken);
        SecurityContext sc = SecurityContextHolder.getContext();
        sc.setAuthentication(authentication);
        HttpSession session = request.getSession(true);
        session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, sc);
        return LoginResponse.builder().message("Login Success").code(99999).build();
    }

    @Override
    public void logout() {

    }
}
