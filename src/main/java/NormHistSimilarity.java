import java.math.BigDecimal;

public class NormHistSimilarity extends SimilarityCalculator {

    @Override
    public BigDecimal calculateSimilarity(Cluster c1, Cluster c2) {
        return c1.getHistogram().computePairwiseMin(c2.getHistogram());
    }
}
