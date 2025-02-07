package com.example.demo.service.student;

import com.example.demo.dto.AddAccountRequest;
import com.example.demo.entity.Account;
import com.example.demo.entity.Student;
import com.example.demo.repotitory.AccountRepository;
import com.example.demo.repotitory.CheckerRepository;
import com.example.demo.repotitory.StudentRepository;
import com.example.demo.ulti.SecurityHelper;
import com.example.demo.ulti.StringHelper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class StudentServiceImpl implements StudentService {
    private final StudentRepository studentRepository;

    private final CheckerRepository checkerRepository;

    private final AccountRepository accountRepository;

    @Override
    public List<Student> getAll() {
        return studentRepository.findAll();
    }

    @Override
    public Optional<Student> getById(String id) {
        return studentRepository.findById(id);
    }

    @Override
    public void addStudent(Student student) {
        Student savedStudent = studentRepository.save(student);
        Account account = new Account();
        account.setUsername(student.getId());
        account.setPassword(new BCryptPasswordEncoder().encode("123456"));
        account.setRawPassword("123456");
        account.setRole("ROLE_USER");
        accountRepository.save(account);
        savedStudent.setAccount(account);
        studentRepository.save(savedStudent);
    }

    @Override
    public void editStudent(Student student) {
        Student student1 = studentRepository.findById(student.getId()).orElse(new Student());
        BeanUtils.copyProperties(student, student1, SecurityHelper.getNullPropertyNames(student));
        if (student.getFingerId() != null) {
            student1.setFingerId(student.getFingerId());
        }
        studentRepository.save(student1);
    }

    @Override
    public void deleteStudent(String student) {
        Student s = studentRepository.findById(student).orElse(null);
        if (s != null) {
            checkerRepository.deleteAllByStudent(s);
            studentRepository.deleteById(student);
            accountRepository.deleteById(s.getAccount().getId());
        }
    }

    @Override
    public void addAll(List<Student> students) {
        studentRepository.saveAll(students);
    }

    @Override
    public ResponseEntity<?> addAccountToStudent(String id, AddAccountRequest request) {
        Student student = studentRepository.findById(id).orElse(null);
        if (student == null) {
            return ResponseEntity.notFound().build();
        }
        if (student.getAccount() != null) {
            return ResponseEntity.badRequest().body("Account had exists on this student");
        }
        Account account = new Account();
        account.setUsername(request.getUsername());
        account.setRole("ROLE_USER");
        account.setPassword(new BCryptPasswordEncoder().encode(request.getPassword()));
        student.setAccount(accountRepository.save(account));
        return ResponseEntity.ok(studentRepository.save(student));
    }

    @Override
    public Student findByAccount(Account account) {
        return studentRepository.findByAccount(account).orElse(null);
    }
}
