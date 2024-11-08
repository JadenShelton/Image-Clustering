import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a cluster of Image objects.
 * Maintains a collection of related images and thier combined Histogram data.
 * Provides functionality for merging clusters together, calculating the squared difference
 * of a cluster, and retrieving information about clusters and the images within them.
 */
public class Cluster {
    private List<Image> images;
    private Histogram histogram;

    /**
     * Constructs a new Cluster containing a single image
     * Intializes the clustsers histogram using the normalized histogram of the provided image.
     * 
     * @param image the intial image provided for the cluster
     */
    public Cluster(Image image) {
        this.images = new ArrayList<>();
        this.images.add(image);
        this.histogram = image.getNormHistogram(); 
    }

    /**
     * Merges another given cluster into this cluster.
     * Combines the  image lists from both clusters, sorts them by file name,
     * and updates the histogram to refelcet the new merged data.
     * 
     * @param other Cluster to be merged into this one
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
     * Retrieves the list of images within the Cluster
     * 
     * @return List of Image objects in the Cluster
     */
    public List<Image> getImages() {
        return images;
    }

    /**
     * Retieves the combined Histogram representation of the Cluster
     * 
     * @return The Histogram Object representing the Cluster
     */
    public Histogram getHistogram() {
        return histogram;
    }

    /**
     * Retrieves a String of all the images filenames in this Cluster in their sorted order.
     * 
     * @return The space sperated String of image filenames in the Cluster
     */
    public String getClusterNames() {
        StringBuilder sb = new StringBuilder();
        for(Image img : images) {
            sb.append(img.getFileName()).append(" ");
        }
        return sb.toString().trim();
    }
    
    /**
     * Retieves the specified quarter histogram of the first image in the Cluster from the given index
     * 
     * @param index The index of the quareter histogram to get (0-3)
     * @return The Histogram Object representing the quarter specified 
     * @throws IllegalArgumentException if the cluster contains no images
     */
    public Histogram getQuarterHistogram(int index) {

        if(images.isEmpty()) {
            throw new IllegalArgumentException("Cluster has no images");
        }
        return images.get(0).getQuarterHistograms()[index];
    }

    /**
     * Retrieves the specified one ninth histogram representation of the first image in the cluster from the given index
     * 
     * @param index The index of the specific ninth of the images histogram to retrieve
     * @return Histogram that represent that one ninth of the image
     */
    public Histogram getNinthsHistograms(int index) {

        if(images.isEmpty()) {
            throw new IllegalArgumentException("Cluster has no images");
        }
        return images.get(0).getNinthsHistograms()[index];
    }

    /**
     * Calculates the squared difference of this cluster with an other cluster.
     * The calculation compares each image in this cluster with each image in the given cluster by
     * calculating the squared difference between each image and returns the average of those differences.
     * 
     * @param other The Cluster to compare
     * @return the average squared difference between all the images in the clusters
     */
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
