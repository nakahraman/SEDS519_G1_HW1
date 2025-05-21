package com.seds519.hw1.service;


import com.seds519.hw1.model.Course;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


import java.util.*;

@Service
public class HtmlScheduleRenderer {

    private static final List<String> DAYS = List.of("MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY");

    public String render(List<Course> courses) {
        // Saat -> Gün -> Liste
        Map<String, Map<String, List<Course>>> grouped = courses.stream()
                .collect(Collectors.groupingBy(
                        Course::getTimeSlot,
                        LinkedHashMap::new,
                        Collectors.groupingBy(Course::getDay, LinkedHashMap::new, Collectors.toList())
                ));

        StringBuilder html = new StringBuilder();
        html.append("<html><head><meta charset='UTF-8'><title>Weekly Schedule</title><style>");
        html.append("table { border-collapse: collapse; width: 100%; }");
        html.append("th, td { border: 1px solid #ccc; padding: 8px; text-align: center; vertical-align: top; }");
        html.append("th { background-color: #2c3e50; color: white; }");
        html.append(".odd-row { background-color: #f9f9f9; }"); // açık gri
        html.append(".even-row { background-color: #ffffff; }"); // beyaz
        html.append("</style></head><body>");
        html.append("<h2>Weekly Class Schedule</h2>");

        html.append("<table>");
        html.append("<tr><th>Time</th>");
        for (String day : DAYS) {
            html.append("<th>").append(day).append("</th>");
        }
        html.append("</tr>");

        int rowIndex = 0;
        for (String time : grouped.keySet()) {
            String rowClass = (rowIndex % 2 == 0) ? "even-row" : "odd-row";
            html.append("<tr class='").append(rowClass).append("'>");
            html.append("<td><strong>").append(time).append("</strong></td>");
            for (String day : DAYS) {
                html.append("<td>");
                List<Course> courseList = grouped.get(time).getOrDefault(day, List.of());

           /*     // PHOT ile başlayan dersleri filtrele
                courseList = courseList.stream()
                        .filter(c -> !c.getName().toUpperCase().startsWith("PHOT"))
                        .collect(Collectors.toList());

            */
                for (Course c : courseList) {
                    html.append("<div style='margin-bottom:6px;'>")
                            .append("<strong>").append(c.getName()).append("</strong><br>");

                    boolean hasInstructor = c.getInstructor() != null && !c.getInstructor().isBlank();
                    boolean hasClassroom = c.getClassroom() != null && !c.getClassroom().isBlank();

                    if (hasInstructor || hasClassroom) {
                        if (hasInstructor) html.append(c.getInstructor());
                        if (hasInstructor && hasClassroom) html.append(" - ");
                        if (hasClassroom) html.append(c.getClassroom());
                    }

                    html.append("</div>");
                }
                html.append("</td>");
            }
            html.append("</tr>");
            rowIndex++;
        }

        html.append("</table></body></html>");
        return html.toString();
    }

}
