# K-Means Image Compression Application

This web application uses K-Means clustering to compress images by reducing the number of colors. Each pixel's RGB value is treated as a data point in 3D space, and similar colors are grouped into clusters. This reduces file size while maintaining visual quality.

## Features

- Upload any image for compression
- Control the number of colors (clusters) used in the compressed image
- View original and compressed images side by side
- Download both original and compressed images
- See compression details including compression ratio

## Requirements

- Java 11 or higher
- Maven

## How to Run

### Option 1: Using Maven directly

1. Navigate to the project directory
```
cd image-compression-app
```

2. Build the application with Maven
```
mvn clean package
```

3. Run the application
```
java -jar target/image-compression-app-0.0.1-SNAPSHOT.jar
```

### Option 2: Using Maven Spring Boot plugin

1. Navigate to the project directory
```
cd image-compression-app
```

2. Run with Maven
```
mvn spring-boot:run
```

## How to Use

1. Open your web browser and navigate to `http://localhost:8080`
2. Select an image file from your computer
3. Adjust the number of colors (clusters) using the slider
4. Click "Compress Image"
5. View the comparison between original and compressed images
6. Download either image using the respective download buttons
7. To compress another image, click "Compress Another Image"

## Implementation Details

The application uses:
- Spring Boot for the web framework
- Thymeleaf for HTML templates
- K-Means clustering algorithm to group similar colors
- Java AWT/ImageIO for image processing

## Technical Note

The compression works by reducing the color palette of the image. While the compressed image may look similar to the original, it will have significantly fewer unique colors, resulting in a smaller file size when saved in formats that benefit from color reduction. 