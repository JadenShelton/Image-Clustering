import java.math.BigDecimal;
import java.math.RoundingMode;

public class NormHist4Similarity extends SimilarityCalculator {

    @Override
    public BigDecimal calculateSimilarity(Cluster c1, Cluster c2) {
        BigDecimal total = BigDecimal.ZERO;
        for(int i = 0; i < 4; ++i) {
            total = total.add(c1.getQuarterHistogram(i).computePairwiseMin(c2.getQuarterHistogram(i)));
        }
        return total.divide(BigDecimal.valueOf(4), RoundingMode.HALF_UP);
    }
    
}