package com.example.GymApp.controller;

import com.example.GymApp.model.Exercise;
import com.example.GymApp.model.ProgramExercise;
import com.example.GymApp.service.IExerciseService;
import com.example.GymApp.service.UploadFileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/exercise")
public class ExerciseController {

    private final Logger log = LoggerFactory.getLogger(ExerciseController.class);
    private final IExerciseService exerciseService;
    private final UploadFileService uploadFileService;

    List<ProgramExercise> programExercises = new ArrayList<>();

    public ExerciseController(IExerciseService exerciseService,
                              UploadFileService uploadFileService) {
        this.exerciseService = exerciseService;
        this.uploadFileService = uploadFileService;
    }

    @GetMapping("/")
    public String exercises() {
        return "exercise/exercises";
    }

    @GetMapping("/create")
    public String create() {
        return "exercise/create";
    }

    @PostMapping("/save")
    public String save(Exercise exercise,
                       @RequestParam("img") MultipartFile file) throws IOException {

        log.info("exercise: {}", exercise);

        if (exercise.getId() == null) {
            String nameImage = uploadFileService.saveImage(file);
            exercise.setImage(nameImage);
        }
        exerciseService.save(exercise);
        return "redirect:/exercise/";
    }

    @GetMapping("/show/{type}")
    public String show(@PathVariable(name = "type") String type,
                       Model model) {

        List<Exercise> exercises = exerciseService.findByType(type);
        model.addAttribute("exercises", exercises);
        return "exercise/exercises";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable(name = "id") Integer id) {

        Exercise exercise = new Exercise();
        exercise = exerciseService.findById(id).get();

        if (!exercise.getImage().equals("default.jpg")) {
            uploadFileService.deleteImage(exercise.getImage());
        }

        exerciseService.delete(id);
        return "redirect:/exercise/";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable(name = "id") Integer id,
                       Model model) {
        Exercise exercise = new Exercise();
        exercise = exerciseService.findById(id).get();
        String type = exercise.getType();
        model.addAttribute("selectedType", type);
        model.addAttribute("exercise", exercise);

        return "exercise/edit";
    }

    @GetMapping("/add/{id}")
    public String add(@PathVariable(name = "id") Integer id,
                      Model model) {
        Exercise exercise = new Exercise();
        exercise = exerciseService.findById(id).get();
        model.addAttribute("exercise", exercise);

        return "exercise/add";
    }

    @PostMapping("/update")
    public String update(Exercise exercise,
                         @RequestParam(name = "img") MultipartFile file) throws IOException {

        Exercise e = exerciseService.findById(exercise.getId()).get();

        if (file.isEmpty()) {
            exercise.setImage(e.getImage());
        } else {
            if (!e.getImage().equals("default.jpg")) {
                uploadFileService.deleteImage(e.getImage());
            } else{
                String nameImage = uploadFileService.saveImage(file);
                exercise.setImage(nameImage);
            }
        }
        exerciseService.update(exercise);
        return "redirect:/exercise/";
    }

    @PostMapping("/addExercise")
    public String addExercise(@RequestParam(name = "exercise_id") Integer exercise_id,
                              @RequestParam(name = "repetitions") String repetitions,
                              @RequestParam(name = "rest") String rest,
                              @RequestParam(name = "weight") String weight,
                              @RequestParam(name = "day")String day,
                              @RequestParam(name = "notes")String notes,
                              Model model) {

        ProgramExercise programExercise = new ProgramExercise();
        Exercise exercise = new Exercise();

        exercise = exerciseService.findById(exercise_id).get();

        programExercise.setName(exercise.getName());
        programExercise.setExercise(exercise);
        programExercise.setImage(exercise.getImage());
        programExercise.setRepetitions(repetitions);
        programExercise.setRest(rest);
        programExercise.setWeight(weight);
        programExercise.setDay(day);
        programExercise.setNotes(notes);

        programExercises.add(programExercise);

        Map<String, List<ProgramExercise>> exercisesByDay = programExercises.stream()
                .collect(Collectors.groupingBy(ProgramExercise::getDay));

        List<ProgramExercise> exercisesForMonday = exercisesByDay.get("monday");
        List<ProgramExercise> exercisesForTuesday = exercisesByDay.get("tuesday");
        List<ProgramExercise> exercisesForWednesday = exercisesByDay.get("wednesday");
        List<ProgramExercise> exercisesForThursday = exercisesByDay.get("thursday");
        List<ProgramExercise> exercisesForFriday = exercisesByDay.get("friday");
        List<ProgramExercise> exercisesForSaturday = exercisesByDay.get("saturday");

        model.addAttribute("exercisesForMonday", exercisesForMonday);
        model.addAttribute("exercisesForTuesday", exercisesForTuesday);
        model.addAttribute("exercisesForWednesday", exercisesForWednesday);
        model.addAttribute("exercisesForThursday", exercisesForThursday);
        model.addAttribute("exercisesForFriday", exercisesForFriday);
        model.addAttribute("exercisesForSaturday", exercisesForSaturday);
        return "redirect:/exercise/";
    }
}
