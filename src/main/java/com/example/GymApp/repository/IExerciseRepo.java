package com.example.GymApp.repository;

import com.example.GymApp.model.Exercise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IExerciseRepo extends JpaRepository<Exercise, Integer> {

    List<Exercise> findByType(String type);
}
