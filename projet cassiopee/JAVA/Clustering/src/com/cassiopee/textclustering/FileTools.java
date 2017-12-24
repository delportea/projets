package com.cassiopee.textclustering;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import text.Vecteurs;

public class FileTools {
public static int countTitles(File file){
		
		BufferedReader br = null;
		int count = 0;
		
		try{
			String currentLine;
			br = new BufferedReader(new FileReader(file.getPath()));
			while((currentLine = br.readLine()) != null){
				if(currentLine.length() > 0){
					count++;
				}
			}
		}catch(IOException e){
			e.printStackTrace();
		}finally{
			try {
				if (br != null)br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return count;
	}

	public static int countWords(File file){
		
		BufferedReader br = null;
		int count = 0;
		
		try{
			String currentLine;
			br = new BufferedReader(new FileReader(file.getPath()));
			while((currentLine = br.readLine()) != null){
				if(currentLine.length() > 0){
					count+=currentLine.split(",").length;
				}
			}
		}catch(IOException e){
			e.printStackTrace();
		}finally{
			try {
				if (br != null)br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return count;
	}
	
	public static List<File> recurseDirs(String dir, List<File> files, boolean dirPres){
		dir = dir.replace("/", "\\");
		File repertoire = new File(dir);
		String list[] = null;
		if (repertoire.isDirectory()){
			list = repertoire.list();
			for(int i = 0;i < list.length; i++){
				recurseDirs(dir + File.separatorChar + list[i],files, dirPres);
			}
		}
		if(dirPres){
			files.add(new File(dir));
		} else if(dir.contains(".txt")){
			files.add(new File(dir));
		}
		return files;
	}
	
	public static String getClusterName(File file, int nbrNames, int mode){
		BufferedReader br = null;
		List<List<String>> titles = new ArrayList<List<String>>();
		try{
			String currentLine;
			br = new BufferedReader(new FileReader(file.getPath()));
			while((currentLine = br.readLine()) != null){
				List<String> titre = new ArrayList<String>();
				if(currentLine.length() > 0){
					for(String word : currentLine.split(",")){
						titre.add(word);
					}
				}
				titles.add(titre);
			}
		}catch(IOException e){
			e.printStackTrace();
		}finally{
			try {
				if (br != null)br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		HashMap<String, Integer> hmap = new HashMap<String, Integer>();
        
		for(int i = 0;i<titles.size();i++){
			String clusterString = titles.toArray()[i].toString().replace("[", "").replace("]", "").replace(" ", "").replace(",", " ");
	        String[]  words = clusterString.split(" ");
	        
	        for(int j = 0; j<= words.length-1;j++){
	        	if(!hmap.containsKey(words[j])){
	        		hmap.put(words[j], 1);
	        	} else {
	        		final int nbr = hmap.get(words[j]);
	        		hmap.remove(words[j]);
	        		hmap.put(words[j], nbr + 1);
	        	}
	        }
		}
        
		Set<Map.Entry<String, Integer>> wordSet = hmap.entrySet();
		ValueComparator comparateur = new ValueComparator(hmap);
		TreeMap<String,Integer> mapTriee = new TreeMap<String,Integer>(comparateur);
		mapTriee.putAll(hmap);
				
        String[] motsTries = mapTriee.keySet().toString().replace("[", "").replace("]", "").replace(" ", "").split(",");
        String motLePlusFrequent="";
        if(mode==1){
        	motLePlusFrequent = "<tspan x='0' y='-"+((nbrNames*10)/4)+"px'>"+motsTries[0]+"</tspan>";
        	if(nbrNames>motsTries.length){
        		for(int i=1;i<motsTries.length;i++){
        			motLePlusFrequent += "<tspan x='0' dy='1em'>" + motsTries[i] + "</tspan>";
        		}
        	} else {
        		for(int i=1;i<nbrNames;i++){
        			motLePlusFrequent += "<tspan x='0' dy='1em'>" + motsTries[i] + "</tspan>";
        		}
        	}
        }else if(mode==2){
        	motLePlusFrequent = motsTries[0];
        	if(nbrNames>motsTries.length){
        		for(int i=1;i<motsTries.length;i++){
        			motLePlusFrequent += motsTries[i];
        		}
        	} else {
        		for(int i=1;i<nbrNames;i++){
        			motLePlusFrequent += motsTries[i];
        		}
        	}
        }
        
//        motLePlusFrequent += "</text>";
        
		return motLePlusFrequent;
	}
	
	public static String getClusterGroupName(File dir){
		List<File> files = new ArrayList<File>();
		files = recurseDirs(dir.getPath(),files,false);
		int count = 0;
		BufferedReader br = null;
		List<List<String>> titles = new ArrayList<List<String>>();
		try{
			for(File file : files){
				String currentLine;
				br = new BufferedReader(new FileReader(file.getPath()));
				while((currentLine = br.readLine()) != null){
					List<String> titre = new ArrayList<String>();
					if(currentLine.length() > 0){
						for(String word : currentLine.split(",")){
							titre.add(word);
						}
					}
					titles.add(titre);
					count++;
				}
			}
		}catch(IOException e){
			e.printStackTrace();
		}finally{
			try {
				if (br != null)br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		
		HashMap<String, Integer> hmap = new HashMap<String, Integer>();
        
		for(int i = 0;i<titles.size();i++){
			String clusterString = titles.toArray()[i].toString().replace("[", "").replace("]", "").replace(" ", "").replace(",", " ");
	        String[]  words = clusterString.split(" ");
	        
	        for(int j = 0; j<= words.length-1;j++){
	        	if(!hmap.containsKey(words[j])){
	        		hmap.put(words[j], 1);
	        	} else {
	        		final int nbr = hmap.get(words[j]);
	        		hmap.remove(words[j]);
	        		hmap.put(words[j], nbr + 1);
	        	}
	        }
		}
        
		Set<Map.Entry<String, Integer>> wordSet = hmap.entrySet();
        String motLePlusFrequent = null;
        int nbrDeFois = 0;
        for(Entry<String, Integer> word : wordSet){
        	if(word.getValue()>nbrDeFois){
        		motLePlusFrequent = word.getKey();
        		nbrDeFois = word.getValue();
        	}
        }
		return motLePlusFrequent;
	}
	
	public static String getFileContent(File file){
		BufferedReader br = null;
		String content = null;
		try{
			String currentLine;
			br = new BufferedReader(new FileReader(file.getPath()));
			content = br.readLine();
		}catch(IOException e){
			e.printStackTrace();
		}finally{
			try {
				if (br != null)br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return content;
	}
	
	public static void setFileContent(File file, String content){
		try{
			file.delete();
			final FileWriter fw = new FileWriter(file);
			fw.write(content);
			fw.close();
		}catch(IOException exception){
			System.out.println ("Erreur lors de la lecture : " + exception.getMessage());
		}
	}

	public static String getTitlesFormatted(File file, int mode) {
		BufferedReader br = null;
		BufferedReader br2 = null;
		String titles = "";
		try{
			String currentLine;
			int count=0;
			br = new BufferedReader(new FileReader(file.getPath()));
			while((currentLine = br.readLine()) != null){
				count++;
			}
			br2 = new BufferedReader(new FileReader(file.getPath()));
			if(mode==1){
				titles = "<tspan x='0' y='-"+((count*14)/4)+"px'>"+br2.readLine()+"</tspan>";
				while((currentLine = br2.readLine()) != null){
					titles += "<tspan x='0' dy='1.2em'>"+currentLine+"</tspan>";
				}
			}else if (mode==2){
				titles = br2.readLine();
				while((currentLine = br2.readLine()) != null){
					titles += currentLine;
				}
			}
		}catch(IOException e){
			e.printStackTrace();
		}finally{
			try {
				if (br != null)br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return titles;
	}
	
	public static List<String> toVector(String s){
		List<String> list = new ArrayList<String>();
		for(String mot : s.split(","))
			list.add(mot);
		return list;
	}

	public static String getIds(File file, Vecteurs v) {
		BufferedReader br = null;
		String s = "";
		try{
			String currentLine;
			br = new BufferedReader(new FileReader(file.getPath()));
			while((currentLine = br.readLine()) != null){
				if(currentLine.length() > 0){
					s+=v.getKeyVecteurs(FileTools.toVector(currentLine))+",";
				}
			}
			s = s.substring(0, s.length()-1);
		}catch(IOException e){
			e.printStackTrace();
		}finally{
			try {
				if (br != null)br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return s;
	}
}
