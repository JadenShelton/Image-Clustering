import java.math.BigDecimal;
import java.math.RoundingMode;

public class Histogram {
    private double[] bins;

    public Histogram(double[] bins) {
        this.bins = bins;
    }

    public BigDecimal computePairwiseMin(Histogram other) {
        double sumOfMins = 0.0;
        for(int i = 0; i < bins.length; ++i) {
            sumOfMins += Math.min(this.bins[i], other.bins[i]);
        }
        //rounds to 6 decimal places
        return new BigDecimal(sumOfMins).setScale(6, RoundingMode.HALF_UP);
    }

    public void merge(Histogram other, int currentSize, int otherSize) {
        for(int i = 0; i < bins.length; ++i) {
            this.bins[i] = (this.bins[i] * currentSize + other.bins[i] * otherSize) / (currentSize + otherSize);
        }
    }

    //added this functionality here and removed from the image create histogram class so image makes a generic histogram by default
    
    public Histogram normHist() {
       double[] normalizedBins = new double[bins.length];
       double totalPixels = 128*128; //only acepeted image size

       for(int i = 0; i < bins.length; ++i) {
        normalizedBins[i] = this.bins[i] / totalPixels;
       }

       return new Histogram(normalizedBins);
    }

    public double[] getBins() {
        return bins;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(double value : bins) {
            sb.append(value).append(" ");
        }

        return sb.toString().trim();
    }
}
