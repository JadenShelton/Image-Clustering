import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class PerceptronSimilarity extends SimilarityCalculator{
    private PerceptronManager perceptronManager;

    public PerceptronSimilarity(PerceptronManager perceptronManager) {
        this.perceptronManager = perceptronManager;
    }

    @Override
    public BigDecimal calculateSimilarity(Cluster c1, Cluster c2) {
        double totalOfSimilarities = 0.0;

        List<Image> c1Images = c1.getImages();
        List<Image> c2Images = c2.getImages();

        for(Image img1 : c1Images) {
            for(Image img2 : c2Images) {
                totalOfSimilarities += perceptronManager.calculateImageSimilarity(img1, img2);
            }
        }

        double averageClusterSimilarity = totalOfSimilarities / (c1Images.size() * c2Images.size());

        return new BigDecimal(averageClusterSimilarity).setScale(6, RoundingMode.HALF_UP);
    }
    
}
