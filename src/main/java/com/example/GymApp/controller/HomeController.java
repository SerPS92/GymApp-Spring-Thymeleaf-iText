package com.example.GymApp.controller;

import com.example.GymApp.model.ProgramExercise;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/")
public class HomeController {
    @GetMapping("")
    public String home(HttpSession session){
        if(session.getAttribute("programExercise") == null){
            List<ProgramExercise> programExercises = new ArrayList<>();
            session.setAttribute("programExercises", programExercises);
        }
        System.out.println("Id de sesión: " + session.getId());
        return "redirect:/exercise/";
    }
}
