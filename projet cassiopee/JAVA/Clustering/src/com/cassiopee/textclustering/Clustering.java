package com.cassiopee.textclustering;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.JFrame;

import com.apporiented.algorithm.clustering.AverageLinkageStrategy;
import com.apporiented.algorithm.clustering.Cluster;
import com.apporiented.algorithm.clustering.ClusteringAlgorithm;
import com.apporiented.algorithm.clustering.DefaultClusteringAlgorithm;
import com.apporiented.algorithm.clustering.visualization.DendrogramPanel;

import text.DB;
import text.Publi;
import text.Vecteurs;

@SuppressWarnings("unused")
public class Clustering {

	public static void main(String[] args) {
		
		DB db = new DB("48"); //initialise la lecture de BD
		
		Vecteurs v =  new Vecteurs(db);
		v.savefile("data/articles.txt", ","); //sauvegarde au format voulu
		db.close(); 
		
//		Clusters clusters = new Clusters(25, "data/articles.txt", true, new File("data/clusters/"));
//		clusters.createDictionary();
//		clusters.vectorizeData();
//		clusters.create(1);
//		clusters.save();
//		Clusters.subClusterize("data/clusters/", 20, 1);
		Clusters.generateVisual("data/clusters/", "data/visual/", 1, v);
		
//		String [] names = new String[clusters.getInputSet().size()];
//		for(List<String> title : clusters.getInputSet()){
//			names[clusters.getInputSet().indexOf(title)] = title.toString();
//		}
//		double[][] distances = clusters.distanceMatrixToArray();
//		ClusteringAlgorithm alg = new DefaultClusteringAlgorithm();
//		Cluster cluster = alg.performClustering(distances, names, new AverageLinkageStrategy());
//		DendrogramPanel dp = new DendrogramPanel();
//		dp.setModel(cluster);
//		JFrame myWindow = new JFrame();
//		myWindow.setVisible(true);
//		myWindow.setTitle("Visualisation du dendrogramme");
//		myWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		myWindow.setLocationRelativeTo(null);
//		myWindow.setSize(500, 400);
//		myWindow.setExtendedState(myWindow.MAXIMIZED_BOTH);
//		myWindow.add(dp);
	}

}
