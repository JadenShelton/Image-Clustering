import java.math.BigDecimal;

/**
 * Abstract Similarity Calculator class that provides the functionality for deciding which similarity measure to utilze depending on the input.
 */
public abstract class SimilarityCalculator {
    public abstract BigDecimal calculateSimilarity(Cluster c1, Cluster c2);

    /**
     * Takes in the an int that represents the similarity calculation method that should be used based on input.
     * 
     * Utilzes a switch statement to call the correct similarity measure class.
     * 
     * @param measureType The int value from the input that represents the wanted similarity measure to be used by the user.
     * @return The BigDecimal similarity measure that is between 1.0 and 0.0.
     */
    public static SimilarityCalculator createCalculator(int measureType, PerceptronManager perceptronManager) {
        switch (measureType) {
            case 1:
                return new NormHistSimilarity();
            case 2:
                return new NormHist4Similarity();
            case 3:
                return new InvSquareDiffSimilarity();
            case 4:
                return new NormHist9Similarity();
            case 5:
                return new PerceptronSimilarity(perceptronManager);
            default:
                throw new IllegalArgumentException("Invalid similarity measure type.");
        }
    }
}

