package com.cassiopee.textclustering;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.orhandemirel.clustering.Kmeans;

import text.Vecteurs;

public class Clusters {

	private String dataSrc;
	private int	clustersNbr;
	private List<List<String>> inputSet;
	private List<List<String>> firstInputSet;
	private List<List<Double>> inputSetVectorized;
	private List<List<List<String>>> resultClusters;
	private List<String> dictionary;
	private List<List<Double>> distanceMatrix;
	private List<List<List<Double>>> clusters;
	private	boolean comments;
	private File dir;

	public Clusters(int clustersNbr, String dataSrc, boolean comments, File dir){
		this.clustersNbr = clustersNbr;
		this.dataSrc = dataSrc;
		this.inputSet = new ArrayList<List<String>>();
		this.firstInputSet = new ArrayList<List<String>>();
		this.inputSetVectorized = new ArrayList<List<Double>>();
		this.distanceMatrix =  new ArrayList<List<Double>>();
		this.dictionary = new ArrayList<String>();
		this.clusters = new ArrayList<List<List<Double>>>();
		this.comments = comments;
		this.dir = dir;
		
		BufferedReader br = null;
		
		try{
			String currentLine;
			br = new BufferedReader(new FileReader(dataSrc));
			while((currentLine = br.readLine()) != null){
				List<String> titre = new ArrayList<String>();
				if(currentLine.length() > 0){
					for(String word : currentLine.split(",")){
						titre.add(word);
					}
				}
				firstInputSet.add(titre);
				inputSet.add(titre);
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
	}
	
	public Clusters(int clustersNbr, List<String> titles, boolean comments, File dir){
		this.clustersNbr = clustersNbr;
		this.inputSet = new ArrayList<List<String>>();
		this.firstInputSet = new ArrayList<List<String>>();
		this.inputSetVectorized = new ArrayList<List<Double>>();
		this.distanceMatrix =  new ArrayList<List<Double>>();
		this.dictionary = new ArrayList<String>();
		this.clusters = new ArrayList<List<List<Double>>>();
		this.comments = comments;
		this.dir = dir;
		
		for(String title : titles){
			List<String> titre = new ArrayList<String>();
			for(String word : title.replace("[", "").replace("]", "").split(";")){
				titre.add(word);
			}
			inputSet.add(titre);
			firstInputSet.add(titre);
		}
	}
	
	public void createDictionary(){
		for(List<String> title : inputSet){
			for(String word : title){
				dictionary.add(word);
			}
		}
		Set<String> mySet = new HashSet<String>(dictionary);
		List<String> dictionary2 = new ArrayList<String>(mySet);
		dictionary = dictionary2;
		Collections.sort(dictionary);
	}

	public void vectorizeData(){
		for(List<String> title : inputSet){
			List<Double> vectorizedTitle = new ArrayList<Double>();
			for(String component : dictionary){
				if(title.contains(component)){
					vectorizedTitle.add((double) 1);
				} else {
					vectorizedTitle.add((double) 0);
				}
			}
			inputSetVectorized.add(vectorizedTitle);
		}
	}

	private double[][] inputSetVectorizedToMatrix(){
		double[][] data = new double[inputSetVectorized.size()][dictionary.size()];
		for(int i=0;i<=inputSetVectorized.size()-1;i++){
			for(int j=0;j<=dictionary.size()-1;j++){
				data[i][j] = inputSetVectorized.get(i).get(j);
			}
		}
		return data;
	}

	public void create(int distance){
		System.out.println("Opération de clustering en cours ...");
		long start = System.nanoTime();
		double[][] data = inputSetVectorizedToMatrix();
		Kmeans kmeans = new Kmeans(data,clustersNbr,distance);
		kmeans.calculateClusters();
		clusters = kmeans.getClusters();
		List<List<List<String>>> newClusters = new ArrayList<List<List<String>>>();
		for(int i = 0;i<=clustersNbr-1;i++){
			newClusters.add(new ArrayList<List<String>>());
		}
		for(List<List<Double>> cluster : clusters){
			List<Integer> listTitles = new ArrayList<Integer>();
			for(List<Double> titre : cluster){
				listTitles.add(inputSetVectorized.indexOf(titre));
			}
			List<List<String>> newCluster = new ArrayList<List<String>>();
			for(int id : listTitles){
				newCluster.add(firstInputSet.get(id));
			}
			newClusters.set(clusters.indexOf(cluster), newCluster);
		}	
		resultClusters = newClusters;
		if(comments){
			int titlesNbr = 0;
			for(List<List<String>> titres : resultClusters){
				titlesNbr+=titres.size();
			}
			System.out.println("Temps de calcul : " + (System.nanoTime() - start)/1000000000 + " secondes");
			System.out.println(titlesNbr + " titres ont été classés");
			System.out.println(resultClusters);
		}
		System.out.println("Opération de clustering terminée !");
	}
	 
	public static void subClusterize(String srcDir, int max, int distance){
		System.out.println("Opération de sous-clustering en cours ...");
		int i = 0;
		boolean running = true;
		while(running){
			List<File> files = new ArrayList<File>();
			for(final File file : FileTools.recurseDirs(srcDir, files, false)){
				int length = FileTools.countTitles(file);
				if(length>max){
					int taille = (int) Math.floor((Math.sqrt(length/2)));
					Clusters subCluster = new Clusters(taille, file.getPath(), false, new File(file.getPath().replace(".txt","/")));
					subCluster.createDictionary();
					subCluster.vectorizeData();
					subCluster.create(distance);
					subCluster.save();
					file.delete();
				}
			}
			files = new ArrayList<File>();
			int nbrBadClusters=0;
			for(final File file : FileTools.recurseDirs(srcDir, files, false)){
				int length = FileTools.countTitles(file);
				if(length>max){
					nbrBadClusters++;
				}
			}
			if(nbrBadClusters==0){
				running = false;
			}
			i++;
		}
		System.out.println("Opération de sous-clustering terminée avec " + i + " étapes.");
	}
	
	private static boolean deleteDir(File dir){
		if (dir.isDirectory()) {
			String[] children = dir.list();
			for (int i = 0; i < children.length; i++){
			boolean success = deleteDir(new File(dir, children[i]));
			if (!success) 
				return false;	
			}
		}
		return dir.delete();
	}

	public void saveDictionary(String dictionaryDest){
		try{
			final File f = new File(dictionaryDest + "/Dictionary.txt");
			final FileWriter fw = new FileWriter (f);
			for(String word : dictionary){
				fw.write(word);
				fw.write("\r\n");
			}
			fw.close();
		} catch (IOException exception){
			System.out.println ("Erreur : " + exception.getMessage());
		}
	}
	
	public void saveInputSet(String inputSetDest){
		try{
			final File f = new File(inputSetDest + "/InputSet.txt");
			final FileWriter fw = new FileWriter (f);
			for(List<String> vector : inputSet){
				fw.write(vector.toString().replace("[", "").replace("]", "").replace(" ", ""));
				fw.write("\r\n");
			}
			fw.close();
		} catch (IOException exception){
			System.out.println ("Erreur : " + exception.getMessage());
		}
	}

	public void saveInputSetVectorized(String inputSetVectorizedDest){
		try{
			final File f = new File(inputSetVectorizedDest + "/InputSetVectorized.txt");
			final FileWriter fw = new FileWriter (f);
			for(List<Double> vector : inputSetVectorized){
				fw.write(vector.toString());
				fw.write("\r\n");
			}
			fw.close();
		} catch (IOException exception){
			System.out.println ("Erreur : " + exception.getMessage());
		}
	}
	
	public void save(){
		deleteDir(dir);
		dir.mkdirs();
		int idCluster = 0;
		for(List<List<String>> cluster : resultClusters){
			try{
				final File f = new File(dir.getPath() + "/cluster" + idCluster + ".txt");
				final FileWriter fw = new FileWriter(f);
				for(List<String> titre : cluster){
					fw.write(titre.toString().replace("[", "").replace("]", "").replace(" ", ""));
					fw.write("\r\n");
				}
				fw.close();
			}catch(IOException exception){
				System.out.println ("Erreur lors de la lecture : " + exception.getMessage());
			}
			idCluster++;
		}
		if(comments){
			System.out.println("Les clusters ont bien été extrait dans le répertoire " + dir.getPath() + ".");	
		}
	}
	
	public static void generateVisual(String src, String destination, int mode, Vecteurs v){
		System.out.println("Génération du visuel en cours ...");
		//On crée pour chaque groupe de cluster un flare.json
		List<File> files = new ArrayList<File>();
		files = FileTools.recurseDirs(src, files, true);
		for(File file : files){
			if(file.isDirectory())
				generateFlareJSON(file.getPath()+"/",file.getPath()+"/", mode, v);
		}
		
		files = FileTools.recurseDirs(src, files, true);
		List<File> flareList = new ArrayList<File>();
		for(File file : files){
			if(file.getName().equals("flare.json"))
				flareList.add(file);
		}
		
		//On compacte tous les flare.json dans un seul (en remplaçant des chaines de caractères à des endroits précis
		for(File flare : flareList){
			for(String str : FileTools.getFileContent(flare).split("\"")){
				String srcFlare = null;
				if(str.contains("Remplacer par")){
					srcFlare = str.replace("Remplacer par ", "");
					String newContent = FileTools.getFileContent(flare).replace("\"Remplacer par "+srcFlare+"\"", FileTools.getFileContent(new File(srcFlare))).replace(",]", "]");
					FileTools.setFileContent(flare, newContent);
				}
			}
		}
		
		//On crée le flare.json final dans le dossier de destination
		File res = new File(destination+"/flare.json");
		FileTools.setFileContent(res, FileTools.getFileContent(new File(src+"/flare.json")));
		
		//On supprime les flare.json contenus dans chaque sous-cluster (ils sont temporaires)
		for(File flare : flareList){
			flare.delete();
		}
		System.out.println("Visuel généré !");
	}
	
	public static void generateFlareJSON(String src, String destination, int mode, Vecteurs v){
		File flare = new File(destination+"flare.json");
		File dir = new File(destination);
		flare.delete();
		List<File> files = new ArrayList<File>();
		files = invertFileList(FileTools.recurseDirs(src, files, true));
		try{
			final File f = new File(destination + "flare.json");
			final FileWriter fw = new FileWriter(f);
			fw.write("{");
			fw.write("\"name\": \""+FileTools.getClusterGroupName(new File(src))+"\",");
			fw.write("\"children\": [");
			for(File file : files){
				if(!file.isDirectory() && file.getPath().replace(src.replace("/", file.separator), "").replace(file.separator, "/").split("/").length==1){
					if(files.indexOf(file) < files.size()-1){
						fw.write("{\"name\": \""+FileTools.getClusterName(file,2,mode)+"\", \"child\": \"true\", \"ids\": \""+FileTools.getIds(file,v)+"\", \"nbTitles\": \""+FileTools.countTitles(file)+"\", \"children\":[{\"name\": \""+FileTools.getClusterName(file,4,mode)+"\", \"size\": "+(int) Math.floor((Math.log(FileTools.countTitles(file))+1)*1000)+"}]},");
					}else{
						fw.write("{\"name\": \""+FileTools.getClusterName(file,2,mode)+"\", \"child\": \"true\", \"ids\": \""+FileTools.getIds(file,v)+"\", \"nbTitles\": \""+FileTools.countTitles(file)+"\", \"children\":[{\"name\": \""+FileTools.getClusterName(file,4,mode)+"\", \"size\": "+(int) Math.floor((Math.log(FileTools.countTitles(file))+1)*1000)+"}]}");
					}
				} else if (file.getPath().replace(src.replace("/", file.separator), "").replace(file.separator, "/").split("/").length==1){
					if(files.indexOf(file) < files.size()-1){
						fw.write("{\"name\": \""+FileTools.getClusterGroupName(file)+"\", \"child\": \"false\", \"children\":[\"Remplacer par "+src.replace("\\", "/")+file.getName()+"/flare.json\"]},");
					}else{
						fw.write("{\"name\": \""+FileTools.getClusterGroupName(file)+"\", \"child\": \"false\", \"children\":[\"Remplacer par "+src.replace("\\", "/")+file.getName()+"/flare.json\"]}");
					}
				}
			}
			fw.write("]");
			fw.write("}");
			fw.close();
		}catch(IOException exception){
			System.out.println ("Erreur lors de la lecture : " + exception.getMessage());
		}
	}

	private static List<File> invertFileList(List<File> files){
		List<File> newFiles = new ArrayList<File>();
		for(int i = files.size()-1;i>=0;i--){
			newFiles.add(files.get(i));
		}
		return newFiles;
	}
	
	public void createDistanceMatrix(int distance){
		double[][] data = inputSetVectorizedToMatrix();
		for(double[] datum : data){
			List<Double> liste = new ArrayList<Double>();
			for(double[] datum2 : data){
				if(distance==0){
					liste.add(Kmeans.distanceEuclidian(datum,datum2));
				} else {
					liste.add(Kmeans.distanceCos(datum,datum2));
				}
			}
			distanceMatrix.add(liste);
		}
	}
	
	public void saveDistanceMatrix(String distanceMatrixDest){
		try{
			final File f = new File(distanceMatrixDest + "/DistanceMatrix.txt");
			final FileWriter fw = new FileWriter (f);
			for(List<Double> vector : distanceMatrix){
				fw.write(vector.toString());
				fw.write("\r\n");
			}
			fw.close();
		} catch (IOException exception){
			System.out.println ("Erreur : " + exception.getMessage());
		}
	}
	
	public List<List<Double>> getDistanceMatrix(){
		return distanceMatrix;
	}
	
	public double[][] distanceMatrixToArray(){
		double[][] data = new double[distanceMatrix.size()][distanceMatrix.size()];
		for(int i=0;i<=distanceMatrix.size()-1;i++){
			for(int j=0;j<=distanceMatrix.size()-1;j++){
				data[i][j] = distanceMatrix.get(i).get(j);
			}
		}
		return data;
	}
	
	public String getDataSrc(){
		return dataSrc;
	}

	public int getClustersNbr(){
		return clustersNbr;
	}

	public List<List<List<String>>> getResultClusters(){
		return resultClusters;
	}
	
	public List<List<String>> getInputSet(){
		return inputSet;
	}
	
	public List<List<Double>> getInputSetVectorized(){
		return inputSetVectorized;
	}
	
	public List<String> getDictionary(){
		return dictionary;
	}
	
	public List<List<List<Double>>> getClusters(){
		return clusters;
	}
	
	public File getDir() {
		return dir;
	}
	
}