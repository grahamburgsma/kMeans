import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Project: Assign2
 * Name: Graham Burgsma
 * Created on 07 March, 2016
 */
public class Main extends Application {

    //Change these values for k-means algorithm
    private static final int K = 15;
    private static final String DATA_FILE_NAME = "s1.txt";
    private static final KMeans.Distance DISTANCE_ALGORITHM = KMeans.Distance.Euclidean;

    private static ScatterChart<Number, Number> scatterChart;
    private double dunnIndex = 0;

    public Main() {
        KMeans kMeans = new KMeans(K, new DataReader().getData(DATA_FILE_NAME), DISTANCE_ALGORITHM);

        graphClusters(kMeans.getClusters());
        dunnIndex = kMeans.calculateDunn();

        kMeans.printSummary();
    }

    public static void main(String[] args) {
        launch(args);
    }

    private void graphClusters(List<Cluster> clusters) {
        NumberAxis xAxis = new NumberAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setTickLabelsVisible(false);
        yAxis.setTickLabelsVisible(false);

        scatterChart = new ScatterChart<Number, Number>(xAxis, yAxis);

        XYChart.Series<Number, Number> seriesPoints = new XYChart.Series<Number, Number>();
        XYChart.Series<Number, Number> seriesCentroids = new XYChart.Series<Number, Number>();

        seriesPoints.setName("Points");
        seriesCentroids.setName("Centroids");

        for (Cluster cluster : clusters) {
            seriesCentroids.getData().add(new XYChart.Data<Number, Number>(cluster.centroid.x, cluster.centroid.y));

            for (Point point : cluster.points) {
                seriesPoints.getData().add(new XYChart.Data<Number, Number>(point.x, point.y));
            }
        }

        if (scatterChart != null) {
            scatterChart.getData().add(seriesPoints);
            scatterChart.getData().add(seriesCentroids);
        }
    }

    @Override
    public void start(Stage stage) throws Exception {
        Scene scene = new Scene(scatterChart, 800, 600);

        DecimalFormat dunnFormat = new DecimalFormat("#.#");
        dunnFormat.setMaximumFractionDigits(10);

        stage.setTitle("K=" + K + "\t" + DISTANCE_ALGORITHM + "\t" + DATA_FILE_NAME + "\tDunn: " + dunnFormat.format(dunnIndex));

        scene.getStylesheets().add("/scatterChart.css");

        stage.setScene(scene);
        stage.show();
    }
}
