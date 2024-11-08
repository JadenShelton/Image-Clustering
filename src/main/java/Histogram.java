import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Represents a Histogram object used for characterising images for comparison.
 * 
 * Provides Functionality for merging two histograms, normalizing histograms, 
 * and computing the pairwise minimum between two histograms for comparisons. 
 */
public class Histogram {
    private double[] bins;

    /**
     * Constructs a Histogram object given a double array of a histogram.
     * 
     * @param bins The array storing the histogram values
     */
    public Histogram(double[] bins) {
        this.bins = bins;
    }

    /**
     * Computes the pairwise minimum between the histogram and a given histogram. 
     * 
     * Iterates through both histograms adding the minimum value found in each bin to the sum.
     * 
     * @param other The histogram for comparison
     * @return The sum of the minimums between the two histograms, rounds to six decimal places.
     */
    public BigDecimal computePairwiseMin(Histogram other) {
        double sumOfMins = 0.0;
        for(int i = 0; i < bins.length; ++i) {
            sumOfMins += Math.min(this.bins[i], other.bins[i]);
        }
        //rounds to 6 decimal places
        return new BigDecimal(sumOfMins).setScale(6, RoundingMode.HALF_UP);
    }

    /**
     * Merges the histogram with a given histogram. 
     * Merges histograms from two clusters given the current size of the cluster and the current size of the other cluster.
     * 
     * @param other The histogram for comparison.
     * @param currentSize The current size of the list of images in the cluster. Just 1 if it is a standalone image histogram.
     * @param otherSize The current size of the other list of images in the cluster. Just 1 if is a standalone image histogram.
     */
    public void merge(Histogram other, int currentSize, int otherSize) {
        for(int i = 0; i < bins.length; ++i) {
            this.bins[i] = (this.bins[i] * currentSize + other.bins[i] * otherSize) / (currentSize + otherSize);
        }
    }
    
    /**
     * Normalizes the Histogram for better comparisons.
     * 
     * Creates a new histogram to return to image class. 
     * Iterates through the current histogram and divides each bin by the total number of pixels in the image.
     * Assumes image size of 128*128.
     * 
     * @return A normalized version of the Histogram.
     */
    public Histogram normHist() {
       double[] normalizedBins = new double[bins.length];
       double totalPixels = 128*128; //only acepeted image size

       for(int i = 0; i < bins.length; ++i) {
        normalizedBins[i] = this.bins[i] / totalPixels;
       }

       return new Histogram(normalizedBins);
    }

    /**
     * Retrieves the the double array the histogram is stored within.
     * 
     * @return A double Array that stores the Histogram data.
     */
    public double[] getBins() {
        return bins;
    }

    /**
     * Builds a space seperated string of all the histogram values.
     * 
     * @return The String of space seperated histogram values.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(double value : bins) {
            sb.append(value).append(" ");
        }

        return sb.toString().trim();
    }
}
