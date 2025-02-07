package com.example.demo.service.checker;

import com.example.demo.entity.Checker;
import com.example.demo.entity.Student;
import com.example.demo.repotitory.CheckerRepository;
import com.example.demo.repotitory.StudentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.Check;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import com.example.demo.dto.AddFingerRequest;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CheckerServiceImpl implements CheckerService {
    private final CheckerRepository checkerRepository;

    private final StudentRepository studentRepository;

    private final SimpMessagingTemplate simpMessagingTemplate;

    @Override
    public List<Checker> getAll() {
        return checkerRepository.findAll(Sort.by(Sort.Order.desc("date")));
    }

    @Override
    public List<Checker> getAllByUser(Student student) {
        return checkerRepository.findAllByStudent(student);
    }

    @Override
    public ResponseEntity<?> checkInByFinger(String id) {
        Student student = studentRepository.findByFingerId(id).orElse(null);
        if (student == null) {
            return ResponseEntity.notFound().build();
        }
        // get check in first
        List<Checker> checkers = checkerRepository.findAllByStudent(student);
        checkers.sort(Comparator.comparing(Checker::getTimeIn).reversed());
        Checker checker = new Checker();
        if (checkers.size() == 0) {
            checker.setDate(LocalDate.now());
            checker.setStatus("Đã check in");
            checker.setStudent(student);
            checker.setTimeout(null);
            checker.setTimeIn(LocalTime.now());
        } else {
            if (checkers.get(0).getTimeout() != null) {
                checker.setDate(LocalDate.now());
                checker.setStatus("Đã check in");
                checker.setStudent(student);
                checker.setTimeout(null);
                checker.setTimeIn(LocalTime.now());
            } else {
                checker = checkers.get(0);
                checker.setTimeout(LocalTime.now());
                checker.setStatus("Đã checkout");
            }
        }
        Checker savedChecker = checkerRepository.save(checker);
        simpMessagingTemplate.convertAndSend("/topic/notification", savedChecker);
        return ResponseEntity.ok(savedChecker);
    }

    @Override
    public ResponseEntity<?> checkOutById(long id) {
        Checker checker = checkerRepository.findById(id).orElse(null);
        if (checker == null) {
            return ResponseEntity.notFound().build();
        }
        checker.setTimeout(LocalTime.now());
        Checker savedChecker = checkerRepository.save(checker);
        simpMessagingTemplate.convertAndSend("/topic/notification", savedChecker);
        return ResponseEntity.ok(savedChecker);
    }

    @Override
    public ResponseEntity<?> addFinger(AddFingerRequest request) {
        Student std = studentRepository.findById(request.getStudentId()).orElse(null);
        if (std == null) {
            return ResponseEntity.notFound().build();
        }
        std.setFingerId(request.getFingerId());
        return ResponseEntity.ok(studentRepository.save(std));
    }
}
