package com.example.demo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Data
public class Checker {

    @jakarta.persistence.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @ManyToOne
    private Student student;

    @Column
    private LocalTime timeIn;

    @Column
    private LocalTime timeout;

    @Size(max = 100)
    private String status;
    private LocalDate date = LocalDate.now();

}
