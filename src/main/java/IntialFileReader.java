import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Manages the intial txt input file provided that contains all the image files for clustering and comparison.
 * Provides Functionality for reading the input file and creating a list of all the image filenames.
 */
public class IntialFileReader {

    /**
     * Takes a string containing a filename in and validates that filname.
     * Reads the txt file and creates a list to store each individual image filename within the file.
     * 
     * @param inputFileName String that contains the filename from the arguments provided.
     * @return The list of image filenames that were contained in the txt input file.
     * @throws FileNotFoundException In case the input filename can not be found.
     * @throws InvalidImageFormatException In case when the image object is created the image file is not valid.
     */
    public static List<Image> readImagesFromFile(String inputFileName) throws FileNotFoundException, InvalidImageFormatException {
        //make sure string provided is a valid file path
        File inputFile = new File(inputFileName);
        if(!inputFile.exists()) {
            throw new FileNotFoundException("Error input file does not exist: " + inputFileName);
        }

        //create an array to store all the image files provided in the intial txt file
        List<Image> images = new ArrayList<>();
        try(Scanner scanner = new Scanner(inputFile)) {
            while(scanner.hasNext()) {
                String filePath = scanner.next();
                Image image = new Image(new File(filePath));
                images.add(image);
            }
        }
        return images;
    }
}

