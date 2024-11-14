import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;


public class Perceptron_Tester {
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;
    private Perceptron perceptron;
    private static ByteArrayOutputStream errContent;


    @BeforeEach
    public void setUp() throws FileNotFoundException, InvalidImageFormatException {
        System.setOut(new PrintStream(outContent));
        perceptron = new Perceptron();
        errContent = new ByteArrayOutputStream();
        System.setErr(new PrintStream(errContent));
    }

    @AfterEach
    public void restoreStreams() {
        System.setOut(originalOut);
        System.setErr(originalErr);
    }

    //Test all the the weights in array are all intialized to zero
    @Test
    public void testIntialWeightsZero() {
        double[] testHistogram = new double[64];
        for(int i = 0; i < 64; ++i) {
            testHistogram[i] = 1.0;
        }
        assertEquals(0.0, perceptron.perceptronEvaluation(testHistogram));
    }

    //Test histogram validation, invalid histogram input
    @Test
    public void testInvalidHistogramSize() {
        double[] badHistogram = new double[8];
        assertThrows(IllegalArgumentException.class, () -> perceptron.perceptronEvaluation(badHistogram));
    }

    //Test empty image list
    @Test
    public void testEmptyImageList() {
        assertDoesNotThrow(() -> perceptron.trainPerceptron(new ArrayList<>(), 1));
    }

    //Empty input file
    @Test
    public void testEmptyInputFile() {
        String[] args = {"input_files/empty.txt", "1"};
        CS_214_Project.main(args);
        assertEquals("Error: Need at least 1 image", errContent.toString().trim());

    }

    //Non-PGM files input
    @Test
    public void testNonPGMFilesInInput() {
        String[] args = {"input_files/contains_non_pgm.txt", "1"};
        CS_214_Project.main(args);
        assertEquals("Error: Invalid file format for file: not_an_image.txt", errContent.toString().trim());

    }

    //intial file reader class
    @Test
    public void testNonexistentInputFile() {
        String nonExistentFile = "nonexistent.txt";
        assertThrows(FileNotFoundException.class, () -> IntialFileReader.readImagesFromFile(nonExistentFile));
    }

    // New tests for Perceptron class
    @Test
    public void testTrainPerceptronWithEmptyList() {
        List<Image> emptyList = new ArrayList<>();
        assertDoesNotThrow(() -> perceptron.trainPerceptron(emptyList, 1));
    }

    // Test perceptron evaluation with different values
    @Test
    public void testPerceptronEvaluationWithDifferentValues() {
        double[] testHistogram = new double[64];
        testHistogram[0] = 1.0;
        testHistogram[1] = 0.5;
        
        double result = perceptron.perceptronEvaluation(testHistogram);
        assertNotNull(result);
    }

    // Test main method with invalid arguments
    @Test
    public void testMainWithInvalidArguments() {
        String[] args = new String[]{"file.txt"}; // Missing second argument
        CS_214_Project.main(args);
        assertEquals("Error: two argumens required - <input_file> <K>", 
            errContent.toString().trim());
    }

    // Test main method with invalid class number
    @Test
    public void testMainWithInvalidClassNumber() {
        String[] args = new String[]{"file.txt", "0"};
        CS_214_Project.main(args);
        assertEquals("Error: class number must be greater than zero", 
            errContent.toString().trim());
    }

    // Test main method with non-integer class number
    @Test
    public void testMainWithNonIntegerClassNumber() {
        String[] args = new String[]{"file.txt", "abc"};
        CS_214_Project.main(args);
        assertEquals("Error: K must be an integer", errContent.toString().trim());
    }

    // Test file not found scenario
    @Test
    public void testMainWithNonexistentFile() {
        String[] args = new String[]{"nonexistent.txt", "1"};
        CS_214_Project.main(args);
        assertTrue(errContent.toString().trim()
            .startsWith("Error: Error input file does not exist:"));
    }


}   
