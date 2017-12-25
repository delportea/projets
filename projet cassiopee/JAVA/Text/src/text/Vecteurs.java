package text;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import javax.swing.plaf.BorderUIResource.TitledBorderUIResource;

import com.cybozu.labs.langdetect.Detector;
import com.cybozu.labs.langdetect.DetectorFactory;
import com.cybozu.labs.langdetect.LangDetectException;

import edu.stanford.nlp.parser.shiftreduce.BinaryTransition.Side;
import edu.stanford.nlp.simple.Sentence;

/* VECTEURS */

/*
 * Cette classe est la plus importante car elle effectue tout le pré-traitement du titre et des mots clés des publications
 * De manière peu élégante elle s'adapte à la base de donnée et à l'utilisation ou non des mots clés 
 */

public class Vecteurs {
	
	/* Une fois initialisée, elle contient les mots utilisables de chanque publication son auteur et son groupe de recherche */
	
	private List<Integer> sources;
	private HashMap<Integer,List<String>> listeVecteurs;
	private HashMap<Integer,String> listeGroupe;
	private HashMap<Integer,String> listeAuteurs;
	
	/* Plusieurs constructeurs sont possible :
	 * 
	 * S'il s'agit de la BD 48 Vecteurs(DB db) permet une construction par défaut
	 * 
	 * Sinon Vecteurs() puis s'il s'agit de 48 il faut utiliser addSource(int i) en fonction du type de publicaation à chercher (les numéros sont ceux de BibTex)
	 * Enfin (ou directement pour 48_2) lecture(...) permet de remplir les vecteurs
	 */
	
	public Vecteurs(DB db){
		sources = new Vector<Integer>();
		this.addSource(1);
		this.addSource(8);
		this.lecture(db, false, false);
	
	}
	
	public Vecteurs(){
		sources = new Vector<Integer>();
	}
	
	public void addSource(int sourceNb){
		
		sources.add(sourceNb);
		
	}
	
	/* S'il s'agit de 48_2 mettre db2 à true (false si 48) dans ce cas keywords peut être mis à true pour rajouter les keywords dans la liste de mots utilisables  */
	public void lecture(DB db, boolean db2, boolean keywords){
		
		
		
		this.listeVecteurs = new HashMap<Integer,List<String>>();
		this.listeGroupe = new HashMap<Integer,String>();
		this.listeAuteurs = new HashMap<Integer,String>();
		
		
		//Requete SQL
		
		ResultSet rset = null;
		if(!db2){
			String dbSource = "(";
			for(int i : sources)
			{
				dbSource += i +", ";
			}
			dbSource = dbSource.substring(0, dbSource.length()-2);
			dbSource += ")";
			
			rset = db.query("SELECT entries_id, title, author FROM entries WHERE entry_types_id  IN " + dbSource +" GROUP BY title");
			
			
		}else{
			rset = db.query("SELECT title, author, keywords, SUBSTRING(hal_id,5,8) AS entries_id FROM keywords");
		}
		
		
		
		//Traitement :
		
		try {
		
		DetectorFactory.loadProfile("data/profiles"); //pour la détection de language
		
		while (rset.next()){
			//Pour chaque publication
			String t = rset.getString("title").replace("{","").replace("}","").toLowerCase(); 
			String k = "";
			// on rajoute eventuellement les keywords
			if(db2 && keywords){
				if(rset.getString("keywords") != null){
					k = rset.getString("keywords").replace("-", " ").toLowerCase();
					
				}
			}
			
			/* Detection du language : le français est enlevé (les publications sont soit en français soit en anglais) */
			
			Detector detector = DetectorFactory.create();
			detector.append(t);
			String lang = detector.detect();
			
			if(lang.equals("fr")) {
				continue;
			}
			
			/* Lemmatisation */
			Sentence sent;
			
			if(keywords){
				 sent = new Sentence(t + " " + k);
			}else {
				 sent = new Sentence(t);
				 
			}
			
			int id = rset.getInt("entries_id");
			
			List<String> lemmas = sent.lemmas(); //ici
			
			/* Supression des mots indésirables */
			 
			List<String> words = new Vector<String>();
			 Stopwords stop = new Stopwords();
			 Vector<String> swords = stop.getWords();
				
				for (String s : lemmas){
					
					if(s.contains("-based")){s = s.replace("-based", "");}
					//On s'assure aussi d'éviter les doublons de mot dans un titre
					if(!swords.contains(s) && !words.contains(s)){
						words.add(s);
					}
					
					
				}
			
			//Si la liste de mots existe déja à l'identique on considère qu'il s'agit d'un doublon et on ne la prend pas en compte
			if(listeVecteurs.containsValue(words)){
				continue;}
			this.listeVecteurs.put(id, words);
			
			/*Remplissage des map des groupes et des auteurs gràce à la classe Groups */
			
			Groups groupes = new Groups();
			
			this.listeGroupe.put(id, groupes.getGroupFromAuthor(rset.getString("author"))); 
			
			this.listeAuteurs.put(id, groupes.getPersonnelName(rset.getString("author")));

			
		}
				
		}catch(SQLException e) {
			e.printStackTrace();
    	} catch (LangDetectException e) {
			e.printStackTrace();
		}
	}
	
