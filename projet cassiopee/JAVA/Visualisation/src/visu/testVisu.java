package visu;


import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import com.cassiopee.textclustering.*;
import text.*;


/* VISUALISATION */

/*
 * Projet permettant de g�n�rer les visuels apr�s clusterisation.
 * 
 * Est impl�ment� :
 *  - Score un test essayant de scorer les r�sultats en fonction des clusters obtenus et d'it�r� jusqu'� obtenir un score satisfaisant 
 * (le r�sultat n'�tant pas tr�s utile, on utilisation a �t� abandonn�e)
 *  - Wordcloud qui cr�e dans un r�p�rtoire une page html permettant de visualiser le resultat sous forme de nuage de mot (les fichiers Javascript doivent �tre rajout�s
 *   manuellement
 *   
 *   ici des exemples d'utilisation
 * 
 */

public class testVisu {

	public static void main(String[] args) {

	
		
	DB db = new DB("48_2");
		
		
		
	Vecteurs v =  new Vecteurs();

	v.lecture(db, true, true); 
	v.savefile("data/temp.txt", ",");
	
	db.close(); 
	
	Clusters clusters = new Clusters(10, "data/temp.txt", true, new File("data/temp/"));
	clusters.createDictionary();
	clusters.vectorizeData();
	clusters.create(1);
	
	
	clusters.save();
	Clusters.subClusterize("data/temp/", 20, 1);
	Clusters.generateVisual("data/temp/", "data/d3/");
	
	
	Wordcloud test = new Wordcloud(clusters.getResultClusters(), "data/visu");
	
	

        
	}
	

}
