package visu;

import java.util.HashMap;
import java.util.List;
import text.*;

import com.cassiopee.textclustering.Clusters;

public class Score {
	
	private Vecteurs v;
	
	private HashMap<Integer,List<List<List<String>>>> clustersAll;
	
	
	private void read(){
		DB db = new DB("48_2"); 
		
		
		
		v =  new Vecteurs();
		v.lecture(db, true, true); 
		v.savefile("data/titles_test1.txt", ",");
		
		db.close();
		
	}
	
	public Score(int iterations){
	
	clustersAll = new HashMap<Integer,List<List<List<String>>>>(); 	
		
	double max = 0;
	int idmax = 0;
	read();
	
	
	for (int i = 0; i <= iterations; i++){
		List<List<List<String>>> clusterss = clusterize();
		clustersAll.put(i, clusterss);
		double s = score(clusterss);
		
		if (s > max){
			max = s;
			idmax = i;
		}
		
		System.out.println(i + " : " + s);
		
	}
	System.out.println(max + " (" + idmax + ")");
	
	display(clustersAll.get(idmax));
	
	
	}
	
	private List<List<List<String>>> clusterize(){
		Clusters clusters = new Clusters(10, "data/titles_test1.txt", false, null);
		
		clusters.createDictionary();
		
		clusters.vectorizeData();
		clusters.create(0);
		return clusters.getResultClusters();
	}
	
	private double score(List<List<List<String>>> clusterss){
		double score = 0;
			
		for(List<List<String>> cluster : clusterss){
			HashMap<String,Integer> cmap = new HashMap<String,Integer>();
			String a = "";
			int nb =0;
			
			HashMap<String,Integer> gmap = new HashMap<String,Integer>();
			
			for(List<String> titre : cluster){
				nb++;
				
				for(String mot : titre){
					if(cmap.containsKey(mot)){
						cmap.replace(mot, cmap.get(mot)+1);
					}else{
						cmap.put(mot, 1);
					}
					
					
				}
				int id = v.getKeyVecteurs(titre);
				String groupe = v.getGroup(id);
				
				if(gmap.containsKey(groupe)){
					gmap.replace(groupe, gmap.get(groupe)+1);
				}else{
					gmap.put(groupe, 1);
				}
				
				
				a += v.getAuthor(id) + " ";
				
				
			}
			
			if (nb > 2 && nb < 100){
				score += 10;
			}
			
			if (nb > 3){
				double f = 0;
				for (String entry : cmap.keySet())
				{
					double f2 = (double) cmap.get(entry)/nb;
					if ( f2 > f){
						f =f2;
					}
				    
				}
				score += f*10;
			}
			
		}
		
		return score;
	}
	
	private void display(List<List<List<String>>> clusterss){
		int i = 1;
		
		for(List<List<String>> cluster : clusterss){
			HashMap<String,Integer> cmap = new HashMap<String,Integer>();
			String a = "";
			int nb =0;
			
			HashMap<String,Integer> gmap = new HashMap<String,Integer>();
			
			for(List<String> titre : cluster){
				nb++;
				for(String mot : titre){
					if(cmap.containsKey(mot)){
						cmap.replace(mot, cmap.get(mot)+1);
					}else{
						cmap.put(mot, 1);
					}
					
				}
				int id = v.getKeyVecteurs(titre);
				String groupe = v.getGroup(id);
				
				if(gmap.containsKey(groupe)){
					gmap.replace(groupe, gmap.get(groupe)+1);
				}else{
					gmap.put(groupe, 1);
				}
				
				
				a += v.getAuthor(id) + " ";
			}
			
			System.out.println("\nCluster " + i + " (" + nb+ " entrées) :\n");
			System.out.println( a + "\n");
			for (String group : gmap.keySet())
			{
				
			    System.out.println(group  + " :" + gmap.get(group));
			}
			System.out.println("\n");
			for (String entry : cmap.keySet())
			{
				if (cmap.get(entry) > 1)
			    System.out.println(entry  + " [" + cmap.get(entry) + "]");
			}
			i++;
		}
	}

}
