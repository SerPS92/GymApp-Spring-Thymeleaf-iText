package com.example.GymApp.service;

import com.example.GymApp.model.Exercise;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


public interface IExerciseService {

    List<Exercise> findAll();
    List<Exercise> findByType(String type);
    Optional<Exercise> findById(Integer id);
    Exercise save(Exercise exercise);
    void update(Exercise exercise);
    void delete(Integer id);
}
