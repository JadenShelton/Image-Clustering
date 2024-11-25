import java.io.FileNotFoundException;
import java.util.List;

/**
 * Main Class for my Image Clustering project, it processes images and groups them
 * into clusters based on different similarity measures
 */
public class CS_214_Project {

    /**
     * Main entry point for Image Clustering application, takes input in, performs clustering,
     * and outputs the results.
     * @param args
     */
    public static void main(String[] args){

        //check for valid arguments
        if(args.length != 3) {
            System.err.println("Error: three argumens required - <Training_Set> <Test_Set> <K> or <Test_Set> <K> <SimilarityMeasure>");
            return;
        }
        
        String inputSetOne = args[0];
        String inputSetTwoOrK = args[1];
        String kValueOrSimilarityM = args[2];
        
        
       

        try {
            boolean isPerceptronMode = !isInteger(inputSetTwoOrK);

            int k;
            int similarityMeasure;
            List<Image> testImages;

            //check for valid K value
            if(isPerceptronMode) {
                k = Integer.parseInt(kValueOrSimilarityM);
                similarityMeasure = 5;
                List<Image> trainingImages = IntialFileReader.readImagesFromFile(inputSetOne);
                testImages = IntialFileReader.readImagesFromFile(inputSetTwoOrK);

                validateK(k, testImages.size());

                //train perceptron
                PerceptronManager perceptronManager = new PerceptronManager();
                perceptronManager.createPerceptrons(trainingImages);

                //Perform clustering
                ClusterManager manager = new ClusterManager(testImages);
                SimilarityCalculator calculator;
                calculator = SimilarityCalculator.createCalculator(similarityMeasure, perceptronManager);
                manager.performClustering(k, calculator);

                //print clusters
                List<Cluster> clusters = manager.getClusters();
                for(Cluster cluster : clusters) {
                    System.out.println(cluster.getClusterNames());
                }
            
            } else {
                k = Integer.parseInt(inputSetTwoOrK);
                similarityMeasure = Integer.parseInt(kValueOrSimilarityM);
                testImages = IntialFileReader.readImagesFromFile(inputSetOne);

                validateK(k, testImages.size());

                if (similarityMeasure < 1 || similarityMeasure> 4) {
                    throw new IllegalArgumentException("Similarity measure must be between 1 and 4");
                }

                //perform clustering
                ClusterManager manager = new ClusterManager(testImages);
                SimilarityCalculator calculator;
                calculator = SimilarityCalculator.createCalculator(similarityMeasure, null);
                manager.performClustering(k, calculator);

                //print clusters
                List<Cluster> clusters = manager.getClusters();
                for(Cluster cluster : clusters) {
                    System.out.println(cluster.getClusterNames());
                }
            }

        } catch (NumberFormatException e) {
            System.err.println("Error: K must be an integer");
        } catch (FileNotFoundException e) {
            System.err.println("Error: "+ e.getMessage());
        } catch (InvalidImageFormatException e) {
            System.err.println("Error: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    
    }

    public static boolean isInteger(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static void validateK(int k, int numOfImages) {
        if (k <= 0) {
            throw new IllegalArgumentException("K must be greater than zero");
        }

        if(numOfImages < k) {
            throw new IllegalArgumentException("K can't be greater than the number of images");
        }
    }

}
