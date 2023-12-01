package com.example.GymApp.controller;

import com.example.GymApp.model.Exercise;
import com.example.GymApp.service.IExerciseService;
import com.example.GymApp.service.UploadFileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/exercise")
public class ExerciseController {

    private final Logger log = LoggerFactory.getLogger(ExerciseController.class);
    private final IExerciseService exerciseService;
    private final UploadFileService uploadFileService;

    public ExerciseController(IExerciseService exerciseService,
                              UploadFileService uploadFileService) {
        this.exerciseService = exerciseService;
        this.uploadFileService = uploadFileService;
    }

    @GetMapping("/")
    public String exercises(){
        return "exercise/exercises";
    }

    @GetMapping("/create")
    public String create(){
        return "exercise/create";
    }

    @PostMapping("/save")
    public String save(Exercise exercise,
                       @RequestParam("img") MultipartFile file) throws IOException {

        log.info("exercise: {}", exercise);

        if(exercise.getId() == null){
            String nameImage = uploadFileService.saveImage(file);
            exercise.setImage(nameImage);
        }
        exerciseService.save(exercise);
        return "redirect:/exercise/";
    }

    @GetMapping("/show/{type}")
    public String show(@PathVariable(name = "type") String type,
                       Model model){

        List<Exercise> exercises = exerciseService.findByType(type);
        model.addAttribute("exercises", exercises);
        return "exercise/exercises";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable(name = "id") Integer id){

        Exercise exercise = new Exercise();
        exercise = exerciseService.findById(id).get();

        if(!exercise.getImage().equals("default.jpg")){
            uploadFileService.deleteImage(exercise.getImage());
        }

        exerciseService.delete(id);
        return "redirect:/exercise/";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable(name = "id")Integer id,
                       Model model){
        Exercise exercise = new Exercise();
        exercise = exerciseService.findById(id).get();
        String type = exercise.getType();
        model.addAttribute("selectedType", type);
        model.addAttribute("exercise", exercise);

        return "exercise/edit";
    }
}
