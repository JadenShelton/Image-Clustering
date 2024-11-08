import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Provides functionality of similarity calculation utilizing pairwise minimum comparisons between 1 ninth histograms of a cluster.
 */
public class NormHist9Similarity extends SimilarityCalculator{
    /**
     * Calcualtes the similarity between two Clusters utilizing normalized histograms of the cluster divided into ninths.
     * 
     *  @return The BigDecimal value rounded to 6 decimal places that represents the similarity between the two clusters.
     */
    @Override
    public BigDecimal calculateSimilarity(Cluster c1, Cluster c2) {
        BigDecimal total = BigDecimal.ZERO;
        for(int i = 0; i < 9; ++i) {
            total = total.add(c1.getNinthsHistograms(i).computePairwiseMin(c2.getNinthsHistograms(i)));
        }
        return total.divide(BigDecimal.valueOf(4), RoundingMode.HALF_UP);
    }
}
