package com.seds519.hw1.parser;

import com.seds519.hw1.model.Course;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileParser {
    List<Course> parse(MultipartFile file) throws Exception;
}
