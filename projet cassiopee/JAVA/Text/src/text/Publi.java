package text;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Vector;

/* PUBLI */

/*
 * Cette classe facilite l'utilisation de la base de donnée de production (avec les données pré-traitées) qui est crée dans script.java 
 */

public class Publi {
	
	public Publi(){
		
	}
	/*récupère  les titres enfonction de la BD (1 = 48, 2 = 48_2, 3 = les deux)*/
	public List<String> getWords(int database){
		DB db = new DB("48_prod"); 
		List<String> l = new Vector<String>();
		
		
		ResultSet rset = null;
		if(database == 3){
			rset = db.query("SELECT id, title FROM publis");
		}else{
			rset = db.query("SELECT id, title FROM publis WHERE dbId = " + database);
		}
		
		try {
			while(rset.next()){
				l.add(rset.getString("title"));
				
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		db.close();
		
		return l;
	}
	
	/* Permet de savoir de quelle BD un titre (liste de mot clé) provient*/
	public int getDBId(String words){
		DB db = new DB("48_prod"); 
		int id = 0;

		ResultSet rset = null;
		rset = db.query("SELECT id, dbId FROM publis WHERE title = '" + words +"'");
		
		
		try {
			while(rset.next()){
				id = rset.getInt("dbId");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		db.close();
		return id;
		
	}
	
	/* Permet de savoir de quelle BD un id (de publication) provient*/
	public int getDBId(int rowId){
		DB db = new DB("48_prod"); 
		int id = 0;

		ResultSet rset = null;
		rset = db.query("SELECT dbId FROM publis WHERE id = " + rowId);
		
		
		try {
			while(rset.next()){
				id = rset.getInt("dbId");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		db.close();
		return id;
		
	}
	
	

}
