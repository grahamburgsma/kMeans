import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Project: Assign2
 * Name: Graham Burgsma
 * Created on 07 March, 2016
 */
public class DataReader {

    //Reads data from text file given file name
    public ArrayList<Point> getData(String dataFileName) {
        Scanner scanner;
        ArrayList<Point> points = new ArrayList<Point>();

        try {
            String fileName = "data/" + dataFileName;
            scanner = new Scanner(new File(fileName));

            while (scanner.hasNext()) {
                points.add(new Point(scanner.nextInt(), scanner.nextInt()));
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return points;
    }
}
