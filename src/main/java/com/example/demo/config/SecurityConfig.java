package com.example.demo.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(proxyTargetClass = true)
@RequiredArgsConstructor
public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity.csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .authorizeHttpRequests(authRe -> authRe
                        .requestMatchers(HttpMethod.OPTIONS, "/api/public/**", "/student/add-account-to-student/**")
                        .permitAll()
                        .requestMatchers("/api/public/**", "/student/add-account-to-student/**").permitAll()
                        .requestMatchers("/public/**", "/ws/**").permitAll()
                        .anyRequest()
                        .authenticated())
                .formLogin(f -> f.loginPage("/public/auth/login").defaultSuccessUrl("/", true).permitAll())
                .exceptionHandling((ex) -> ex.authenticationEntryPoint((req, res, authEx) -> {
                    System.out.printf(authEx.getMessage());
                    // res.sendError(HttpServletResponse.SC_UNAUTHORIZED, authEx.getMessage());
                    res.sendRedirect("/public/auth/login");
                }))
                .build();
    }
}
