package com.example.demo.service.student;

import com.example.demo.dto.AddAccountRequest;
import com.example.demo.entity.Account;
import com.example.demo.entity.Student;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public interface StudentService {

    List<Student> getAll();

    Optional<Student> getById(String id);

    void addStudent(Student student);

    void editStudent(Student student);

    void deleteStudent(String student);

    void addAll(List<Student> students);

    ResponseEntity<?> addAccountToStudent(String id, AddAccountRequest request);

    Student findByAccount(Account account);
}
