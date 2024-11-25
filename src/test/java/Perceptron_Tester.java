import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

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

}