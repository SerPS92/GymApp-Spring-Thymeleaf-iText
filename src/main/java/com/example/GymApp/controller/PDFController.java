package com.example.GymApp.controller;


import com.example.GymApp.model.Program;
import com.example.GymApp.model.ProgramExercise;
import com.example.GymApp.service.IProgramService;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class PDFController {

    private final String folder = "src/main/resources/static/images//";
    private final IProgramService programService;

    public PDFController(IProgramService programService) {
        this.programService = programService;
    }

    @GetMapping("/generate-pdf")
    public void generatePdf(HttpServletResponse response) throws IOException {

        int id = 13;
        Program program = programService.findById(id).get();
        List<ProgramExercise> programExercisesFinal = program.getProgramExercises();

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

        try {
            response.setContentType("application/pdf");

            PdfWriter writer = new PdfWriter(response.getOutputStream());
            PdfDocument pdf = new PdfDocument(writer);
            pdf.setDefaultPageSize(com.itextpdf.kernel.geom.PageSize.A4.rotate());
            Document document = new Document(pdf);

            document.add(new Paragraph("Program"));

            Table exerciseTableMonday = new Table(1);
            for(ProgramExercise exercise : exercisesForMonday){
                ImageData imageData = ImageDataFactory.create(folder + exercise.getImage());
                Image image = new Image(imageData);
                image.setWidth(50);
                image.setHeight(50);

                Cell cell = new Cell().add(image);
                cell.setPadding(5);
                exerciseTableMonday.addCell(cell);
            }

            document.add(exerciseTableMonday);
            document.close();

            response.getOutputStream().flush();
            response.getOutputStream().close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
