package com.kmeanscompression.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

    @GetMapping("/analysis")
    public String analysisPage() {
        return "analysis";
    }
} 