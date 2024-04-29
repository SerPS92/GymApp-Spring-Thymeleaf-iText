package com.example.GymApp.repository;

import com.example.GymApp.model.Program;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IProgramRepo extends JpaRepository<Program, Integer> {
    List<Program> findAllByOrderByIdDesc();

}
