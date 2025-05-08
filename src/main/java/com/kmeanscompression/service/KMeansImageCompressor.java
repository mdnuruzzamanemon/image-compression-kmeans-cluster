package com.kmeanscompression.service;

import com.kmeanscompression.model.Pixel;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class KMeansImageCompressor {

    private final Random random = new Random();

    public byte[] compressImage(byte[] imageData, int k) throws IOException {
        // Read the original image
        BufferedImage originalImage = ImageIO.read(new ByteArrayInputStream(imageData));
        int width = originalImage.getWidth();
        int height = originalImage.getHeight();

        // Extract pixels
        List<Pixel> pixels = new ArrayList<>();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int rgb = originalImage.getRGB(x, y);
                int r = (rgb >> 16) & 0xFF;
                int g = (rgb >> 8) & 0xFF;
                int b = rgb & 0xFF;
                pixels.add(new Pixel(r, g, b, x, y));
            }
        }

        // Initialize centroids
        List<Pixel> centroids = initializeCentroids(pixels, k);
        
        // Run k-means clustering
        boolean changed = true;
        int iteration = 0;
        int maxIterations = 100;

        while (changed && iteration < maxIterations) {
            // Assign each pixel to the nearest centroid
            changed = assignClusters(pixels, centroids);
            
            // Update centroids
            updateCentroids(pixels, centroids, k);
            
            iteration++;
        }

        // Create compressed image by replacing each pixel with its centroid color
        BufferedImage compressedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (Pixel pixel : pixels) {
            Pixel centroid = centroids.get(pixel.getCluster());
            int rgb = (centroid.getR() << 16) | (centroid.getG() << 8) | centroid.getB();
            compressedImage.setRGB(pixel.getX(), pixel.getY(), rgb);
        }

        // Convert to byte array
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(compressedImage, "png", outputStream);
        return outputStream.toByteArray();
    }

    private List<Pixel> initializeCentroids(List<Pixel> pixels, int k) {
        List<Pixel> centroids = new ArrayList<>();
        
        // Initialize centroids randomly from the pixels
        for (int i = 0; i < k; i++) {
            int index = random.nextInt(pixels.size());
            Pixel pixel = pixels.get(index);
            centroids.add(new Pixel(pixel.getR(), pixel.getG(), pixel.getB(), 0, 0));
        }
        
        return centroids;
    }

    private boolean assignClusters(List<Pixel> pixels, List<Pixel> centroids) {
        boolean changed = false;
        
        for (Pixel pixel : pixels) {
            int oldCluster = pixel.getCluster();
            double minDistance = Double.MAX_VALUE;
            int closestCluster = 0;
            
            for (int i = 0; i < centroids.size(); i++) {
                double distance = pixel.distance(centroids.get(i));
                if (distance < minDistance) {
                    minDistance = distance;
                    closestCluster = i;
                }
            }
            
            if (oldCluster != closestCluster) {
                pixel.setCluster(closestCluster);
                changed = true;
            }
        }
        
        return changed;
    }

    private void updateCentroids(List<Pixel> pixels, List<Pixel> centroids, int k) {
        int[] counts = new int[k];
        int[] totalR = new int[k];
        int[] totalG = new int[k];
        int[] totalB = new int[k];
        
        // Accumulate RGB values for each cluster
        for (Pixel pixel : pixels) {
            int cluster = pixel.getCluster();
            counts[cluster]++;
            totalR[cluster] += pixel.getR();
            totalG[cluster] += pixel.getG();
            totalB[cluster] += pixel.getB();
        }
        
        // Calculate new centroids
        for (int i = 0; i < k; i++) {
            if (counts[i] > 0) {
                centroids.get(i).setR(totalR[i] / counts[i]);
                centroids.get(i).setG(totalG[i] / counts[i]);
                centroids.get(i).setB(totalB[i] / counts[i]);
            }
        }
    }
} 