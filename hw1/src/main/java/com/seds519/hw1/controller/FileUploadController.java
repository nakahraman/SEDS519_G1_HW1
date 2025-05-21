package com.seds519.hw1.controller;

import com.seds519.hw1.model.Course;
import com.seds519.hw1.parser.FileParser;
import com.seds519.hw1.service.FileParserContext;
import com.seds519.hw1.service.HtmlScheduleRenderer;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api")
public class FileUploadController {

    private final FileParserContext parserContext;
    private final HtmlScheduleRenderer renderer;

    @Autowired
    public FileUploadController(FileParserContext parserContext, HtmlScheduleRenderer renderer) {
        this.parserContext = parserContext;
        this.renderer = renderer;
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return ResponseEntity.badRequest().body("File is missing.");
        }

        try {
            FileParser parser = parserContext.getParser(file);
            List<Course> courses = parser.parse(file);
            String html = renderer.render(courses);
            return ResponseEntity.ok(html);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error while processing file: " + e.getMessage());
        }
    }
}