package com.example.GymApp.repository;

import com.example.GymApp.model.Program;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.relational.core.sql.In;
import org.springframework.stereotype.Repository;

@Repository
public interface IProgramRepo extends JpaRepository<Program, Integer> {
}
