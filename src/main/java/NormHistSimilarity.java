import java.math.BigDecimal;

/**
 * Provides functionality of similarity calculation utilizing pairwise minimum comparisons between two normalized histograms of clusters.
 */
public class NormHistSimilarity extends SimilarityCalculator {

    /**
     * Utilizes the pairwise minimum comparison method to compare two clusters.
     * 
     * @return The BigDecimal value that represents the similarity between the two clusters.
     */
    @Override
    public BigDecimal calculateSimilarity(Cluster c1, Cluster c2) {
        return c1.getHistogram().computePairwiseMin(c2.getHistogram());
    }
}
