package com.kmeanscompression.service;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import org.springframework.stereotype.Service;

@Service
public class CompressionAnalyzer {

    private final KMeansImageCompressor compressor;

    public CompressionAnalyzer(KMeansImageCompressor compressor) {
        this.compressor = compressor;
    }

    public Map<String, Object> analyzeCompression(byte[] imageData, int[] kValues) throws IOException {
        Map<String, Object> results = new HashMap<>();
        
        // Original image metrics
        BufferedImage originalImage = ImageIO.read(new ByteArrayInputStream(imageData));
        int originalSize = imageData.length;
        results.put("originalSize", originalSize);
        results.put("originalDimensions", Map.of(
            "width", originalImage.getWidth(),
            "height", originalImage.getHeight()
        ));
        
        // Compression results for different k values
        List<Map<String, Object>> compressionResults = new ArrayList<>();
        
        for (int k : kValues) {
            long startTime = System.currentTimeMillis();
            byte[] compressedData = compressor.compressImage(imageData, k);
            long endTime = System.currentTimeMillis();
            
            BufferedImage compressedImage = ImageIO.read(new ByteArrayInputStream(compressedData));
            
            // Calculate compression ratio
            double compressionRatio = (double) originalSize / compressedData.length;
            
            // Calculate PSNR (Peak Signal-to-Noise Ratio) - higher is better
            double psnr = calculatePSNR(originalImage, compressedImage);
            
            // Calculate unique colors
            int originalColors = countUniqueColors(originalImage);
            int compressedColors = countUniqueColors(compressedImage);
            
            Map<String, Object> result = new HashMap<>();
            result.put("k", k);
            result.put("compressedSize", compressedData.length);
            result.put("compressionRatio", compressionRatio);
            result.put("processingTimeMs", endTime - startTime);
            result.put("psnr", psnr);
            result.put("originalColors", originalColors);
            result.put("compressedColors", compressedColors);
            result.put("colorReduction", (double) (originalColors - compressedColors) / originalColors * 100);
            
            compressionResults.add(result);
        }
        
        results.put("compressionResults", compressionResults);
        return results;
    }
    
    private double calculatePSNR(BufferedImage original, BufferedImage compressed) {
        int width = original.getWidth();
        int height = original.getHeight();
        
        long mse = 0;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int rgbOrig = original.getRGB(x, y);
                int rgbComp = compressed.getRGB(x, y);
                
                int rOrig = (rgbOrig >> 16) & 0xFF;
                int gOrig = (rgbOrig >> 8) & 0xFF;
                int bOrig = rgbOrig & 0xFF;
                
                int rComp = (rgbComp >> 16) & 0xFF;
                int gComp = (rgbComp >> 8) & 0xFF;
                int bComp = rgbComp & 0xFF;
                
                long pixelMse = (long) Math.pow(rOrig - rComp, 2) + 
                               (long) Math.pow(gOrig - gComp, 2) + 
                               (long) Math.pow(bOrig - bComp, 2);
                mse += pixelMse;
            }
        }
        
        mse /= (width * height * 3);
        if (mse == 0) return Double.POSITIVE_INFINITY; // Perfect match
        
        double psnr = 10 * Math.log10(Math.pow(255, 2) / mse);
        return psnr;
    }
    
    private int countUniqueColors(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        Map<Integer, Boolean> uniqueColors = new HashMap<>();
        
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                uniqueColors.put(image.getRGB(x, y), true);
            }
        }
        
        return uniqueColors.size();
    }
} 