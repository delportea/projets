package com.orhandemirel.clustering;
 
import java.util.Random;
import java.util.ArrayList;
import java.util.List;
 
public class  Kmeans{
 
    private double[][] data;         // data to cluster
    private int numClusters;    // number of clusters
    private double[][] clusterCenters;   // cluster centers
    private int dataSize;               // size of the data
    private int dataDim;                // dimension of the data
    private List<List<List<Double>>> clusters;     // calculated clusters
    private double[] clusterVars;        // cluster variances
    private int stepsNbr;
    private int distance;
    private double epsilon;
  
    public Kmeans(double[][] data, int numClusters, int distance){
    	this(data, numClusters, distance, true);
    }
 
    public Kmeans(double[][] data, int numClusters, int distance, boolean randomizeCenters){
    	dataSize = data.length;
        dataDim = data[0].length;
        this.data = data;
        this.distance = distance;
        this.numClusters = numClusters;
        this.clusterCenters =  new double[numClusters][dataDim];
        clusters = new ArrayList<List<List<Double>>>();
        for(int i=0;i<numClusters;i++){
        	clusters.add(new ArrayList<List<Double>>());
        }
        clusterVars = new double[numClusters];
        epsilon = 0.01;
        stepsNbr = 0;
        if(randomizeCenters){
        	randomizeCenters(numClusters, data);
        }
    }
 
    private void randomizeCenters(int numClusters, double[][] data){
    	Random r = new Random();
     	int[] check = new int[numClusters];
     	for (int i = 0; i < numClusters; i++){
     		int rand = r.nextInt(dataSize);
            if (check[i] == 0){
            	this.clusterCenters[i] = data[rand].clone();
                check[i] = 1;
            } else {
                i--;
            }
        }
	}
 
    private void calculateClusterCenters(){
    	for(int i=0;i<numClusters;i++){
          	int clustSize = clusters.get(i).size();
          	for(int k= 0; k < dataDim; k++){
          		double sum = 0d;
          		for(int j =0; j < clustSize; j ++){
          			double[] elem = listToArray(clusters.get(i).get(j));
          			sum += elem[k];
                }
          		clusterCenters[i][k] = sum / clustSize;
            }
        }
    }
 
    private double[] listToArray(List<Double> vector){
		double[] data = new double[vector.size()];
		for(int i=0;i<vector.size();i++){
			data[i] = vector.get(i);
		}
		return data;
	}
    
    private List<Double> arrayToList(double[] vector){
		List<Double> data = new ArrayList<Double>();
		for(int i=0;i<vector.length;i++){
			data.add(i, vector[i]);
		}
		return data;
	}
    
    private void calculateClusterVars(){
     	for(int i=0;i<numClusters;i++){
         	int clustSize = clusters.get(i).size();
         	Double sum = 0d;
         	for(int j =0; j < clustSize; j ++){
         		double[] elem = listToArray(clusters.get(i).get(j));
         		for(int k= 0; k < dataDim; k++){
         			sum += Math.pow( (Double)elem[k] - getClusterCenters()[i][k], 2);
         		}
         	}
         	clusterVars[i] = sum / clustSize;
     	}
    }
 
    public List<List<List<Double>>> getClusters(){
		return clusters;
	}
 
	private void assignData(){
		for(int k=0;k<numClusters;k++){
			clusters.get(k).clear();
		}
		for(int i=0; i<dataSize; i++){
			int clust = 0;
            double dist = Double.MAX_VALUE;
            double newdist = 0;
            for(int j=0; j<numClusters; j++){
            	newdist = distToCenter(data[i], j, distance);
                if(newdist<=dist){
                	clust = j;
                	dist = newdist;
                }
            }
            clusters.get(clust).add(arrayToList(data[i]));
        }
    }
 
	private double distToCenter(double[] datum, int j, int distance){
		if(distance==0){
			double sum = 0d;
			for(int i=0;i < dataDim; i++){
				sum += Math.pow(( datum[i] - getClusterCenters()[j][i] ), 2);
			}
			return Math.sqrt(sum);
	 	} else if (distance==1) {
	 		return distanceCos(datum,getClusterCenters()[j]);
	 	} else {
	 		return 0;
	 	}    
	}
    
	public static double distanceEuclidian(double[] x1, double[] x2){
		double sum = 0d;
		for(int i=0;i < x1.length; i++){
			sum += Math.pow(( x1[i] - x2[i] ), 2);
		}
		return Math.sqrt(sum);
	}
	
	public static double distanceCos(double[] x1, double[] x2){
		return Math.abs(dot(x1,x2)/(norm(x1)*norm(x2))-1);
	}
	
    public static double dot(double[] x1, double[] x2){	
 		int n = x1.length;
 		double sum = 0d;
 		for (int i=0;i<=n-1;i++){
 			sum+=x1[i]*x2[i];
 		}
 		return sum;
 	}
 	
 	public static double norm(double[] x1){
 		double sum = 0d;
 		for(double comp : x1){
 			sum+=comp*comp;
 		}
 		return Math.sqrt(sum);
 	}
 
	public void calculateClusters(){
		double var1 = Double.MAX_VALUE;
		double var2;
		double delta;
		int steps = 0;
		do{
			calculateClusterCenters();
			assignData();
           	calculateClusterVars();
         	var2 = getTotalVar();
           	if (Double.isNaN(var2)){    // if this happens, there must be some empty clusters
            	delta = Double.MAX_VALUE;
              	randomizeCenters(numClusters, data);
              	assignData();
              	calculateClusterCenters();
              	calculateClusterVars();
           	} else {
           		delta = Math.abs(var1 - var2);
            	var1 = var2;
           	}
         	steps++;
		}while(delta > epsilon);
     	stepsNbr = steps;
    }
 
	public double[][] getClusterCenters(){
	    return clusterCenters;
	}

	public double[] getClusterVars(){
		return  clusterVars;
	}

	public int getStepsNbr(){
    	return stepsNbr;
    }

	public double getTotalVar(){
		double total = 0d;
		for(int i=0;i< numClusters;i++){
			total += clusterVars[i];
		}
		return total;
	}

	public void setEpsilon(double epsilon){
	 	if(epsilon > 0){
	      	this.epsilon = epsilon;
	  	}
	}
}