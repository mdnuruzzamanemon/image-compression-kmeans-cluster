# 🖼️ K-Means Image Compression Application

This web application compresses images using the **K-Means clustering algorithm** by reducing the number of unique colors in an image. Each pixel's RGB value is treated as a point in 3D space. By clustering similar colors and reassigning pixel values based on their cluster centroid, we achieve **smaller image file sizes** while preserving **visual quality**.

---

## 🚀 Features

- 📤 Upload any image (JPG, PNG, etc.)
- 🎛️ Select the number of colors (clusters) for compression
- 🖼️ View original and compressed images side-by-side
- 💾 Download both versions
- 📊 See compression ratio and size comparison
- 🔁 Option to compress another image

---

## 🛠️ Requirements

Before you run the application, make sure you have:

- **Java JDK 11** or higher
- **Apache Maven 3.6+**
- A modern web browser (Chrome, Firefox, Edge, etc.)

---

## 📥 Installation Guide

### 🔧 Step 1: Install Java

#### 📌 Windows / macOS / Linux

1. Download Java JDK 11 or later from:

   - [Oracle JDK](https://www.oracle.com/java/technologies/javase-downloads.html)
   - [OpenJDK (free)](https://jdk.java.net/)

2. Install it following the instructions for your OS.

3. Verify Java installation:

```bash
java -version
```

Expected output:

```
java version "11.x.x"
```

---

### 🔧 Step 2: Install Maven

1. Download Maven from the official site:

   - [Maven Download](https://maven.apache.org/download.cgi)

2. Extract it and add the `bin/` directory to your system's `PATH`.

3. Verify Maven installation:

```bash
mvn -v
```

Expected output:

```
Apache Maven 3.x.x
```

---

## ⚙️ Running the Application

You can run the app in two ways:

### ✅ Option 1: Package and Run as JAR

```bash
cd image-compression-app
mvn clean package
java -jar target/image-compression-app-0.0.1-SNAPSHOT.jar
```

### ✅ Option 2: Run Directly with Spring Boot

```bash
cd image-compression-app
mvn spring-boot:run
```

Once started, open your browser and go to:

```
http://localhost:8080
```

---

## 🧑‍💻 How to Use

1. Upload an image file from your computer.
2. Select the number of color clusters using the slider.
3. Click **Compress Image**.
4. Compare the original and compressed images displayed side by side.
5. Download the images using the respective **Download** buttons.
6. Click **Compress Another Image** to repeat the process.

---

## 💡 How It Works (Technical Details)

- Each pixel in the image is treated as a data point in **RGB color space (3D)**.
- The **K-Means algorithm** groups these pixels into _K_ clusters.
- Each cluster is assigned a centroid (mean RGB value).
- Pixels are recolored based on their cluster's centroid.
- The result is a visually similar image using only _K_ colors.

### 🧱 Tech Stack

| Component         | Technology                |
| ----------------- | ------------------------- |
| Backend           | Java, Spring Boot         |
| Frontend          | Thymeleaf (HTML)          |
| Image Processing  | Java AWT, ImageIO         |
| Compression Logic | Custom K-Means Clustering |

---

## 📊 Compression Benefit

- The fewer the clusters, the fewer the colors → smaller file size.
- Compression is **lossy** but optimized for visual similarity.
- Especially effective on images with large areas of similar color.

---

## 📎 Resources

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [K-Means Clustering – Wikipedia](https://en.wikipedia.org/wiki/K-means_clustering)
- [Apache Maven](https://maven.apache.org/)
- [Java ImageIO API](https://docs.oracle.com/javase/8/docs/api/javax/imageio/package-summary.html)

---

## 📄 License

This project is licensed under the **MIT License**. See the [LICENSE](LICENSE) file for details.

---

## 🙋‍♂️ Support

If you encounter any issues or want to contribute, feel free to create an issue or submit a pull request. Contributions are welcome!

---

## 🎯 Future Improvements

- Allow user-defined image formats for output
- Support bulk image compression
- Add real-time progress bar for large images
- Enable mobile responsive UI
