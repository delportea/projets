package text;


import java.io.File;
import java.sql.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Vector;

import edu.stanford.nlp.simple.Sentence;

/*GROUPS*/

/* 
 * Cette classe utilisable de mani�re ind�pendante permet de r�cup�rer toutes les informations concerant les �quipes de recherche (== groupe, dans ce projet)
 * 
 */

public class Groups {
 
	HashMap<String,String> perso; //key = auteur, Value = groupe
	
	/* 
	 * L'initialisation remplie la map avec l'association entre auteurs et �quipe de la BD 48 
	 */
	public Groups(){
    	
    	DB db = new DB("48");
    	
    	try{
    		
    		ResultSet persSet = db.query("SELECT DISTINCT nomprenom, equipe FROM personnel");
    		
    		perso  = new HashMap<String,String>();
    		
    		while(persSet.next()){
    			/* Traitement n�c�ssaire pour standardiser l'�criture des noms et prenoms entre les diff�rentes sources */
    			String s = persSet.getString("nomprenom").replace("{\\'e}", "e").replace("{\\c{c}}", "c").replace("{\\`ene}", "e").replace("{\\^o}", "o").replace("{\\~a}", "a").replace("{\\`e}", "e").replace("{^i}", "i");
    			s = s.replace(",", "").replace("\\", "");
    			s.replace("\\", "").replace("{\\\"u}", "u").replace("{\\?i}", "i").replace("{\\^i}", "i").replace("?", "").replace("{\\\"O}", "o");
    			s = s.toLowerCase(Locale.ROOT);
    			
    			perso.put(s, persSet.getString("equipe"));
    		}
    	}catch(SQLException e){
    		e.printStackTrace();
    	} 
		
    	db.close();
  
    }
    
	/* Renvoie le groupe associ� � un nom*/
    public String getGroupFromAuthor(String author){
    	
    	String s = author;
    	String result = "";
    	if(s != null){
    		//m�me traitement de standardisation
			s = s.replace("\\bf", "").replace("{\\'e}", "e").replace("{\\c{c}}", "c").replace("{\\`ene}", "e").replace("{\\^o}", "o").replace("\\underline", "").replace("{\\~a}", "a").replace("{\\`e}", "e").replace("{^i}", "i");
			s = s.replace(",", "").replace("{", "").replace("}","").replace("\\", "");
			s = s.replace("ç", "c").replace("é", "e").replace("í", "i").replace("ü", "u").replace("Ó", "o").replace("ë", "e").replace("ä", "a").replace("è", "e").replace("ę", "e").replace("ö", "o").replace("á", "a").replace("ñ", "n").replace("ó", "o").replace("Ç", "c");
			s.replace("\\", "").replace("{\\\"u}", "u").replace("{\\?i}", "i").replace("{\\^i}", "i").replace("?", "").replace("{\\\"O}", "o");
			s = s.toLowerCase(Locale.ROOT);
			
			//Le nom est parfois �crit "nom prenom" ou l'inverse et il faut tennir compte des personnes ayant plusieurs noms ou pr�noms 
			for (String ss: s.split("and ")){
				
				 if(perso.containsKey(ss))
				 {
					 //cas simple ou le nom est �crit de mani�re �gale
					 result += perso.get(ss) + ';' ;
					 continue;
				 }else{
					//on inverse l'ordre des mots
					List<String> words = Arrays.asList( ss.split( "\\s" ));
					Collections.reverse( words );
					ss ="";
					for(String word : words){
						ss += word + " ";
					}
					ss = ss.substring(0, ss.length()-1);
					
					 if(perso.containsKey(ss))
    				 {
						// Cas ou l'ordre est simplement invers�
						 result += perso.get(ss) + ';' ;
						 continue;
    				 }else{
 							//Cas plus complexe on fait une recherche mot par mot. Si tous sont pr�sents dans le nom d'un auteur c'est qu'il sont �gaux
							for(String key : perso.keySet()){
								int i = 0;
								for(String word : words){
									if (!key.contains(word)){
										continue;
									}
									i++;
								}
								if(i == words.size()){
									result += perso.get(key) + ';' ;
									break;
								}
							}
	    				 }
				 }
		      }
    	
    	}
    	
    	if(result != "")
    		return result.substring(0,result.length()-1);
    	else
    		return null;
    	
    	
    		
    	
    }
    
