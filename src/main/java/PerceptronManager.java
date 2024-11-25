import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PerceptronManager {
    private List<Perceptron> perceptrons;
    private Set<Integer> imageClasses;

    public void createPerceptrons (List<Image> trainingImages) {
        imageClasses = new HashSet<>();
        for(Image img : trainingImages) {
            imageClasses.add(img.getClassNumber());
        }

        if(imageClasses.size() < 2) {
            throw new IllegalArgumentException("Training Data must have at least 2 different classes of images.");
        }

        perceptrons = new ArrayList<>();
        for(Integer classNumber : imageClasses) {
            Perceptron perceptron = new Perceptron();
            perceptron.trainPerceptron(trainingImages, classNumber);
            perceptrons.add(perceptron);
        }
    }

    public double[] getImageScores(Image img) {
        double[] scores = new double[perceptrons.size()];
        for(int i = 0; i < perceptrons.size(); ++i) {
            scores[i] = perceptrons.get(i).perceptronEvaluation(img.getNormHistogram().getBins()); //gets the 
        }

        return scores;
    }

    public double calculateImageSimilarity (Image img1, Image img2) {
        double[] imgScores1 = getImageScores(img1);
        double[] imgScores2 = getImageScores(img2);

        double similarity = 0.0;
        for(int i = 0; i < imgScores1.length; ++i) {
            double difference = imgScores1[i] - imgScores2[i];
            similarity += 1.0 / (1.0 + difference * difference); //add a constant 1.0 for normalization, prevents extreme similarity values for more accurate clustering
        }
        return similarity;
    }
}
