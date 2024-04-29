package com.example.GymApp.service;

import com.example.GymApp.model.Program;
import com.example.GymApp.repository.IProgramRepo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProgramServiceImpl implements IProgramService{

    private final IProgramRepo programRepo;

    public ProgramServiceImpl(IProgramRepo programRepo) {
        this.programRepo = programRepo;
    }

    @Override
    public List<Program> findAll() {
        return programRepo.findAll();
    }

    @Override
    public Optional<Program> findById(Integer id) {
        return programRepo.findById(id);
    }

    @Override
    public List<Program> findAllByOrderByIdDesc() {
        return programRepo.findAllByOrderByIdDesc();
    }

    @Override
    public Program save(Program program) {
        return programRepo.save(program);
    }

    @Override
    public void delete(Integer id) {
        programRepo.deleteById(id);
    }
}
