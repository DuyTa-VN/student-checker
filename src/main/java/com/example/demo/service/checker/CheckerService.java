package com.example.demo.service.checker;

import com.example.demo.entity.Checker;
import com.example.demo.entity.Student;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import com.example.demo.dto.AddFingerRequest;

public interface CheckerService {
    List<Checker> getAll();

    List<Checker> getAllByUser(Student student);

    ResponseEntity<?> checkInByFinger(String id);

    ResponseEntity<?> checkOutById(long id);
    /////////////
    ResponseEntity<?> addFinger(AddFingerRequest request);
}
