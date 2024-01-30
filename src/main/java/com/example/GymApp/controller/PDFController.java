package com.example.GymApp.controller;


import com.example.GymApp.model.Program;
import com.example.GymApp.model.ProgramExercise;
import com.example.GymApp.service.IProgramService;
import com.example.GymApp.service.PdfService;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Controller
public class PDFController {

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

        //Data
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
            String dates = "Start date: " + startDateFormatted + " -- End date: " + endDateFormatted;
            Paragraph dateParagraph = new Paragraph(dates);
            dateParagraph.setFontSize(8);
            document.add(dateParagraph);


            Table mainTable = new Table(6);
            Table columnMonday = pdfService.createExerciseTable(exercisesForMonday, "Monday");
            Table columnTuesday = pdfService.createExerciseTable(exercisesForTuesday, "Tuesday");
            Table columnWednesday = pdfService.createExerciseTable(exercisesForWednesday, "Wednesday");
            Table columnThursday = pdfService.createExerciseTable(exercisesForThursday, "Thursday");
            Table columnFriday = pdfService.createExerciseTable(exercisesForFriday, "Friday");
            Table columnSaturday = pdfService.createExerciseTable(exercisesForSaturday, "Saturday");

            if (!exercisesForMonday.isEmpty()) {
                mainTable.addCell(columnMonday);
            }
            if(!exercisesForTuesday.isEmpty()){
                mainTable.addCell(columnTuesday);
            }
            if(!exercisesForWednesday.isEmpty()){
                mainTable.addCell(columnWednesday);
            }
            if(!exercisesForThursday.isEmpty()){
                mainTable.addCell(columnThursday);
            }
            if(!exercisesForFriday.isEmpty()){
                mainTable.addCell(columnFriday);
            }
            if(!exercisesForSaturday.isEmpty()){
                mainTable.addCell(columnSaturday);
            }

            document.add(mainTable);
            document.close();

            response.getOutputStream().flush();
            response.getOutputStream().close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
