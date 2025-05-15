package com.kmeanscompression.controller;

import java.io.IOException;
import java.util.Base64;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.kmeanscompression.service.KMeansImageCompressor;

@Controller
public class ImageCompressionController {

    private final KMeansImageCompressor compressor;

    @Autowired
    public ImageCompressionController(KMeansImageCompressor compressor) {
        this.compressor = compressor;
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }
    
    @GetMapping("/developers")
    public String developers() {
        return "developers";
    }
    
    @GetMapping("/analysis-redirect")
    public String redirectToAnalysis() {
        return "redirect:/analysis";
    }

    @PostMapping("/compress")
    public String compressImage(
            @RequestParam("image") MultipartFile file,
            @RequestParam("clusters") int clusters,
            Model model,
            HttpSession session) throws IOException {
        
        if (file.isEmpty()) {
            model.addAttribute("error", "Please select an image file");
            return "index";
        }

        // Store original image data in session
        byte[] originalImageBytes = file.getBytes();
        session.setAttribute("originalImage", originalImageBytes);
        session.setAttribute("originalFileName", file.getOriginalFilename());
        
        // Compress the image
        byte[] compressedImageBytes = compressor.compressImage(originalImageBytes, clusters);
        session.setAttribute("compressedImage", compressedImageBytes);
        
        // Convert to Base64 for displaying in HTML
        String originalBase64 = Base64.getEncoder().encodeToString(originalImageBytes);
        String compressedBase64 = Base64.getEncoder().encodeToString(compressedImageBytes);
        
        model.addAttribute("originalImage", originalBase64);
        model.addAttribute("compressedImage", compressedBase64);
        model.addAttribute("clusters", clusters);
        model.addAttribute("compressionRatio", 
            String.format("%.2f", (double) originalImageBytes.length / compressedImageBytes.length));
        
        return "result";
    }

    @GetMapping("/download/original")
    public ResponseEntity<byte[]> downloadOriginal(HttpSession session) {
        byte[] imageBytes = (byte[]) session.getAttribute("originalImage");
        String fileName = (String) session.getAttribute("originalFileName");
        
        return prepareDownloadResponse(imageBytes, fileName);
    }

    @GetMapping("/download/compressed")
    public ResponseEntity<byte[]> downloadCompressed(HttpSession session) {
        byte[] imageBytes = (byte[]) session.getAttribute("compressedImage");
        String originalFileName = (String) session.getAttribute("originalFileName");
        
        // Add a suffix to indicate compressed version
        String fileName = originalFileName.substring(0, originalFileName.lastIndexOf('.')) 
            + "_compressed.png";
        
        return prepareDownloadResponse(imageBytes, fileName);
    }

    private ResponseEntity<byte[]> prepareDownloadResponse(byte[] imageBytes, String fileName) {
        if (imageBytes == null) {
            return ResponseEntity.notFound().build();
        }
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", fileName);
        
        return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
    }
} 