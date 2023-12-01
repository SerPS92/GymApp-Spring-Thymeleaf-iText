package com.example.GymApp.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "programs")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Program {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private Date start_date;
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private Date end_date;

    @OneToMany(mappedBy = "program")
    private List<ProgramExercise> programExercises;

    @ManyToOne
    private User user;



}
