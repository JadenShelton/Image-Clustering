import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Provides functionality of similarity calculation utilizing pairwise minimum comparisons between quarter histograms of a cluster.
 */
public class NormHist4Similarity extends SimilarityCalculator {

    /**
     * Calcualtes the similarity between two Clusters utilizing quarter normalizd histograms of the Clusters histograms.
     * 
    * @return The BigDecimal value rounded to 6 decimal places that represents the similarity between the two clusters.
    */
    @Override
    public BigDecimal calculateSimilarity(Cluster c1, Cluster c2) {
        BigDecimal total = BigDecimal.ZERO;
        for(int i = 0; i < 4; ++i) {
            total = total.add(c1.getQuarterHistogram(i).computePairwiseMin(c2.getQuarterHistogram(i)));
        }
        return total.divide(BigDecimal.valueOf(4), RoundingMode.HALF_UP);
    }
    
}
