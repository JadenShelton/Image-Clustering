import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Provides calculation functionality for InvSquareDiff Similarity measure.
 * Contains Scaling factor for more acurate clustering.
 */
public class InvSquareDiffSimilarity extends SimilarityCalculator {
    private static final double SCALING_FACTOR = 1000000.0; //to reduce the super large values I'm getting

    /**
     * Calculates the Similarity between two clusters utilizing the squared difference method with Cluster class,
     * scales the result and is calculated and returned after being rounded to 6 decimal places.
     * 
     * @return The BigDecimal value rounded to 6 decimal places representing the similarity between the two clusters.
     */
    @Override
    public BigDecimal calculateSimilarity(Cluster c1, Cluster c2) {
        double squaredDifference = c1.calculateSquaredDifference(c2);
        double scaledDifference = squaredDifference / SCALING_FACTOR;

        double similarityValue = 1.0 / (scaledDifference + 1.0);
        return new BigDecimal(similarityValue).setScale(6, RoundingMode.HALF_UP);
    }
    
}
