import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class IntialFileReader {

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

