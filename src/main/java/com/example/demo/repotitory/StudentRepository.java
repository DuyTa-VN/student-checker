package com.example.demo.repotitory;

import com.example.demo.entity.Account;
import com.example.demo.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, String> {
    Optional<Student> findByFingerId(String id);
    Optional<Student> findByAccount(Account account);
}

