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

                int desiredOutput = (image.getGroundTruthValue() == classNumber) ? 1 : -1;

                double output = perceptronEvaluation(histogram);

                double weightAndBiasAdjustment = (desiredOutput - output); // (d - y)
                for(int i = 0; i < 64; ++i) {
                    weights[i] += weightAndBiasAdjustment * histogram[i]; //w[i] <- w[i] + (d-y) * h[i]
                }
                bias += weightAndBiasAdjustment; // b <- b + (d - y)
            }
        }
    }

    public double perceptronEvaluation(double[] histogram) {
        if(histogram.length != 64) {
            throw new IllegalArgumentException("Histogram must be a size of 64");
        }

        // y = b + w[i] * h[i]
        double sum = bias; // y + b
        for(int i = 0; i < 64; ++i) {
            sum += weights[i] * histogram[i]; //sum of all weights multipled by all hsitogram values
        }

        return sum;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        //all 64 weights in one line
        for(Double weight : weights) {
            sb.append(String.format("%.6f", weight)).append(" ");
        }
        //bias at the end
        sb.append(String.format("%.6f", bias)).append(" ");

        return sb.toString();
    }

}
