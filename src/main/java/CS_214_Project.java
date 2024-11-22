import java.io.FileNotFoundException;
import java.util.List;

/**
 * Main Class for my Image Clustering project, it processes images and groups them
 * into clusters based on different similarity measures
 */
public class CS_214_Project {

    /**
     * Main entry point for Imag Clustering application, takes input in, performs clustering,
     * and outputs the results.
     * @param args
     */
    public static void main(String[] args){
        //for checking run time lab
        long start = System.currentTimeMillis();

        //check for valid arguments
        if(args.length != 3) {
            System.err.println("Error: two argumens required - <input_file> <K>");
            return;
        }

        String inputFileName = args[0];
        String kValue = args[1];
        String similarityMeasureValue = args[2];
        int K;
        int similarityMeasure;

        try {
            //check for valid K value
            K = Integer.parseInt(kValue);
            if (K <= 0) {
                System.err.print("Error: K must be greater than zero");
                return;
            }

            similarityMeasure = Integer.parseInt(similarityMeasureValue);
            if(similarityMeasure <= 0 || similarityMeasure > 4) {
                System.err.println("Error: The similarity measure value must be in range of 1 - 4");
                return;
            }

            //store the image file paths in a list
            List<Image> images = IntialFileReader.readImagesFromFile(inputFileName);

            if(images.size() < K) {
                System.err.println("Error: K can't be greater than the number of images");
                return;
            }

            ClusterManager manager = new ClusterManager(images);
            SimilarityCalculator calculator = SimilarityCalculator.createCalculator(similarityMeasure);
            manager.performClustering(K, calculator);

            //Output the ground truth measurement
            System.out.printf("%.6f%n", manager.clusterQualityEvaluation());

        } catch (NumberFormatException e) {
            System.err.println("Error: K must be an integer");
        } catch (FileNotFoundException e) {
            System.err.println("Error: "+ e.getMessage());
        } catch (InvalidImageFormatException e) {
            System.err.println("Error: " + e.getMessage());
        }
        //measuring runtime
        long end = System.currentTimeMillis();
        System.out.println(end - start);
    }
}


