package com.example.demo.dto;

import com.example.demo.entity.Checker;
import com.example.demo.entity.Student;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class SearchResponse {
    private Student student;
    private List<Checker> checkers = new ArrayList<>();
}
