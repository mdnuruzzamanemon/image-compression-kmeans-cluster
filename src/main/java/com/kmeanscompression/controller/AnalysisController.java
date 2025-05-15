package com.kmeanscompression.controller;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.kmeanscompression.service.CompressionAnalyzer;

@RestController
@RequestMapping("/api/analysis")
public class AnalysisController {

    private final CompressionAnalyzer analyzer;

    @Autowired
    public AnalysisController(CompressionAnalyzer analyzer) {
        this.analyzer = analyzer;
    }

    @PostMapping("/compress")
    public ResponseEntity<?> analyzeCompression(
            @RequestParam("image") MultipartFile imageFile,
            @RequestParam(value = "kValues", defaultValue = "8,16,32,64,128") String kValuesStr) {
        
        try {
            // Parse k values
            int[] kValues = Arrays.stream(kValuesStr.split(","))
                                 .map(String::trim)
                                 .mapToInt(Integer::parseInt)
                                 .toArray();
            
            // Run analysis
            Map<String, Object> results = analyzer.analyzeCompression(
                imageFile.getBytes(), 
                kValues
            );
            
            return ResponseEntity.ok(results);
        } catch (IOException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to process image: " + e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
} 