import java.math.BigDecimal;

//incase we need to compare the images in different ways down the line
public abstract class SimilarityCalculator {
    public abstract BigDecimal calculateSimilarity(Cluster c1, Cluster c2);

    //for deciding which type of similarity to calculate
    public static SimilarityCalculator createCalculator(int measureType) {
        switch (measureType) {
            case 1:
                return new NormHistSimilarity();
            case 2:
                return new NormHist4Similarity();
            case 3:
                return new InvSquareDiffSimilarity();
            default:
                throw new IllegalArgumentException("Invalid similarity measure type.");
        }
    }
}

