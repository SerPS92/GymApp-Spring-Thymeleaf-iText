package com.example.GymApp.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "program_exercises")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ProgramExercise {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String name;
    private String repetitions;
    private String series;
    private String rest;
    private String weight;
    private String image;
    private String day;
    private String notes;

    @ManyToOne
    private Program program;
    @ManyToOne
    private Exercise exercise;
}
