package com.example.GymApp.service;

import com.example.GymApp.model.Exercise;
import com.example.GymApp.repository.IExerciseRepo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ExerciseServiceImpl implements IExerciseService{

    private final IExerciseRepo exerciseRepo;

    public ExerciseServiceImpl(IExerciseRepo exerciseRepo) {
        this.exerciseRepo = exerciseRepo;
    }

    @Override
    public List<Exercise> findAll() {
        return exerciseRepo.findAll();
    }

    @Override
    public List<Exercise> findByType(String type) {
        return exerciseRepo.findByType(type);
    }

    @Override
    public Optional<Exercise> findById(Integer id) {
        return exerciseRepo.findById(id);
    }

    @Override
    public Exercise save(Exercise exercise) {
        return exerciseRepo.save(exercise);
    }

    @Override
    public void update(Exercise exercise) {
        exerciseRepo.save(exercise);
    }

    @Override
    public void delete(Integer id) {
        exerciseRepo.deleteById(id);
    }
}
