package text;

import java.util.HashMap;
import java.util.List;

public class script {

	public static void main(String[] args) {
		
		/* EXEMPLES D'UTILISATION
		 * 
		 * Ce script permet de créer des vecteurs contenant les keywords utilisables pour la clusterisation et de les rajouter dans une base de données
		 * avec d'autres informations utiles (auteurs, groupes)
		 */
		
		 
		DB db = new DB("48_prod");
		
		//Les inforamtions sont récupérées dans l'une ou l'autre des bases de données, l'initialisation de Vecteurs est différente
		int dbnb = 1;
		DB myDB = new DB("48");
		Vecteurs v = new Vecteurs(myDB); 
		
//		int dbnb = 2;
//		DB myDb = new DB("48_2");
//		Vecteurs v = new Vecteurs();
//		v.lecture(myDb, true, true);
		
		List<List<String>> titles = v.getVectors();
		
		for (List<String> title : titles)
		{
		    String titleWords = "";
		    for(String word : title)
		    {
		    	titleWords += word + ';'; 
		    }
		    titleWords = titleWords.substring(0,titleWords.length()-1);
		    
		    int id = v.getKeyVecteurs(title);
		    String authorsOk = v.getAuthor(id);
		    String groups = v.getGroup(id);
		    
		    db.insert("INSERT INTO publis(title, authors, groups, dbId, db)  VALUES('" + titleWords + "','" + authorsOk + "','" + groups + "'," + id  + "," + dbnb + ")");
		    
		}
		
		System.out.println("Done \n");
		db.close();
		myDB.close();
		

}
	


}
