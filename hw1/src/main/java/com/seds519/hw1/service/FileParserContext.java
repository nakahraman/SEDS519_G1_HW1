package com.seds519.hw1.service;


import com.seds519.hw1.parser.FileParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Component
public class FileParserContext {

    private final Map<String, FileParser> parserMap;

    @Autowired
    public FileParserContext(Map<String, FileParser> parserMap) {
        this.parserMap = parserMap;
    }

    public FileParser getParser(MultipartFile file) {
        String filename = file.getOriginalFilename();
        if (filename == null || filename.lastIndexOf('.') == -1) {
            throw new IllegalArgumentException("Invalid file name: " + filename);
        }

        String extension = filename.substring(filename.lastIndexOf('.') + 1).toLowerCase();

        return switch (extension) {
            case "xlsx", "xls" -> parserMap.get("excel");
            case "pdf" -> parserMap.get("pdf");
            default -> throw new IllegalArgumentException("Unsupported file type: " + extension);
        };
    }
}
