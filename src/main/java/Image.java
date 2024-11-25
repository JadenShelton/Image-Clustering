import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Represents a Image given an Image file. 
 * Maintains all the relevent data to that specific image for comparisons.
 * Provides the functionality for validating an image file, storing individual pixel values,
 * creating and storing different histogram variations of the image. Also provides methods used
 * in comparisons.
 * 
 */
public class Image {
    private File file; 
    private int[][] pixels; //need to store image as pixel values in a 2D array for quarter histograms
    private double[] histogram;
    private Histogram normHistogram;
    private Histogram[] quarterHistograms;
    private int classNumber;
    private Histogram[] ninthsHistograms;

    /**
     * Constructs a new Image object given a image file.
     * 
     * Checks that files exists before proceeding. Then validates the Image and creates Histogram representations of the Image,
     * also assumes the image filename contains a ground truth value for what group it should eventually be clustered in.
     * 
     * @param file The image file for the Image object
     * @throws FileNotFoundException Throws exception if the given image file name can not be found.
     * @throws InvalidImageFormatException Throws exception if the image file given is not the right format for comparions. (Must be PGM)
     */
    public Image(File file) throws FileNotFoundException, InvalidImageFormatException {
        this.file = file;
        if(!file.exists()) {
            throw new FileNotFoundException("File does not exist: " + file.getName());
        }
        
        //validate and compute histogram and norm histogram
        validateImage();
        normHistogram = new Histogram(histogram).normHist();
        createQuarterHistograms();
        createNinthsHistograms();
        classNumber = extractClassNumber();
    }

    /**
     * Validates the file that it is a valid image. Checks contents, format, width, height, and pixel values.
     * Also stores pixel values in a 2d array while checking them for histogram creation. Creates a base histogram once all validation is completed.
     * 
     * @throws InvalidImageFormatException Throws exception if the file fails any of the validation checks.
     * @throws FileNotFoundException Throws exception if the given file is not able to be found.
     */
    private void validateImage() throws InvalidImageFormatException, FileNotFoundException {
        try (Scanner scanner = new Scanner(file)) {
            //Make sure file is not empty and that it is a pgm file type
            if (!scanner.hasNext() || !scanner.next().equals("P2")) {
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
            
            //corrupted image check
            int totalPixels = 0;
            for(int[] pixelRow : pixels) {
                totalPixels += pixelRow.length;
            }
            if(totalPixels != 128*128){
                throw new InvalidImageFormatException("Corrupted Image invalid number of pixels in file: " + file.getName());
            }

            //all validation has passed so begin computing image histogram
            createHistogram();
        }
    }

    /**
     * Creates a histogram by iterating through all the pixel values and sorting them into values in a 64 bin histogram.
     */
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

    /**
     * Creates 2D array to create 4 histograms to split the image into 4 seperate quarters for a better comparison methods.
     * 
     * Iterates through entire images pixel values and then sorts them into the approriate bins based on location of pixel, creates 4 normalized histograms.
     */
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

    /**
     * Creates 2D array to create 9 seperate histograts to split the image inot ninths for another better image comparison method. 
     * 
     * Iterates through the entire array of pixels and divides the image up into 9 almost equal sections for 9 seperate normalized histograms.
     */
    private void createNinthsHistograms() {
        double[][] nineBins = new double[9][64]; //9 seperate groups of 64 bins to split image into ninths

        //loop throug the entire image asigning each pixel to the correct ninth and bin in that ninth
        for(int x = 0; x < 128; ++x) {
            for(int y = 0; y < 128; ++y) {
                int pixelValue = pixels[x][y];
                int bin = pixelValue / 4;

                //for ninths histogram we determine what ninth histogram the pixel belongs to as well
                if(x < 43 && y < 43) { //bottom left
                    nineBins[0][bin]++;
                } else if(x >= 43 && x < 86 && y < 43) { //bottom middle
                    nineBins[1][bin]++;
                } else if(x >= 86 && y < 43) {  //bottom right
                    nineBins[2][bin]++;
                } else if(x < 43 && y >= 43 && y < 86) { //middle left
                    nineBins[3][bin]++;
                } else if(x >= 43 && x < 86 && y >= 43 && y < 86) { //middle middle
                    nineBins[4][bin]++;
                } else if(x >= 86 && y >= 43 && y < 86) {  //middle right
                    nineBins[5][bin]++;
                } else if(x < 43 && y >= 86) { //middle left
                    nineBins[6][bin]++;
                } else if(x >= 43 && x < 86 && y >= 86) { //middle middle
                    nineBins[7][bin]++;
                } else {  //top right
                    nineBins[8][bin]++;
                }
            }
        }

        ninthsHistograms = new Histogram[9]; //create histogram objects for each ninth normalize them and add the to the collection of ninths histograms stored by image
        for(int i = 0; i < 9; ++i) {
            ninthsHistograms[i] = new Histogram(nineBins[i]).normHist();
        }
    }

    /**
     * Calculates the squared difference between the image and a given image. For the InvSpuareDiffSimilarity compariosn method.
     * 
     * Iterates through the pixel values of both images and subtracts the pixel value of this image from the other image and sums up those differences squared.
     * @param other The other image for difference evaluation.
     * @return Double value that is the sum of the differences between the the pixel values squared.
     */
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

    /**
     * Retrieves the pixel value of the image at the given coordinates.
     * 
     * @param x X location of pixel
     * @param y Y location of pixel
     * @return The int pixel value at the given coordinates.
     * @throws IllegalArgumentException Throws exception if the coordiantes are out of bounds.
     */
    public int getPixelValue(int x, int y) {
        if(x < 0 || x >= 128 || y < 0 || y >= 128) {
            throw new IllegalArgumentException("Coordinates out of bounds!");
        }
        return pixels[x][y];
    }

    /**
     * Finds the number representing the class number of the image given that the file name is in a specific format.
     * 
     * @return The int that represents the class number of a file.
     */
    private int extractClassNumber() {
        String truthValue = file.getName().substring(5, file.getName().indexOf("_"));
        return Integer.parseInt(truthValue);
    }

    /**
     * Retrieves the class number.
     * 
     * @return The int value representing the class number of the image.
     */
    public int getClassNumber() {
        return classNumber;
    }

    /**
     * Retrieves the base Histogram of this image.
     * 
     * @return The double array that contains the data for the Histogram of the image.
     */
    public double[] getHistogram() {
        return histogram;
    }

    /**
     * Retrieves the normalized histogram of this image.
     * 
     * @return The Histogram object that contains the data for the normalized Histogram of the image.
     */
    public Histogram getNormHistogram() {
        return normHistogram;
    }

    /**
     * Retrieves the array of Histogram objects that contains the data for four quarter histograms of the image.
     * 
     * @return The Histogram array containing the data for four quarter histograms of the image.
     */
    public Histogram[] getQuarterHistograms() {
        return quarterHistograms;
    }

    /**
     * The Histogram array containing the data for nine histograms that make up the image.
     * 
     * @return The Histogram array containing the data for nine histograms that make up the image.
     */
    public Histogram[] getNinthsHistograms() {
        return ninthsHistograms;
    }

    /**
     * Retrieves the filename of the image.
     * 
     * @return The string containing the filename of the image.
     */
    public String getFileName() {
        return file.getName();
    }
}
