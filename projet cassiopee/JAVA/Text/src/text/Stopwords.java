package text;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Vector;


/* STOPWORDS */

/*
 * Le fichier data/stopwords.txt contient une liste de stopwords et plus généralement de mot indésirables que l'on ne veut pas. Cette classe permet de les utiliser
 */

public class Stopwords {

	private Vector<String> words;
	
	//Le constructeur initialise directement un vector avec la liste des mots indésirables
	public Stopwords(){
		
		words = new Vector<String>();
		BufferedReader br = null;
		
		try{
			String curLine;
			
			br = new BufferedReader(new FileReader("data/stopwords.txt"));
			while((curLine = br.readLine()) != null){
				if(curLine.length() > 0){
					words.add(curLine);
				}
			}
			
		}catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		
	}
	
	
	//retoune la liste pour utilisation
	public Vector<String> getWords(){
		return this.words;
	}
	
	//Methode "au cas où" :
	public void displayWords(){
		for(String s : words){
			System.out.println(s);
		}
	}
}
