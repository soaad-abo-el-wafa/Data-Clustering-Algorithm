
import java.util.*;
import java.io. * ;

public class DBScan {

    //Instance Variables
    private List<Point3D> pointsList;
    private double eps,minPts;

    private int clusterIndex=0;

    private int numberOfNoise=0;

    private List<Integer> sizeList = new ArrayList<>();

    //Constructor
    public DBScan(List<Point3D> pointsList){
        this.pointsList= pointsList;
    }

    //Setter Methods
    public double setEps(double eps){
        this.eps= eps;
        return eps;
    }

    public double setMinPts(double minPts){
        this.minPts = minPts;
        return minPts;
    }

    //Getter Methods
    public int getNumberOfClusters(){
        return clusterIndex;
    }

    public List<Point3D> getPoints(){
        return pointsList;
    }

    public int getNumberOfNoise() {return numberOfNoise;}

    //Executes DBScan Algorithm
    public void findClusters(){
        //Starts counting clusters from 0
        clusterIndex = 0;

        Stack<Point3D> s= new Stack<Point3D>();

        //Iterates through each point
        for(Point3D point : pointsList){
            //if the point is undefined continue
            if(point.getLabel()==-1){

                //Find neighbors
                NearestNeighbors neighborFinder = new NearestNeighbors(pointsList);
                List<Point3D> neighbors = neighborFinder.rangeQuery(eps, point);
                //Checking the amount of neighbors of point
                if(neighbors.size() < minPts){ // if the point does not have enough neighbors it is noise
                    //-2 corresponds to noise
                    point.setLabel(-2);
                }else{
                    //Create new cluster ID
                    clusterIndex = clusterIndex+1;
                    point.setLabel(clusterIndex);
                    //Push every neighbor point onto a stack
                    for(Point3D neighborPoint : neighbors){
                        s.push(neighborPoint);
                    }

                    while(!s.isEmpty()){
                        //Pop every neighbor point out
                        Point3D q = s.pop();
                        if(q.getLabel() == -2){
                            //If the neighbor point is noise add it to cluster ID
                            q.setLabel(clusterIndex);

                        }
                        if(q.getLabel()== -1){
                            //if the neighbor point is undefined add it to the cluster ID
                            q.setLabel(clusterIndex);
                            //Add all the neighbors of the neighbor point to the stack
                            List<Point3D> qNeighbors= neighborFinder.rangeQuery(eps, q);
                            if(qNeighbors.size() >= minPts){
                                for(Point3D qPoint : qNeighbors){
                                    s.push(qPoint);
                                }
                            }
                        }
                    }
                }
            }

        }
    }
    //Reads from CSV file and returns list of points
    public static List<Point3D> read(String filename){
        //Setting the breaker to "," and instantiating the returnList
        String breaker = ",";
        List<Point3D> returnList = new ArrayList<Point3D>();
        //boolean flag for skipping the first Line
        boolean skipFirst = true;
        try{
            //Instantiating java io file readers
            File file = new File(filename);
            FileReader fileReader = new FileReader(file);
            BufferedReader br = new BufferedReader(fileReader);
            String pointInfo = " ";
            String [] pointInfoArr;
            //Reading through each line of the CSV file
            while((pointInfo = br.readLine()) != null){
                //Skips the first line
                if(skipFirst==true){
                    skipFirst=false;
                }else{
                    //Breaking each line into an array
                    pointInfoArr = pointInfo.split(breaker);
                    //Creating Point3D to add to list
                    Point3D newPoint = new Point3D(Double.parseDouble(pointInfoArr[0]),Double.parseDouble(pointInfoArr[1]),Double.parseDouble(pointInfoArr[2]));
                    returnList.add(newPoint);
                }
            }

        }catch(IOException error){
            error.printStackTrace();
        }
        return returnList;
    }
    //Writes to output CSV file
    public void save(String filename){

        try{
            //Creating header of file
            FileWriter csvWriter = new FileWriter(filename);
            csvWriter.append("x");
            csvWriter.append(",");
            csvWriter.append("y");
            csvWriter.append(",");
            csvWriter.append("z");
            csvWriter.append(",");
            csvWriter.append("C");
            csvWriter.append(",");
            csvWriter.append("R");
            csvWriter.append(",");
            csvWriter.append("G");
            csvWriter.append(",");
            csvWriter.append("B");
            csvWriter.append("\n");
            //iterate through every point
            for (Point3D point : pointsList) {
                //Reformat noise label and record number of noise points
                if(point.getLabel() == -2){
                    point.setLabel(0);
                    numberOfNoise= numberOfNoise+1;
                }
                //Determine RGB value
                Double divisor = Double.valueOf(clusterIndex);
                double rgbValue = point.getLabel()/divisor;
                //Add point entry to file
                String appendString = point.getX() + "," + point.getY() + "," + point.getZ() +"," + point.getLabel() + "," + rgbValue + "," + rgbValue + "," + rgbValue;
                csvWriter.append(appendString);
                csvWriter.append("\n");
            }
            //Close csvwriter
            csvWriter.flush();
            csvWriter.close();
        }catch(IOException e){
            e.printStackTrace();
        }

    }
    //Generates sorted Array of cluster sizes
    public int[] clusterSizes(){
        int[] clusterSizes = new int[clusterIndex+1];
        for(Point3D point: pointsList){
            if(point.getLabel() != 0){
                clusterSizes[point.getLabel()] = clusterSizes[point.getLabel()]+1;
            }

        }

        Arrays.sort(clusterSizes);
        return clusterSizes;

    }



    public static void main(String[] args) {
        //Takes input arguments and assigns them to variables
        String inputFile = args[0];
        String inputFileCut = inputFile.substring(0, inputFile.length() - 4);
        double eps = Double.parseDouble(args[1]);
        double minPts = Double.parseDouble(args[2]);

        //Runs the DBScan for the specified information
        DBScan reader = new DBScan(null);
        DBScan runner = new DBScan(reader.read(inputFile));
        runner.setEps(eps);
        runner.setMinPts(minPts);
        runner.findClusters();
        runner.save(inputFileCut + "_clusters_" + eps +"_"+minPts+"_"+runner.getNumberOfClusters()+".csv");

        //Prints the descending size of clusters
        int[] clusterSizes = runner.clusterSizes();
        for(int i=clusterSizes.length-1;i > 0; i--){
            System.out.println(clusterSizes[i]);
        }
        //Prints the number of noise points
        System.out.println(runner.getNumberOfNoise());

    }




}
