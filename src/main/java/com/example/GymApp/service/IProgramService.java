package com.example.GymApp.service;

import com.example.GymApp.model.Program;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

public interface IProgramService {
    List<Program> findAll();
    Optional<Program> findById(Integer id);
    List<Program> findAllByOrderByIdDesc();
    Program save(Program program);
    void delete(Integer id);
}
