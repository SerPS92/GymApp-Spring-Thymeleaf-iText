package com.example.GymApp.controller;


import com.example.GymApp.model.Program;
import com.example.GymApp.model.ProgramExercise;
import com.example.GymApp.service.IProgramService;
import com.example.GymApp.service.PdfService;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class PDFController {

    private final String folder = "src/main/resources/static/images//";
    private final IProgramService programService;
    private final PdfService pdfService;

    public PDFController(IProgramService programService,
                         PdfService pdfService) {
        this.programService = programService;
        this.pdfService = pdfService;
    }

    @GetMapping("/generate-pdf/{id}")
    public void generatePdf(@PathVariable(name = "id") Integer id,
                            HttpServletResponse response) throws IOException {

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

        //PDF
        try {
            response.setContentType("application/pdf");
            PdfWriter writer = new PdfWriter(response.getOutputStream());
            PdfDocument pdf = new PdfDocument(writer);
            pdf.setDefaultPageSize(com.itextpdf.kernel.geom.PageSize.A4.rotate());
            Document document = new Document(pdf);

            //Date
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            String startDateFormatted = dateFormat.format(program.getStart_date());
            String endDateFormatted = dateFormat.format(program.getEnd_date());
            Paragraph dateParagraph = new Paragraph("Start date: " + startDateFormatted +
                    "--End date: " + endDateFormatted);
            dateParagraph.setFontSize(8);
            document.add(dateParagraph);


            Table mainTable = new Table(6);

            Table columnMonday = pdfService.createExerciseTable(exercisesForMonday, "Monday");
            Table columnTuesday = pdfService.createExerciseTable(exercisesForTuesday, "Tuesday");
            Table columnWednesday = pdfService.createExerciseTable(exercisesForWednesday, "Wednesday");
            Table columnThursday = pdfService.createExerciseTable(exercisesForThursday, "Thursday");
            Table columnFriday = pdfService.createExerciseTable(exercisesForFriday, "Friday");
            Table columnSaturday = pdfService.createExerciseTable(exercisesForSaturday, "Saturday");

            mainTable.addCell(columnMonday);
            mainTable.addCell(columnTuesday);
            mainTable.addCell(columnWednesday);
            mainTable.addCell(columnThursday);
            mainTable.addCell(columnFriday);
            mainTable.addCell(columnSaturday);
            document.add(mainTable);
            document.close();

            response.getOutputStream().flush();
            response.getOutputStream().close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
