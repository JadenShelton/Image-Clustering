import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Controls all the processes of Clustering a set of images.
 * Also provides methods for varifying the accuracy of those clusterings if the given Image filnames are formated to contain a ground truth value.
 * 
 * The class implements an agglomerative (bottom up) clustering approach where clusters are merged until a target number of clusters is reached.
 */
public class ClusterManager {
    private List<Cluster> clusters;

    /**
     * Contructs a new Cluster Manager containing a list of Image Clusters.
     * Populates the list of clusters by creating one cluster representation of every image in the given list.
     * To prepare for the clustering process.
     * 
     * @param images the List of images to be clustered for comparison
     */
    public ClusterManager(List<Image> images) {
        clusters = new ArrayList<>();
        for(Image img : images) {
            clusters.add(new Cluster(img));
        }
    }

    /**
     * Performs clustering by merging the most similar clusters together
     * unitl the targe number of clusters is reached.
     * 
     * The method uses a similarity calculator class to determine the similarity between two clusters
     * and merges the pair with the highest similarity value in each iteration.
     * 
     * @param targetK
     * @param calculator
     */
    public void performClustering(int targetK, SimilarityCalculator calculator) {
        while(clusters.size() > targetK) {
            double maxSimilarity = -1.0;
            int mergeIndex1 = -1;
            int mergeIndex2 = -1;

            //Find the two most similar clusters based on the similarity measurement given
            for(int i = 0; i < clusters.size(); ++i) {
                for(int j = i + 1; j < clusters.size(); ++j) { //nested for loop compares each image cluster to each other
                    BigDecimal similarity = calculator.calculateSimilarity(clusters.get(i),clusters.get(j)); //compares the normalized histograms of the clusters at index i and j gives a similarity value
                    double simValue = similarity.doubleValue(); //converts big decimal into a double
                    
                    if(simValue > maxSimilarity) { //if it is more similar than previously compared cluster than replace it as most similar
                        maxSimilarity = simValue;
                        mergeIndex1 = i;            //track the most similar clusters for merge
                        mergeIndex2 = j;
                    }
                }
            }

            //Merge the two clusters
            if(mergeIndex1 != -1 && mergeIndex2 != -1) {
                clusters.get(mergeIndex1).merge(clusters.get(mergeIndex2));
                clusters.remove(mergeIndex2);
            } else {
                break; //No more clusters to merge
            }
        }
     }

     /**
      * Evaluates the quality of the clustering by comparing each cluster groupings ground truch values.
      *
      * @return a double representing the correctness of the grouping 1.0 is perfect, the higher the score the better the clustering.
      */
     public double clusterQualityEvaluation() {
        int totalImages = 0;
        int correctlySortedImages = 0; //count of images that are correctly grouped based on truth value

        for (Cluster cluster : clusters) {
            List<Image> clusterImages = cluster.getImages();
            totalImages += clusterImages.size();
            
            List<Integer> truthValues = new ArrayList<>(); //store all the truth values of the images in this cluster
            for(Image img : cluster.getImages()) {
                truthValues.add(img.getClassNumber());
            }

            int dominantTruthValue = findDominantCategory(truthValues);
            
            //count number of images with the dominant truth value
            int matchingImages = 0;
            for(Image img : clusterImages) {
                if(img.getClassNumber() == dominantTruthValue) {
                    matchingImages++;
                }
            }

            correctlySortedImages += matchingImages;
        }

        return (double) correctlySortedImages / totalImages;
     }

     /**
      * Finds the most common ground truth value within a list of truth values from a clustering.
      * Determines the primary category of cluster in order to measure the quality of that clustering.
      *
      * @param truthValues List of integer truth values provided by the image filenames
      * @return the dominant truth value in the list
      */
     public int findDominantCategory(List<Integer> truthValues) {
        int maxCount = 0;
        int dominantTruthValue = truthValues.get(0);

        for(int truthCount : truthValues) {
            int count = 0;
            for(int value : truthValues) {
                if(value == truthCount) {
                    count++;
                }
            }

            if(count > maxCount) {
                maxCount = count;
                dominantTruthValue = truthCount;
            }
        }

        return dominantTruthValue;
     }

     /**
      * Retrieves the current list of clusters within the cluster manager
      *
      * @return List of Cluster objects showing the current clustering state
      */
     public List<Cluster> getClusters() {
        return clusters;
     }
}
