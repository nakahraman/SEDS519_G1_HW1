package com.seds519.hw1.parser;

import com.seds519.hw1.model.Course;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import technology.tabula.*;
import technology.tabula.extractors.BasicExtractionAlgorithm;

import java.io.InputStream;
import java.util.*;

@Component("pdf")
public class PdfFileParser implements FileParser {

    private static final String[] DAYS = { "Pazartesi", "Salı", "Çarşamba", "Perşembe", "Cuma" };

    @Override
    public List<Course> parse(MultipartFile file) throws Exception {
        List<Course> courses = new ArrayList<>();
      //  String currentTimeSlot = "";

        try (InputStream inputStream = file.getInputStream();
             PDDocument document = PDDocument.load(inputStream)) {

            ObjectExtractor extractor = new ObjectExtractor(document);
            BasicExtractionAlgorithm algorithm = new BasicExtractionAlgorithm();
            PageIterator iterator = extractor.extract();



            while (iterator.hasNext()) {
                Page page = iterator.next();
                List<Table> tables = algorithm.extract(page);

                for (Table table : tables) {
                    List<List<RectangularTextContainer>> rows = table.getRows();

                    int[] rowGroups = { 5, 7, 7, 7 };
                    String[] timeSlots = { "08:45-09:30", "09:45-10:30", "10:45-11:30", "11:45-12:30" };

                    int rowIndex = 0;
                    for (int t = 0; t < timeSlots.length; t++) {
                        String currentTimeSlot = timeSlots[t];
                        int blockSize = rowGroups[t];

                        for (int i = 0; i < blockSize && rowIndex < rows.size(); i++, rowIndex++) {
                            List<RectangularTextContainer> row = rows.get(rowIndex);

                            for (int j = 1; j + 2 < row.size(); j += 3) {
                                String maybeClassroom = row.get(j).getText().trim();
                                String maybeCode = row.get(j + 1).getText().trim();
                                String maybeInstructor = row.get(j + 2).getText().trim();

                                if (isCourseCode(maybeCode)) {
                                    int dayIndex = j / 3;
                                    if (dayIndex < DAYS.length) {
                                        Course course = new Course(
                                                translateDayToEnglish(DAYS[dayIndex]),
                                                maybeCode,
                                                currentTimeSlot,
                                                isInstructor(maybeInstructor) ? maybeInstructor : "",
                                                isClassroom(maybeClassroom) ? maybeClassroom : ""
                                        );
                                        courses.add(course);
                                    }
                                }
                            }
                            int startIndex = row.size() % 3 == 0 ? 0 : 1; // 15 sütun varsa 5x3 blok demektir, ama 16 ise zaman var demektir

                            for (int j = startIndex; j + 2 < row.size(); j += 3) {
                                String maybeClassroom = row.get(j).getText().trim();
                                String maybeCode = row.get(j + 1).getText().trim();
                                String maybeInstructor = row.get(j + 2).getText().trim();

                                if (isCourseCode(maybeCode)) {
                                    int dayIndex = (j - startIndex) / 3;
                                    if (dayIndex < DAYS.length) {
                                        Course course = new Course(
                                                translateDayToEnglish(DAYS[dayIndex]),
                                                maybeCode,
                                                currentTimeSlot,
                                                isInstructor(maybeInstructor) ? maybeInstructor : "",
                                                isClassroom(maybeClassroom) ? maybeClassroom : ""
                                        );
                                        courses.add(course);
                                    }
                                }
                            }
                        }
                    }

                }
            }
        }

        return courses;
    }


    private String translateDayToEnglish(String turkishDay) {
        return switch (turkishDay) {
            case "Pazartesi" -> "MONDAY";
            case "Salı" -> "TUESDAY";
            case "Çarşamba" -> "WEDNESDAY";
            case "Perşembe" -> "THURSDAY";
            case "Cuma" -> "FRIDAY";
            default -> turkishDay.toUpperCase();
        };
    }

    private boolean isCourseCode(String text) {
        return text.matches("[A-Z]{2,}[0-9]{3,}"); // ENG102, PHOT222, CENG214 vb.
    }

    private boolean isClassroom(String text) {
        return text.matches("D\\d|L\\d"); // D1, D2, L1
    }

    private boolean isInstructor(String text) {
        return text.matches("[A-Z]{2,4}"); // SA, TK, BGA, OG vb.
    }
}
