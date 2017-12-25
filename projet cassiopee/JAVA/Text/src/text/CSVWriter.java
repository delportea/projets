package text;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/* CSVWriter*/
/*
 * Cette classe permet l'�criture des r�sultat dans un fichier ligne par ligne en sp�cifiant le carract�re de s�paration  
 * Initialisation avec le path vers le fichier puis �criture avec addline(List<String> l)
 * Il faut mieux fermer apr�s utilisation avec closeWriter()
 */

public class CSVWriter {
	
	File file;
	
	FileWriter fw;
	BufferedWriter bw; 

	public CSVWriter(String path){
		this.initiate(path, false);
	}
	
	/* Permet d'�crire � la fin du fichier*/ 
	public CSVWriter(String path, boolean append){
		this.initiate(path, append);
	}
	
	private void initiate(String path, boolean append){
		
		file = new File(path);
		try{
			if(!file.exists()){
				file.createNewFile();
			}
			
			fw = new FileWriter(file.getAbsoluteFile(), append);
			bw = new BufferedWriter(fw);
			
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	/* Fonction principale d'�criture (par d�faut ',' est le s�parateur  */
	public void addLine(List<String> values){
		try{
			String line = "";
			for(String value : values ){
				line += value + ",";
			}
			
			
			bw.write(line.substring(0,line.length()-1));
			bw.newLine();
			
			
		}catch(IOException e){
			e.printStackTrace();
		}
		
	}
	
	/* Choix du s�parateur */
	public void addLine(List<String> values, String separator){
		try{
			String line = "";
			for(String value : values ){
				line += value + separator;
			}
			
			
			bw.write(line.substring(0,line.length()-1));
			bw.newLine();
			
			
		}catch(IOException e){
			e.printStackTrace();
		}
		
	}
	
	public void closeWriter(){
		try {
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
