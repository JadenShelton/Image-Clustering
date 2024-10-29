import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a cluster of Image objects.
 * Provides functionality for merging clusters together, calculating the squared difference
 * of a cluster, and retrieving information about clusters and the images within them.
 */
public class Cluster {
    private List<Image> images;
    private Histogram histogram;

    /**
     * Constructs a new Cluster given a image
     * @param image
     */
    public Cluster(Image image) {
        this.images = new ArrayList<>();
        this.images.add(image);
        this.histogram = image.getNormHistogram(); 
    }

    /**
     * Takes a given cluster and adds all its images to the current cluster, sorts them based on filenames,
     * and merges the histograms together as well
     */ 
    public void merge(Cluster other) {
        this.images.addAll(other.getImages());
        Collections.sort(this.images, (a, b) -> a.getFileName().compareTo(b.getFileName())); //sorts images using a lambda expression
        //calculate the new merged histogram
        int currentSize = this.images.size();
        int otherSize = other.getImages().size();
        this.histogram.merge(other.getHistogram(), currentSize, otherSize);
    }

    /**
     * Returns the list of images within the Cluster
     * @return Images in Cluster
     */
    public List<Image> getImages() {
        return images;
    }

    /**
     * Returns the Histogram representation of the Cluster
     * @return 
     */
    public Histogram getHistogram() {
        return histogram;
    }

    public String getClusterNames() {
        StringBuilder sb = new StringBuilder();
        for(Image img : images) {
            sb.append(img.getFileName()).append(" ");
        }
        return sb.toString().trim();
    }

    public Histogram getQuarterHistogram(int index) {

        if(images.isEmpty()) {
            throw new IllegalArgumentException("Cluster has no images");
        }
        return images.get(0).getQuarterHistograms()[index];
    }

    public Histogram getNinthsHistograms(int index) {

        if(images.isEmpty()) {
            throw new IllegalArgumentException("Cluster has no images");
        }
        return images.get(0).getNinthsHistograms()[index];
    }

    public double calculateSquaredDifference(Cluster other) {
        double sum = 0.0;
        for(Image img1 : this.images) {
            for(Image img2 : other.getImages()) {
                sum += img1.calculateSquaredDifference(img2);
         }
        }
        return sum / (this.images.size() * other.getImages().size());
    }
}
