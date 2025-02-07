package com.example.demo.config;

import com.example.demo.entity.Account;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider {

    // You can inject a service to fetch user data, e.g., from a database or external service
    private final UserDetailsService accountService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();

        // Custom logic to authenticate user
        UserDetails account = accountService.loadUserByUsername(username);
        if (account == null || !account.getPassword().equals(password)) {
            throw new BadCredentialsException("Invalid username or password");
        }

        // Return an authenticated token
        return new UsernamePasswordAuthenticationToken(
                account,
                password,
                account.getAuthorities()
        );
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
