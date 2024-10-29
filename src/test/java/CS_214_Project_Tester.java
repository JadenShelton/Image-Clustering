import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;

import java.io.*;

//import javax.crypto.BadPaddingException;

public class CS_214_Project_Tester {
    // Streams to capture standard output and error
    private static ByteArrayOutputStream outContent;
    private static ByteArrayOutputStream errContent;
    private static final PrintStream originalOut = System.out;
    private static final PrintStream originalErr = System.err;

    @BeforeEach
    public void setUpStreams() {
        outContent = new ByteArrayOutputStream();
        errContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    @AfterEach
    public void restoreStreams() {
        System.setOut(originalOut);
        System.setErr(originalErr);
    }

    //Too few images for clustering
    @Test
    public void testTooFewImagesForClustering() {
        String[] args = {"input_files/only_one_image.txt", "3", "1"};
        CS_214_Project.main(args);
        assertEquals("Error: K can't be greater than the number of images", errContent.toString().trim());
    }

    //Empty input file
    @Test
    public void testEmptyInputFile() {
        String[] args = {"input_files/empty.txt", "3", "1"};
        CS_214_Project.main(args);
        assertEquals("Error: K can't be greater than the number of images", errContent.toString().trim());

    }

    //Non-PGM files input
    @Test
    public void testNonPGMFilesInInput() {
        String[] args = {"input_files/contains_non_pgm.txt", "3", "1"};
        CS_214_Project.main(args);
        assertEquals("Error: Invalid file format for file: not_an_image.txt", errContent.toString().trim());

    }

    //input file does not exist
    @Test
    public void testInputFileDeosNotExist() {
        String[] args = {"input_files/doesnt_exist.txt", "3", "1"};
        CS_214_Project.main(args);
        assertEquals("Error: Error input file does not exist: input_files/doesnt_exist.txt", errContent.toString().trim());
    }

    //invalid file format
    @Test
    public void testInvalidFileFormat() {
        String[] args = {"input_files/invalidformat.txt", "3", "1"};
        CS_214_Project.main(args);
        assertTrue(errContent.toString().contains("Error: Invalid pixel value in file: invalid_image.pgm"));

    }

    //invalid value for k
    @Test
    public void testInvalidKValue() {
        String[] args = {"input_files/correctfiles.txt", "-1", "1"};
        CS_214_Project.main(args);
        assertEquals("Error: K must be greater than zero", errContent.toString().trim());
    
    }

    //k greater than number of iamges
    @Test
    public void testValueGreaterThanTotalImages() {
        String[] args = {"input_files/correctfiles.txt", "10", "1"};
        CS_214_Project.main(args);
        assertEquals("Error: K can't be greater than the number of images", errContent.toString().trim());
    }

    //agglomerative clustering with 4 clusters
    @Test
    public void testClusteringNormHist_4clusters() {
        String[] args = {"input_files/correctfiles.txt", "4", "1"};
        CS_214_Project.main(args);

        String expectedOutput = "1.000000";
        assertEquals(expectedOutput, outContent.toString().trim());
    }

    //agglomerative clustering with all image in one cluster
    @Test
    public void testClusteringNormHist_1cluster() {
        String[] args = {"input_files/correctfiles.txt", "1", "1"};
        CS_214_Project.main(args);

        String expected = "0.285714";
        assertEquals(expected, outContent.toString().trim());
    }

    //agglomerative clustering with all images in seperate clusters
    @Test
    public void testClusteringNormHist_serpateClusters() {
        String[] args = {"input_files/correctfiles.txt", "7", "1"};
        CS_214_Project.main(args);

        String expectedOutput = "1.000000";
        assertEquals(expectedOutput, outContent.toString().trim());
    }

    @Test
    public void testClusteringNormHist4_3Clusters() {
        String[] args = {"input_files/correctfiles.txt", "3", "2"};
        CS_214_Project.main(args);

        String expected = "0.714286";
        assertEquals(expected, outContent.toString().trim());
    }

    @Test
    public void testClusteringInvSquareDiff_4Clusters() {
        String[] args = {"input_files/correctfiles.txt", "4", "3"};
        CS_214_Project.main(args);

        String expected = "1.000000";
        assertEquals(expected, outContent.toString().trim());
    }

    @Test
    public void testClusteringNormHist9_3Clusters() {
        String[] args = {"input_files/correctfiles.txt", "3", "4"};
        CS_214_Project.main(args);

        String expected = "0.714286";
        assertEquals(expected, outContent.toString().trim());
    }


}
