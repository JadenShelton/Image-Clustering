import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ClusterManager {
    private List<Cluster> clusters;

    public ClusterManager(List<Image> images) {
        clusters = new ArrayList<>();
        for(Image img : images) {
            clusters.add(new Cluster(img));
        }
    }

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

     public double clusterQualityEvaluation() {
        int totalImages = 0;
        int correctlySortedImages = 0; //count of images that are correctly grouped based on truth value

        for (Cluster cluster : clusters) {
            List<Image> clusterImages = cluster.getImages();
            totalImages += clusterImages.size();
            
            List<Integer> truthValues = new ArrayList<>(); //store all the truth values of the images in this cluster
            for(Image img : cluster.getImages()) {
                truthValues.add(img.getGroundTruthValue());
            }

            int dominantTruthValue = findDominantCategory(truthValues);
            
            //count number of images with the dominant truth value
            int matchingImages = 0;
            for(Image img : clusterImages) {
                if(img.getGroundTruthValue() == dominantTruthValue) {
                    matchingImages++;
                }
            }

            correctlySortedImages += matchingImages;
        }

        return (double) correctlySortedImages / totalImages;
     }

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

     public List<Cluster> getClusters() {
        return clusters;
     }
}
