package com.example.GymApp.service;

import com.example.GymApp.model.ProgramExercise;
import com.example.GymApp.repository.IProgramExerciseRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProgramExerciseServiceImpl implements IProgramExerciseService{

    private final IProgramExerciseRepo programExerciseRepo;

    public ProgramExerciseServiceImpl(IProgramExerciseRepo programExerciseRepo) {
        this.programExerciseRepo = programExerciseRepo;
    }

    @Override
    public List<ProgramExercise> findAll() {
        return programExerciseRepo.findAll();
    }

    @Override
    public ProgramExercise save(ProgramExercise programExercise) {
        return programExerciseRepo.save(programExercise);
    }
}
