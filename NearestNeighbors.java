//Programmed by Frederick Andrews, 300247972

import java.util.ArrayList;
import java.util.List;

public class NearestNeighbors {

    //Instance variables
    List<Point3D> pointsList;

    //Constructor
    NearestNeighbors(List<Point3D> pointsList){
        this.pointsList= pointsList;
    }

    //rangeQuery method that finds the nearest neighbors of a 3D point
    public List<Point3D> rangeQuery(double eps, Point3D P){
        List<Point3D> neighbors = new ArrayList<Point3D>();
        for(Point3D point: pointsList){
            if(point.distance(P)<= eps ){
                neighbors.add(point);
            }
        }
        return neighbors;
    }
}
