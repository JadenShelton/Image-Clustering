import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Image {
    private File file; 
    private int[][] pixels; //need to store image as pixel values in a 2D array for quarter histograms
    private double[] histogram;
    private Histogram normHistogram;
    private Histogram[] quarterHistograms;
    private int groundTruthValue;

    public Image(File file) throws FileNotFoundException, InvalidImageFormatException {
        this.file = file;
        if(!file.exists()) {
            throw new FileNotFoundException("File does not exist: " + file.getName());
        }
        
        //validate and compute histogram and norm histogram
        validateImage();
        normHistogram = new Histogram(histogram).normHist();
        createQuarterHistograms();
        groundTruthValue = extractGroundTruthValue();
    }

    private void validateImage() throws InvalidImageFormatException, FileNotFoundException {
        try (Scanner scanner = new Scanner(file)) {
            //Make sure file is not empty and that it is a pgm file type
            if (!scanner.hasNext() || !scanner.next().equals("P3")) {
                throw new InvalidImageFormatException("Invalid file format for file: " + file.getName());
            }

            //first three values should be width, height, and max pixel value, store these
            int width = scanner.nextInt();
            int height = scanner.nextInt();
            int maxPixelValue = scanner.nextInt();

            //Ensure image is 128 by 128 pixels
            if(width != 128 || height != 128) {
                throw new InvalidImageFormatException("Invalid width or height of the image must be 128x128: " + file.getName());
            }

            //check max pixel value is within limits
            if(maxPixelValue != 255) {
                throw new InvalidImageFormatException("Invalid max pixel value in file: " + file.getName());
            }

            //store all the pixel values in a 2D array
            pixels = new int[width][height];
            for(int x = 0; x < width; ++x) {
                for(int y = 0; y < height; ++y) {
                    if(scanner.hasNextInt()) {
                        int value = scanner.nextInt();
                        if(value < 0 || value > 255) {
                            throw new InvalidImageFormatException("Invalid pixel value in file: " + file.getName());
                        }
                        pixels[x][y] = value; //pixel value at x, y is store in array
                    } else {
                        throw new InvalidImageFormatException("Incorrect number of pixels in file: " + file.getName());
                    }
                }
            }

            //all validation has passed so begin computing image histogram
            createHistogram();
        }
    }

    //creates a basic histogram of the image
    private void createHistogram() {
        //pixel values from 0 - 255 divided into 64 bins for histogram
        this.histogram = new double[64];

        for(int x = 0; x < 128; ++x) {
            for(int y = 0; y < 128; ++y) {
                int pixelValue = pixels[x][y]; //iterate through the entire histogram getting value for each pixel
                int bin = pixelValue/4; //divide by 4 to place in proper bin
                histogram[bin]++; 
            }
        }
    }

    private void createQuarterHistograms() {
        double[][] quarterBins = new double[4][64]; //4 seperate groupd of 64 bins to split image into seperate quarters

        //loop throug the entire image asigning each pixel to the correct quarter and bin in that quarter
        for(int x = 0; x < 128; ++x) {
            for(int y = 0; y < 128; ++y) {
                int pixelValue = pixels[x][y];
                int bin = pixelValue/4;

                //for quarter histogram we determine what quarter histogram the pixel belongs to as well
                if(x < 64 && y < 64) { //first quarter
                    quarterBins[0][bin]++;
                } else if(x >= 64 && y < 64) { //second quarter
                    quarterBins[1][bin]++;
                } else if(x < 64 && y >= 64) { //third quarter
                    quarterBins[2][bin]++;
                } else { //fourth quarter
                    quarterBins[3][bin]++;
                }
            }
        }

        quarterHistograms = new Histogram[4]; //create histogram objects for each quarter normalize them and add the to the collection of quarter histograms stored by image
        for(int i = 0; i < 4; ++i) {
            quarterHistograms[i] = new Histogram(quarterBins[i]).normHist();
        }
    }

    //calculates the squared difference for InvSquareDiffSimilarity
    public double calculateSquaredDifference(Image other) {
        double sum = 0.0;

        for (int x = 0; x < 128; ++x) {
            for(int y = 0; y < 128; ++y) {
                int diff = this.getPixelValue(x, y) - other.getPixelValue(x, y);
                sum += diff * diff;
            }
        }
        return sum;
    }

    public int getPixelValue(int x, int y) {
        if(x < 0 || x >= 128 || y < 0 || y >= 128) {
            throw new IllegalArgumentException("Coordinates out of bounds!");
        }
        return pixels[x][y];
    }

    private int extractGroundTruthValue() {
        String truthValue = file.getName().substring(5, file.getName().indexOf("_"));
        return Integer.parseInt(truthValue);
    }

    public int getGroundTruthValue() {
        return groundTruthValue;
    }

    public double[] getHistogram() {
        return histogram;
    }

    public Histogram getNormHistogram() {
        return normHistogram;
    }

    public Histogram[] getQuarterHistograms() {
        return quarterHistograms;
    }

    public String getFileName() {
        return file.getName();
    }
}
