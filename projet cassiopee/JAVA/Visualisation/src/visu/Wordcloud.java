package visu;



import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

/*
 * Cette classe permet de générer un nuage de mots dynamique afin de visuliser les mots les plus fréquents dans un cluster
 */

public class Wordcloud {
	
	public Wordcloud(List<List<List<String>>> clusterss, String path ){
		
		int i = 0;
		
		for(List<List<String>> cluster : clusterss){
			
			i++;
			HashMap<String,Integer> cmap = new HashMap<String,Integer>();
			
			File file;
			
			FileWriter fw;
			BufferedWriter bw = null; 
			
			
			file = new File(path +"/Cluster" + i + ".html");
			

			try{
				if(file.exists()){
					file.delete();
				}
				
				file.getParentFile().mkdirs();
				file.createNewFile();
				
				
				
				fw = new FileWriter(file.getAbsoluteFile(), true);
				bw = new BufferedWriter(fw);
				
			}catch(IOException e){
				e.printStackTrace();
			}
			
			
			try{
				String line = "<!DOCTYPE html><html lang=\"en\"><head><meta charset=\"utf-8\"><title>Clusters " + i +"</title><meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\"></head>";
				bw.write(line);
				line = "<body><div id='sourrounding_div' style='width:80em;height:500px'><canvas id='canvas' class='wordcloud'></canvas></div><script type='text/javascript' src='wordcloud2.js'></script><script type='text/javascript'>var div = document.getElementById('sourrounding_div');var canvas = document.getElementById('canvas');canvas.height = div.offsetHeight;canvas.width  = div.offsetWidth;";
				line +="var wF = 10;var mS = 1;";
				bw.write(line);
				line = "var list =";
				bw.write(line);
				
				
			}catch(IOException e){
				e.printStackTrace();
			}
			
			int nb = 0;
			for(List<String> titre : cluster){
				nb++;
				
				for(String mot : titre){
					if(cmap.containsKey(mot)){
						cmap.replace(mot, cmap.get(mot)+1);
					}else{
						cmap.put(mot, 1);
					}
					
				}
				
			}

			
			try{
				String line = "[";
				bw.write(line);
				
				int b = 0;
				for (String entry : cmap.keySet())
				{	
					b++;
					
					line = "[\"" + entry + "\", \"" + cmap.get(entry) +"\"]";
					
				    if (b != cmap.size())
				    {
				    	line += ",";
				    }
				    
				    bw.write(line);
					
				}
				line = "\n];";
				bw.write(line);
				

				line = "var options = {list :list,";
                line +="weightFactor: wF,minSize: mS * wF,rotateRatio: 0} \nWordCloud(document.getElementById('canvas'), options);"; 
                bw.write(line);
                
                line ="function changewf(newVal){wF = newVal;var options = {list : list,weightFactor: wF,minSize: mS * wF,rotateRatio: 0} \nWordCloud(document.getElementById('canvas'), options);}";
                bw.write(line);
                line ="function changems(newVal){mS = newVal;var options = {list : list,weightFactor: wF,minSize: mS * wF,rotateRatio: 0} \nWordCloud(document.getElementById('canvas'), options);}</script>";
                bw.write(line);
                
                line = "<p>" + nb +" entrees </p><p>Weight Factor: <input id='wf' type='range' value='10' max='50' min='1' step='5'  oninput='changewf(this.value)'></p><p>Min Size: <input id='ms' type='range' value='1' max='10' min='0' step='1'  oninput='changems(this.value)'></p>";
                bw.write(line);
                
                if (i == 1){
                	    line="<a href='cluster" + (i+1) +".html'>Suivant</a></body></html>";
                	    bw.write(line);
                }else if(i == clusterss.size()){
                	line="<a href='cluster" + (i-1) +".html'>Precedent</a>    </body></html>";
                	bw.write(line);
                }else{
                	line="<a href='cluster" + (i-1) +".html'>Precedent</a>    <a href='cluster" + (i+1) +".html'>Suivant</a></body></html>";
                	bw.write(line);
                }
				
			}catch(IOException e){
				e.printStackTrace();
			}
			
			
			try {
				bw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	

	

	

}
