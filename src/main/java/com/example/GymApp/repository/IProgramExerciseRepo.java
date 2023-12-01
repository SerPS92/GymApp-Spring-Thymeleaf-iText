package com.example.GymApp.repository;

import com.example.GymApp.model.ProgramExercise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

@Repository
public interface IProgramExerciseRepo extends JpaRepository<ProgramExercise, Integer> {
}
