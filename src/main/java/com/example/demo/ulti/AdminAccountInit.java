package com.example.demo.ulti;

import com.example.demo.entity.Account;
import com.example.demo.repotitory.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminAccountInit implements CommandLineRunner {
    private final AccountRepository accountRepository;

    @Override
    public void run(String... args) throws Exception {
        Account account = accountRepository.findByUsername("kandev").orElse(new Account());
        account.setUsername("kandev");
        account.setPassword(new BCryptPasswordEncoder().encode("13122002"));
        accountRepository.save(account);
    }
}
