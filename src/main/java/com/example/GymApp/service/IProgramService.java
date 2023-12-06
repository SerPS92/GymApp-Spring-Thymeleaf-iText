package com.example.GymApp.service;

import com.example.GymApp.model.Program;

import java.util.List;

public interface IProgramService {
    List<Program> findAll();
    Program save(Program program);
    void delete(Integer id);
}
