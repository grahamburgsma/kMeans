import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Project: Assign2
 * Name: Graham Burgsma
 * Created on 08 March, 2016
 */
public class KMeans {

    private static final Random random = new Random(10);
    private List<Point> points;
    private List<Cluster> clusters;
    private Distance distanceMeasure;
    private double dunnIndex;

    public KMeans(int kValue, List<Point> points, Distance distanceMeasure) {
        this.points = points;
        this.distanceMeasure = distanceMeasure;

        createClusters(kValue);
        boolean keepGoing;
        do {
            keepGoing = assignPointsToClosestCluster();
            recalculateCentroids();
        } while (keepGoing); //Repeat until no centroids change location
    }

    public List<Cluster> getClusters() {
        return clusters;
    }

    //Take average of all points in cluster
    private void recalculateCentroids() {
        for (Cluster cluster : clusters) {
            double xSum = 0, ySum = 0;

            for (Point point : cluster.points) {
                xSum += point.x;
                ySum += point.y;
            }

            cluster.centroid.x = xSum / cluster.points.size();
            cluster.centroid.y = ySum / cluster.points.size();
        }
    }

    //Assign points to nearest cluster
    private boolean assignPointsToClosestCluster() {
        boolean change = false;

        for (Point point : points) {
            double smallestDistance = Double.MAX_VALUE;
            Cluster closestCluster = clusters.get(0);

            for (Cluster cluster : clusters) {
                double distance = getDistance(point, cluster.centroid);
                if (distance < smallestDistance) {
                    smallestDistance = distance;
                    closestCluster = cluster;
                }
            }

            //Change points cluster by removing from old and assigning to new
            if (point.cluster != closestCluster) {
                change = true;
                if (point.cluster != null)
                    point.cluster.points.remove(point);
                closestCluster.points.add(point);
                point.cluster = closestCluster;
            }
        }

        return change;
    }

    //Create K initial clusters from random point
    private void createClusters(int kValue) {
        clusters = new ArrayList<Cluster>();

        for (int i = 0; i < kValue; i++) {
            Cluster cluster = new Cluster();
            Point randomPoint = getRandomPoint();
            cluster.centroid = new Point(randomPoint.x, randomPoint.y);
            clusters.add(cluster);
        }
    }

    //Calculates the Dunn index
    public double calculateDunn() {
        double minDistance = Double.MAX_VALUE;
        double maxDistance = 0;

        for (Point point1 : points) {
            for (Point point2 : points) {
                double distance = getDistance(point1, point2);
                if (point1.cluster != point2.cluster && distance < minDistance)
                    minDistance = distance;
                else if (distance > maxDistance)
                    maxDistance = distance;
            }
        }

        return dunnIndex = minDistance / maxDistance;
    }

    //Calculates distance between two points
    private double getDistance(Point point, Point centroid) {
        switch (distanceMeasure) {
            case Euclidean:
                return Math.sqrt(Math.pow(point.x - centroid.x, 2) + Math.pow(point.y - centroid.y, 2));
            case Chebyshev:
                return Math.max(Math.abs(point.x - centroid.x), Math.abs(point.y - centroid.y));
            case Manhattan:
                return Math.abs(point.x - centroid.x) + Math.abs(point.y - centroid.y);
            default:
                return 0;
        }
    }

    //Prints centroids and Dunn index
    public void printSummary() {
        DecimalFormat decimalFormat = new DecimalFormat("#.0000");
        for (Cluster cluster : clusters) {
            System.out.println(decimalFormat.format(cluster.centroid.x) + " & " + decimalFormat.format(cluster.centroid.y) + " \\\\");
        }

        DecimalFormat dunnFormat = new DecimalFormat("#.#");
        dunnFormat.setMaximumFractionDigits(10);
        System.out.println("Dunn Index: " + dunnFormat.format(dunnIndex));
    }

    private Point getRandomPoint() {
        return points.get(random.nextInt(points.size()));
    }

    enum Distance {
        Euclidean, Chebyshev, Manhattan
    }
}
