package text;

import java.util.HashMap;
import java.util.List;
import java.util.Vector;



public class test {

	public static void main(String[] args) {

		/* NOTE IMPORTANTE*/
		/* Ce projet doit être compris comme un script, un brouillon qui permet de faire des tests et non pas comme un programme
		 * utilisable comme tel.
		 * 
		 */
		
		/* DEPENDANCES */
		/*
		 * Mysql connector pour Java (ou équivalent en fonction de la base de donnée)
		 * Stanford Core NLP (http://stanfordnlp.github.io/CoreNLP/simple.html) pour gérer la lemmatisation 
		 *   -> /!!\ la lib 'stanford-corenlp-3.6.0-models.jar' n'est pas incluse dans git (trop lourde) il faut donc la rajouter manuellement
		 * langdetect (https://github.com/shuyo/language-detection) pour le détection de language 
		 * 	 -> se référer à la documentation de la bibliothèque en particulier pour la création des profils
		 *   
		 *   
		 *   BASES DE DONNEES : Le pojet est très dépendant des bases de données et de leur format.
		 *   Nousa avions deux bases de données (appelées 48 et 48_2). 
		 *   La première est très proche du format BibTex, les tables intéressantes sont 'entries' -> les publications, 'personnel' -> liens entre un auteur et son groupe de recherche,
		 *   La deuxième ne contient que l'équivalent de la table 'entries' de 48 (mais avec des spécificitées)
		 *    L'interet de cette deuxième est de contenir des keywords alors que la première ne contient que le titre de la publication 
		 */
		
		/* CLASSES */
		/* 
		 * CSVWriter gère l'écriture dans un fichier
		 * DB gère l'initialisation de la connexion avec une base de données
		 * Groups gère le matching d'un auteur avec un groupe de recherche (acmes, tipic, r3s ou methodes)
		 * Publi pemet d'utiliser la base de donnée de production (au format pré-traité)
		 * Stopwords gère l'identification des mots indésirables 
		 * Vecteurs gère tout le reste du processus de traitement de texte
		 * 
		 * test et script sont des exemples d'utilisation (tirés de précédents tests dont l'utilité peut varier)    
		 */
	
		/* EXEMPLES*/
		
		//Utilisation simple
		
		DB db = new DB("48_2"); //initialise la lecture de BD
	
		
		
		Vecteurs v =  new Vecteurs();
		v.lecture(db, true, true); //remplissage des vecteurs
		v.savefile("data/newKey.txt", ";"); //sauvegarde au format voulue
		System.out.println(v.getSize());
		
		db.close(); //ferme la connexion
		
		
		//Utiliation combinée avec la création de clusters (Projet Clustering)
		/* N'est pas conseillé ! Clusering utilise le projet Text, pas l'inverse, peut être utile seulement pour vérifier les résultats */
		
		/*Clusters clusters = new Clusters(3, "data/titles_test1.txt"); //le chemin peut être celui précédememnt utilisé pour v.savefile()
		
		clusters.createDictionary();
		
		clusters.vectorizeData();
		clusters.create(0);
		
		List<List<List<String>>> clusterss = clusters.getResultClusters(); // on récupère les clusters
		
		//exemple d'affichage de résultat
		int i = 1;
		
		for(List<List<String>> cluster : clusterss){
			HashMap<String,Integer> cmap = new HashMap<String,Integer>(); //map des titres
			String a = "";
			int nb =0;
			
			HashMap<String,Integer> gmap = new HashMap<String,Integer>(); //map des groupes
			
			for(List<String> titre : cluster){
				nb++;
				for(String mot : titre){
					if(cmap.containsKey(mot)){
						cmap.replace(mot, cmap.get(mot)+1);
					}else{
						cmap.put(mot, 1);
					}
					
				}
				int id = v.getKeyVecteurs(titre);
				String groupe = v.getGroup(id);
				
				if(gmap.containsKey(groupe)){
					gmap.replace(groupe, gmap.get(groupe)+1);
				}else{
					gmap.put(groupe, 1);
				}
				
				
				a += v.getAuthor(id) + " ";
			} //remplissage
			
			//Affichage du résultat dans la console
			System.out.println("\nCluster " + i + " (" + nb+ " entrées) :\n");
			System.out.println( a + "\n");
			for (String group : gmap.keySet())
			{
			    System.out.println(group  + " :" + gmap.get(group));
			}
			System.out.println("\n");
			for (String entry : cmap.keySet())
			{
				if (cmap.get(entry) > 1)
			    System.out.println(entry  + " [" + cmap.get(entry) + "]");
			}
			i++;
		}*/
		
		//FIN exemple 1
			
		//Exemple 2 : utilisation des groupes
		
		Groups g = new Groups();
		g.lecture("48");	
		
		
		//Exemple 3 : utilisation de publi afin de regarder ce que cette BD contient
		
		Publi publi = new Publi();
		List<String> l = publi.getWords(3);
		
		for(String word : l){
			System.out.println(word +"\n" + publi.getDBId(word));
			
		}
		
		
		
        
	}
	

}
