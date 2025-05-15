# K-Means Image Compression Analysis

This project demonstrates image compression using the K-means clustering algorithm. The implementation provides a web interface for compressing images and analyzing the compression results.

## How K-means Image Compression Works

K-means image compression reduces the number of colors in an image by clustering similar colors together and replacing each color with its cluster centroid.

### Algorithm Overview

1. **Color Extraction**: Each pixel in the image is represented as a point in 3D RGB color space.
2. **K-means Clustering**: The algorithm clusters the colors into 'k' groups by:
   - Randomly selecting 'k' initial centroids (colors)
   - Assigning each pixel to the nearest centroid
   - Recalculating centroids as the average of all pixels in each cluster
   - Repeating until convergence or a maximum number of iterations
3. **Color Replacement**: Each pixel's color is replaced with the color of its cluster centroid.

### Advantages of K-means Compression

- **Significant Color Reduction**: Can reduce thousands of colors to a small palette (e.g., 8, 16, 32 colors)
- **Content-Aware**: Creates a color palette optimized for the specific image
- **Adjustable Quality**: 'k' parameter allows balancing between compression ratio and image quality

## Analysis Metrics

The project provides several metrics to evaluate compression performance:

### Compression Ratio

The ratio of original file size to compressed file size. Higher values indicate better compression.

### PSNR (Peak Signal-to-Noise Ratio)

Measures the quality of the compressed image compared to the original. Higher values (measured in dB) indicate better quality.

### Processing Time

Time required to compress the image, which generally increases with higher 'k' values.

### Color Reduction

Percentage of unique colors removed from the original image.

## Analysis Results and Patterns

### Effect of K Value

As the 'k' value increases:

- **Compression Ratio**: Typically decreases (less compression)
- **PSNR**: Typically increases (better quality)
- **Processing Time**: Generally increases
- **Color Reduction**: Decreases

### Optimal K Value Selection

The optimal 'k' value depends on the specific requirements:

- For maximum compression: Lower 'k' values (8-16)
- For better quality with reasonable compression: Medium 'k' values (32-64)
- For high-quality reproduction: Higher 'k' values (128+)

### Image Type Impact

The effectiveness of K-means compression varies based on image content:

- **Photos with smooth gradients**: Benefit from higher 'k' values
- **Graphics with flat colors**: Can achieve good results with lower 'k' values
- **High-contrast images**: Often need higher 'k' values to preserve details

## How to Use This Tool

1. Upload an image using the form
2. Specify comma-separated 'k' values to analyze (e.g., 8,16,32,64,128)
3. Click "Run Analysis"
4. View the compression results including:
   - Interactive charts comparing metrics across different 'k' values
   - Detailed results table
   - Original image information

## Implementation Details

The project is implemented in Java using the Spring Boot framework:

- **K-means Clustering**: Custom implementation in Java
- **Web Interface**: Spring Boot with Thymeleaf
- **Visualization**: Chart.js for interactive graphs

## Getting Started

### Prerequisites

- Java 11 or higher
- Maven

### Building and Running

```bash
mvn clean install
mvn spring-boot:run
```

Access the application at: http://localhost:8080

## Future Improvements

Potential enhancements for this project:

- Side-by-side visual comparison of compressed images
- Batch processing capabilities
- Additional clustering methods (e.g., Median Cut, Octree)
- Alternative distance metrics (e.g., LAB color space)
