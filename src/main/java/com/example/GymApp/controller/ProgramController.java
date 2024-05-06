package com.example.GymApp.controller;

import com.example.GymApp.model.Exercise;
import com.example.GymApp.model.Program;
import com.example.GymApp.model.ProgramExercise;
import com.example.GymApp.model.User;
import com.example.GymApp.service.IExerciseService;
import com.example.GymApp.service.IProgramExerciseService;
import com.example.GymApp.service.IProgramService;
import com.example.GymApp.service.IUserService;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/program")
public class ProgramController {

    private final Logger log = LoggerFactory.getLogger(ProgramController.class);
    private final IExerciseService exerciseService;
    private final IProgramExerciseService programExerciseService;
    private final IProgramService programService;
    private final IUserService userService;

    public ProgramController(IExerciseService exerciseService,
                             IProgramExerciseService programExerciseService,
                             IProgramService programService,
                             IUserService userService) {
        this.exerciseService = exerciseService;
        this.programExerciseService = programExerciseService;
        this.programService = programService;
        this.userService = userService;
    }

    @GetMapping("/add/{id}")
    public String add(@PathVariable(name = "id") Integer id,
                      Model model) {
        Exercise exercise = new Exercise();
        exercise = exerciseService.findById(id).get();
        model.addAttribute("exercise", exercise);

        return "exercise/add";
    }


    @PostMapping("/addExercise")
    public String addExercise(@RequestParam(name = "exercise_id") Integer exercise_id,
                              @RequestParam(name = "repetitions") String repetitions,
                              @RequestParam(name = "series") String series,
                              @RequestParam(name = "rest") String rest,
                              @RequestParam(name = "weight") String weight,
                              @RequestParam(name = "day") String day,
                              @RequestParam(name = "notes") String notes,
                              HttpSession session) {

        ProgramExercise programExercise = new ProgramExercise();
        Exercise exercise = exerciseService.findById(exercise_id).get();

        programExercise.setName(exercise.getName());
        programExercise.setExercise(exercise);
        programExercise.setImage(exercise.getImage());
        programExercise.setRepetitions(repetitions);
        programExercise.setSeries(series);
        programExercise.setRest(rest);
        programExercise.setWeight(weight);
        programExercise.setDay(day);
        programExercise.setNotes(notes);

        List<ProgramExercise> programExercises = (List<ProgramExercise>) session.getAttribute("programExercises");
        programExercises.add(programExercise);
        session.setAttribute("programExercises", programExercises);

        String type = (String) session.getAttribute("type");
        if(type == null){
            type = "All";
        }
        return "redirect:/exercise/show/" + type;
    }

    @GetMapping("/quickAdd/{id}")
    public String quickAdd(@PathVariable(name = "id") Integer id,
                           @RequestParam(name = "day") String day,
                           HttpSession session) {
        ProgramExercise programExercise = new ProgramExercise();
        Exercise exercise = exerciseService.findById(id).get();

        programExercise.setName(exercise.getName());
        programExercise.setExercise(exercise);
        programExercise.setImage(exercise.getImage());
        programExercise.setDay(day);
        programExercise.setSeries("");
        programExercise.setWeight("");
        programExercise.setNotes("");
        programExercise.setRest("");
        programExercise.setRepetitions("");

        List<ProgramExercise> programExercises = (List<ProgramExercise>) session.getAttribute("programExercises");
        programExercises.add(programExercise);
        session.setAttribute("programExercises", programExercises);

        String type = (String) session.getAttribute("type");
        if(type == null){
            type = "All";
        }
        return "redirect:/exercise/show/" + type;
    }

    @GetMapping("/quit/{id}")
    public String quit(@PathVariable(name = "id") Integer id,
                       @RequestParam(name = "day") String day,
                       HttpSession session) {
        List<ProgramExercise> newProgramExercises = new ArrayList<>();
        List<ProgramExercise> programExercises = (List<ProgramExercise>) session.getAttribute("programExercises");
        for (ProgramExercise programExercise : programExercises) {
            if (Objects.equals(programExercise.getExercise().getId(), id) && !programExercise.getDay().equals(day)) {
                newProgramExercises.add(programExercise);
            } else if (!Objects.equals(programExercise.getExercise().getId(), id)) {
                newProgramExercises.add(programExercise);
            }
        }
        programExercises = newProgramExercises;
        session.setAttribute("programExercises", programExercises);
        return "redirect:/program/provisional";
    }


