package com.example.GymApp.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String weight;
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private Date birthday;

    @OneToMany(mappedBy = "user")
    private List<Program> programs;
}
