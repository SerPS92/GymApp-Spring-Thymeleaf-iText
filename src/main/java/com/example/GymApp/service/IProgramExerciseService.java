package com.example.GymApp.service;

import com.example.GymApp.model.ProgramExercise;

import java.util.List;

public interface IProgramExerciseService {
    List<ProgramExercise> findAll();
    ProgramExercise save(ProgramExercise programExercise);

}