    /* Retourne le nom au bon format si on lui passe un nom d'auteur (r�p�tition inutile de la m�thode pr�c�dente*/
    /* Seul le result est diff�rent */
public String getPersonnelName(String authors){
    	
    	String s = authors;
    	String result = "";
    	if(s != null){
			s = s.replace("\\bf", "").replace("{\\'e}", "e").replace("{\\c{c}}", "c").replace("{\\`ene}", "e").replace("{\\^o}", "o").replace("\\underline", "").replace("{\\~a}", "a").replace("{\\`e}", "e").replace("{^i}", "i");
			s = s.replace(",", "").replace("{", "").replace("}","").replace("\\", "").replace("'", "");
			s = s.replace("ç", "c").replace("é", "e").replace("í", "i").replace("ü", "u").replace("Ó", "o").replace("ë", "e").replace("ä", "a").replace("è", "e").replace("ę", "e").replace("ö", "o").replace("á", "a").replace("ñ", "n").replace("ó", "o").replace("Ç", "c");
			s.replace("\\", "").replace("{\\\"u}", "u").replace("{\\?i}", "i").replace("{\\^i}", "i").replace("?", "").replace("{\\\"O}", "o");
			s = s.toLowerCase(Locale.ROOT);
			
			for (String ss: s.split("and ")){
				
				 if(perso.containsKey(ss))
				 {
					  result += ss + ';' ;
					  continue;
				 }else{
					List<String> words = Arrays.asList( ss.split( "\\s" ));
					Collections.reverse( words );
					ss ="";
					for(String word : words){
						ss += word + " ";
					}
					ss = ss.substring(0, ss.length()-1);
					
					 if(perso.containsKey(ss))
    				 {
    					 result += ss + ';';
    					 continue;
    				 }else{
 						
							for(String key : perso.keySet()){
								int i = 0;
								for(String word : words){
									if (!key.contains(word)){
										continue;
									}
									i++;
								}
								if(i == words.size()){
									result += key + ';';
									break;
								}
							}
	    				 }
				 }
		      }
    	
    	}
    	if(result != "")
    		return result.substring(0,result.length()-1);
    	else
    		return null;
    	
    	
    		
    	
    }
    
/* Parcourt les publications de la base de donn�e et cr�e un fichier texte par groupe de recherche contenant la liste des titres des publications de ce groupe*/
/* Utilit� discutable */
/* les titres sont �crit sous forme du vecteur g�n�r�s par la classe Vecteurs*/
    public void lecture(String dbName){
    	
    	this.clear(dbName);
    	
    	DB db = new DB(dbName);
    	
    	try{

    		int nombrePubli = 1;
    		int publiAssoci�s = 0;
    		int nonAssoc =0;
    		int aNull = 0;
    		ResultSet rset = null;
    		if(dbName.equals("48")){
    			rset = db.query("SELECT DISTINCT author, title FROM entries WHERE entry_types_id  IN (1, 2, 6, 7, 18)");
    		}else if(dbName.equals("48_2")){
    			rset = db.query("SELECT DISTINCT author, title FROM keywords");
    		}
    		
    		
    		
    		while (rset.next()){
    		
    			String s = rset.getString("author");
 	
    			
    			if(s != null){
    			s = s.replace("\\bf", "").replace("{\\'e}", "e").replace("{\\c{c}}", "c").replace("{\\`ene}", "e").replace("{\\^o}", "o").replace("\\underline", "").replace("{\\~a}", "a").replace("{\\`e}", "e").replace("{^i}", "i");
    			s = s.replace(",", "").replace("{", "").replace("}","").replace("\\", "");
    			s = s.replace("ç", "c").replace("é", "e").replace("í", "i").replace("ü", "u").replace("Ó", "o").replace("ë", "e").replace("ä", "a").replace("è", "e").replace("ę", "e").replace("ö", "o").replace("á", "a").replace("ñ", "n").replace("ó", "o").replace("Ç", "c");
    			s.replace("\\", "").replace("{\\\"u}", "u").replace("{\\?i}", "i").replace("{\\^i}", "i").replace("?", "").replace("{\\\"O}", "o");
    			s = s.toLowerCase(Locale.ROOT);
    			
    			CSVWriter writer = null;
    			
    			boolean associ� = false;
    			for (String ss: s.split("and ")){
    				if(!associ�){
    				 if(perso.containsKey(ss))
    				 {
    					 writer = new CSVWriter("data\\" + dbName + "\\" + perso.get(ss) + ".csv", true);
    					 associ� = true;
    				 }else{
						List<String> words = Arrays.asList( ss.split( "\\s" ));
						Collections.reverse( words );
						ss ="";
						for(String word : words){
							ss += word + " ";
						}
						ss = ss.substring(0, ss.length()-1);
						
						 if(perso.containsKey(ss))
	    				 {
	    					 writer = new CSVWriter("data\\" + dbName + "\\" + perso.get(ss) + ".csv", true);
	    					 associ� = true;
	    				 }else{
						
							for(String key : perso.keySet()){
								int i = 0;
								for(String word : words){
									if (!key.contains(word)){
										continue;
									}
									i++;
								}
								if(i == words.size()){
									writer = new CSVWriter("data\\" + dbName + "\\" + perso.get(key) + ".csv", true);
			    					associ� = true;
			    					continue;
								}
							}
	    				 }
						
						
    				 }
    				}
    		         
    		      }
    			
    			if(associ�){publiAssoci�s++;}else{
    				nonAssoc++;
    				writer = new CSVWriter("data\\" + dbName + "\\" + "other.csv", true);
    			}
    			
    			String t = rset.getString("title").replace("{","").replace("}","");
    			Sentence sent = new Sentence(t);
    			
    			List<String> lemmas = sent.lemmas();
    			Stopwords stop = new Stopwords();
    			Vector<String> swords = stop.getWords();
    			
    			List<String> words = new Vector<String>();
    			
    			for (String i : lemmas){
    				
    				i = i.toLowerCase();
    				
    				if(!swords.contains(i)){
    					words.add(i);
    				}
   
    			}
    			
				writer.addLine(words);	
        		writer.closeWriter();
	
    			}else {
    			aNull++;
    			}
    			nombrePubli++;
    			
    		}

    		System.out.println(publiAssoci�s + " | " + (nombrePubli - 1 - publiAssoci�s)+ " | " + nonAssoc + " | " + aNull);
    	} catch(SQLException e){
    		e.printStackTrace();
    	} 
    		
    	db.close();
    	
    }
    
    //permet d'efffacer les pr�c�dents fichiers cr��s par lecture()
    public void clear(String dbName){
    	
    	DB db = new DB("48");
    	
    	try{
    		
    		ResultSet groupSet = db.query("SELECT DISTINCT equipe FROM personnel");
    		
    		while(groupSet.next()){
    			File f = new File("data\\" + dbName + "\\" + groupSet.getString("equipe") + ".csv");
    			if(f.exists()){
    				f.delete();
    			}
    		}
    		File f = new File("data\\" +  dbName + "\\" + "other.csv");
			if(f.exists()){
				f.delete();
			}
    		
    	}catch(SQLException e){
    		e.printStackTrace();
    	}
    		
    	db.close();
    	
    }
}