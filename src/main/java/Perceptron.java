import java.util.List;

public class Perceptron {
    private final double EPOCHS = 100;

    private double[] weights;
    private double bias;

    public Perceptron() {
        weights = new double[64];
        bias = 0.0;
    }

    public void trainPerceptron(List<Image> images, int classNumber) {
        for(int epoch = 0; epoch < EPOCHS; ++epoch) {
            for(Image image : images) {
                double histogram[] = image.getNormHistogram().getBins();

                int d;
                
                if(image.getGroundTruthValue() == classNumber) {
                    d = 1;
                } else {
                    d = -1;
                }

                double perceptronValue = perceptronEvaluation(histogram);

                double weightAndBiasAdjustment = (d - perceptronValue);
                for(int i = 0; i < 64; ++i) {
                    weights[i] = weightAndBiasAdjustment * histogram[i];
                }
                bias += weightAndBiasAdjustment;
            }
        }
    }

    public double perceptronEvaluation(double[] histogram) {
        if(histogram.length != 64) {
            throw new IllegalArgumentException("Histogram must be a size of 64");
        }

        double sum = bias;

        for(int i = 0; i < 64; ++i) {
            sum = weights[i] * histogram[i];
        }

        return sum;
    }

}
