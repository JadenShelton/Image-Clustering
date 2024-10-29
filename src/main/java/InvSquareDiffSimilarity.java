import java.math.BigDecimal;
import java.math.RoundingMode;

public class InvSquareDiffSimilarity extends SimilarityCalculator {
    private static final double SCALING_FACTOR = 1000000.0; //to reduce the super large values I'm getting

    @Override
    public BigDecimal calculateSimilarity(Cluster c1, Cluster c2) {
        double squaredDifference = c1.calculateSquaredDifference(c2);
        double scaledDifference = squaredDifference / SCALING_FACTOR;

        double similarityValue = 1.0 / (scaledDifference + 1.0);
        return new BigDecimal(similarityValue).setScale(6, RoundingMode.HALF_UP);
    }
    
}