	/* Possibilité de sauvegarder au format texte via la classe CSVWriter */
	public void savefile(String savepath, String separator)
	{
		
		CSVWriter writer = null;
		writer = new CSVWriter(savepath, false);
		for (HashMap.Entry<Integer, List<String>> entry : listeVecteurs.entrySet())
		{
			writer.addLine(entry.getValue(), separator);
		}
		writer.closeWriter();
	}
	
	public void savefile(String savepath, String separator, String group)
	{
		
		CSVWriter writer = null;
		writer = new CSVWriter(savepath, false);
		
		for (HashMap.Entry<Integer, List<String>> entry : listeVecteurs.entrySet())
		{
			if (getGroup(entry.getKey()) != null && getGroup(entry.getKey()).equals(group))
			{
				writer.addLine(entry.getValue(), separator);
			}
		}
		writer.closeWriter();
	}
	
	/* Méthodes diverses pour récupérer les infos des vecteurs en fcontion d'une données*/
	
	public List<String> getVector(int id){
		return listeVecteurs.get(id);
	}
	
	public String getGroup(int id){
		return listeGroupe.get(id);
	}
	
	public String getAuthor(int id){
		return listeAuteurs.get(id);
	}
	
	public List<List<String>> getVectors(){
		List<List<String>> l = new Vector<List<String>>();
		for (HashMap.Entry<Integer, List<String>> entry : listeVecteurs.entrySet())
		{
		    l.add(entry.getValue());
		}
		
		return l;
		
	}
	
	
	public int getKeyVecteurs(List<String> value){
	    for(int key : listeVecteurs.keySet()){
	        if(listeVecteurs.get(key).equals(value)){
	            return key;
	        }
	    }
	    return 0;
	}
	
	public int getKeyAuteurs(String value){
	    for(int key : listeAuteurs.keySet()){
	        if(listeAuteurs.get(key).equals(value)){
	            return key;
	        }
	    }
	    return 0;
	}
	
	public int getKeyGroupe(String value){
	    for(int key : listeGroupe.keySet()){
	        if(listeGroupe.get(key).equals(value)){
	            return key;
	        }
	    }
	    return 0;
	}
	
	public HashMap<Integer, List<String>> getListeVecteurs(){
		return listeVecteurs;
	}
	
	//Méthodes permettant de vérifier le résultat
	public void display(){
		for (HashMap.Entry<Integer, List<String>> entry : listeVecteurs.entrySet())
		{
		    System.out.println(entry.getKey() + "[" + listeGroupe.get(entry.getKey()) + "]" + " : " + entry.getValue());
		}
	} 
	
	public int getSize(){
		return listeVecteurs.size();
	}
}
