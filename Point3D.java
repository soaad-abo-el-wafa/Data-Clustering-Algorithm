//Programmed by Frederick Andrews, 300247972

import static java.lang.Math.sqrt;

public class Point3D {

    //instance variables
    private double x,y,z;
    private int label;

    //Constructor
    Point3D(double x, double y, double z, int label){

        this.x=x;
        this.y=y;
        this.z=z;
        this.label = label;

    }
    //Constructor without specified label
    Point3D(double x, double y, double z){

        this.x=x;
        this.y=y;
        this.z=z;
        this.label = -1;

    }
    //Getter methods
    public double getX(){
        return x;
    }

    public double getY(){
        return y;
    }

    public double getZ(){
        return z;
    }

    public int getLabel(){
        return label;
    }

    //Setter Method for label

    public void setLabel(int label){
        this.label= label;
    }

    //Method for calculating distance between points

    public double distance(Point3D other){
        return sqrt(Math.pow(this.getX()-other.getX(),2) + Math.pow(this.getY()-other.getY(),2) + Math.pow(this.getZ()-other.getZ(),2));
    }

}