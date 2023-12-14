package com.example.GymApp.service;

import com.example.GymApp.model.ProgramExercise;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.util.List;

@Service
public class PdfService {

    private static final String folder = "src/main/resources/static/images//";

    public Table createExerciseTable(List<ProgramExercise> exercises, String day) throws MalformedURLException {
        Table column = new Table(1);
        column.addCell(new Cell().add(new Paragraph(day)));

        for(ProgramExercise exercise: exercises){
            Cell imageCell = new Cell();
            ImageData imageData = ImageDataFactory.create(folder + exercise.getImage());
            Image image = new Image(imageData);
            image.setWidth(60);
            image.setHeight(50);
            imageCell.add(image);

            Cell textCell = new Cell();
            textCell.setTextAlignment(TextAlignment.LEFT);
            Paragraph paragraph = new Paragraph("S: " + exercise.getSeries() + "\n" +
                    "W: " + exercise.getWeight() + "\n" +
                    "Rp: " + exercise.getRepetitions() + "\n" +
                    "R: " + exercise.getRest() + "\n" +
                    "Note: " + exercise.getNotes());
            paragraph.setFontSize(7);
            textCell.add(paragraph);
            textCell.setWidth(80);

            Table rowTable = new Table(2);
            rowTable.addCell(imageCell);
            rowTable.addCell(textCell);
            column.addCell(rowTable);
        }
        return column;
    }
}
