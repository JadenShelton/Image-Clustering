# 🖼️ Image Clustering Project

## 📝 Overview

> A Java application that processes PGM (Portable Gray Map) images and groups them into clusters based on various similarity measures. The project supports both standard clustering modes and perceptron-based clustering for more advanced image analysis.

---

## ✨ Features

- **Multiple Similarity Measures** for image comparison
- **Dual Mode Support**
  - Training-based (perceptron)
  - Direct clustering
- **Flexible K-value** clustering
- **PGM Image Processing**
- **Robust Error Handling**

---

## Prerequisites

- Java Development Kit (JDK) 8 or higher
- Java Runtime Environment (JRE)
- PGM format image files for input

---

## 📥 Installation

1. **Clone the repository:**
   ```bash
   git clone https://github.com/your-username/image-clustering-project.git
   ```

2. **Navigate to project directory:**
   ```bash
   cd image-clustering-project
   ```

3. **Compile the Java files:**
   ```bash
   javac CS_214_Project.java
   ```

---

## 🚀 Usage

### Standard Clustering Mode

```bash
java CS_214_Project <Test_Set> <K> <SimilarityMeasure>
```

| Parameter | Description |
|-----------|-------------|
| `Test_Set` | Path to the file containing images to be clustered |
| `K` | Number of clusters to create |
| `SimilarityMeasure` | Integer (1-4) for similarity measure |

**Example:**
```bash
java CS_214_Project test_images.txt 3 2
```

### Perceptron Mode

```bash
java CS_214_Project <Training_Set> <Test_Set> <K>
```

| Parameter | Description |
|-----------|-------------|
| `Training_Set` | Path to training images file |
| `Test_Set` | Path to images for clustering |
| `K` | Number of clusters to create |

**Example:**
```bash
java CS_214_Project training_images.txt test_images.txt 3
```

---

## 📄 Input File Format

Your input files should follow this format:
```
/path/to/image1.pgm
/path/to/image2.pgm
/path/to/image3.pgm
```

---

## ⚠️ Error Handling

The program handles various errors including:

- ❌ Invalid number of arguments
- ❌ Invalid K values
- ❌ File not found exceptions
- ❌ Invalid image format
- ❌ Invalid similarity measure values

---

## 📊 Output

The program outputs clusters to standard output:
```
Cluster 1: [image1.pgm, image4.pgm, image7.pgm]
Cluster 2: [image2.pgm, image5.pgm]
Cluster 3: [image3.pgm, image6.pgm]
```

---

## 📐 Similarity Measures

| Measure | Description |
|---------|-------------|
| 1 | Utilizes the pairwise minimum comparison method to compare two clusters |
| 2 | Utilizes pairwise minimum comparisons between quarter histograms of two clusters |
| 3 | Utilizes a squared difference comparison between two clusters |
| 4 | Utilizes pairwise minimum comparisons between histograms split into ninths of two clusters |
| 5 | Perceptron-based similarity |