    @GetMapping("/provisional")
    public String provisional(Model model,
                              HttpSession session) {

        List<ProgramExercise> exercisesForMonday = new ArrayList<>();
        List<ProgramExercise> exercisesForTuesday = new ArrayList<>();
        List<ProgramExercise> exercisesForWednesday = new ArrayList<>();
        List<ProgramExercise> exercisesForThursday = new ArrayList<>();
        List<ProgramExercise> exercisesForFriday = new ArrayList<>();

        List<ProgramExercise> programExercises = (List<ProgramExercise>) session.getAttribute("programExercises");

        if (programExercises != null) {
            for (ProgramExercise exercise : programExercises) {
                if (exercise.getDay().equals("monday")) {
                    exercisesForMonday.add(exercise);
                } else if (exercise.getDay().equals("tuesday")) {
                    exercisesForTuesday.add(exercise);
                } else if (exercise.getDay().equals("wednesday")) {
                    exercisesForWednesday.add(exercise);
                } else if (exercise.getDay().equals("thursday")) {
                    exercisesForThursday.add(exercise);
                } else if (exercise.getDay().equals("friday")) {
                    exercisesForFriday.add(exercise);
                }
            }
        }

        model.addAttribute("exercisesForMonday", exercisesForMonday);
        model.addAttribute("exercisesForTuesday", exercisesForTuesday);
        model.addAttribute("exercisesForWednesday", exercisesForWednesday);
        model.addAttribute("exercisesForThursday", exercisesForThursday);
        model.addAttribute("exercisesForFriday", exercisesForFriday);

        return "program/provisionalprogram";
    }

    @GetMapping("/createProgram")
    public String save(Model model) {
        List<User> users = userService.findAll();
        model.addAttribute("users", users);
        return "program/saveprogram";
    }

    @PostMapping("/saveProgram")
    public String saveProgram(Program program,
                              HttpSession session) {

        List<ProgramExercise> programExercises = (List<ProgramExercise>) session.getAttribute("programExercises");
        program.setProgramExercises(programExercises);
        programService.save(program);

        for (ProgramExercise programExercise : programExercises) {
            programExercise.setProgram(program);
            programExerciseService.save(programExercise);
        }

        programExercises.clear();
        session.setAttribute("programExercises", programExercises);
        return "redirect:/program/programs";
    }

    @GetMapping("/programs")
    public String programs(Model model) {
        List<Program> programs = new ArrayList<>();
        programs = programService.findAllByOrderByIdDesc();
        model.addAttribute("programs", programs);
        return "program/programs";
    }

    @GetMapping("/show/{id}")
    public String show(@PathVariable(name = "id") Integer id,
                       Model model) {
        Program program = programService.findById(id).get();
        List<ProgramExercise> programExercisesFinal = new ArrayList<>();
        programExercisesFinal = program.getProgramExercises();

        List<ProgramExercise> exercisesForMonday = new ArrayList<>();
        List<ProgramExercise> exercisesForTuesday = new ArrayList<>();
        List<ProgramExercise> exercisesForWednesday = new ArrayList<>();
        List<ProgramExercise> exercisesForThursday = new ArrayList<>();
        List<ProgramExercise> exercisesForFriday = new ArrayList<>();
        List<ProgramExercise> exercisesForSaturday = new ArrayList<>();

        for (ProgramExercise exercise : programExercisesFinal) {
            if (exercise.getDay().equals("monday")) {
                exercisesForMonday.add(exercise);
            } else if (exercise.getDay().equals("tuesday")) {
                exercisesForTuesday.add(exercise);
            } else if (exercise.getDay().equals("wednesday")) {
                exercisesForWednesday.add(exercise);
            } else if (exercise.getDay().equals("thursday")) {
                exercisesForThursday.add(exercise);
            } else if (exercise.getDay().equals("friday")) {
                exercisesForFriday.add(exercise);
            } else if (exercise.getDay().equals("saturday")) {
                exercisesForSaturday.add(exercise);
            }
        }

        model.addAttribute("program", program);
        model.addAttribute("exercisesForMonday", exercisesForMonday);
        model.addAttribute("exercisesForTuesday", exercisesForTuesday);
        model.addAttribute("exercisesForWednesday", exercisesForWednesday);
        model.addAttribute("exercisesForThursday", exercisesForThursday);
        model.addAttribute("exercisesForFriday", exercisesForFriday);
        model.addAttribute("exercisesForSaturday", exercisesForSaturday);

        return "program/program";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable(name = "id") Integer id) {
        programService.delete(id);
        return "redirect:/program/programs";
    }

}
