package text;

import java.util.HashMap;
import java.util.List;
import java.util.Vector;



public class test {

	public static void main(String[] args) {

		/* NOTE IMPORTANTE*/
		/* Ce projet doit �tre compris comme un script, un brouillon qui permet de faire des tests et non pas comme un programme
		 * utilisable comme tel.
		 * 
		 */
		
		/* DEPENDANCES */
		/*
		 * Mysql connector pour Java (ou �quivalent en fonction de la base de donn�e)
		 * Stanford Core NLP (http://stanfordnlp.github.io/CoreNLP/simple.html) pour g�rer la lemmatisation 
		 *   -> /!!\ la lib 'stanford-corenlp-3.6.0-models.jar' n'est pas incluse dans git (trop lourde) il faut donc la rajouter manuellement
		 * langdetect (https://github.com/shuyo/language-detection) pour le d�tection de language 
		 * 	 -> se r�f�rer � la documentation de la biblioth�que en particulier pour la cr�ation des profils
		 *   
		 *   
		 *   BASES DE DONNEES : Le pojet est tr�s d�pendant des bases de donn�es et de leur format.
		 *   Nousa avions deux bases de donn�es (appel�es 48 et 48_2). 
		 *   La premi�re est tr�s proche du format BibTex, les tables int�ressantes sont 'entries' -> les publications, 'personnel' -> liens entre un auteur et son groupe de recherche,
		 *   La deuxi�me ne contient que l'�quivalent de la table 'entries' de 48 (mais avec des sp�cificit�es)
		 *    L'interet de cette deuxi�me est de contenir des keywords alors que la premi�re ne contient que le titre de la publication 
		 */
		
		/* CLASSES */
		/* 
		 * CSVWriter g�re l'�criture dans un fichier
		 * DB g�re l'initialisation de la connexion avec une base de donn�es
		 * Groups g�re le matching d'un auteur avec un groupe de recherche (acmes, tipic, r3s ou methodes)
		 * Publi pemet d'utiliser la base de donn�e de production (au format pr�-trait�)
		 * Stopwords g�re l'identification des mots ind�sirables 
		 * Vecteurs g�re tout le reste du processus de traitement de texte
		 * 
		 * test et script sont des exemples d'utilisation (tir�s de pr�c�dents tests dont l'utilit� peut varier)    
		 */
	
		/* EXEMPLES*/
		
		//Utilisation simple
		
		DB db = new DB("48_2"); //initialise la lecture de BD
	
		
		
		Vecteurs v =  new Vecteurs();
		v.lecture(db, true, true); //remplissage des vecteurs
		v.savefile("data/newKey.txt", ";"); //sauvegarde au format voulue
		System.out.println(v.getSize());
		
		db.close(); //ferme la connexion
		
		
		//Utiliation combin�e avec la cr�ation de clusters (Projet Clustering)
		/* N'est pas conseill� ! Clusering utilise le projet Text, pas l'inverse, peut �tre utile seulement pour v�rifier les r�sultats */
		
		/*Clusters clusters = new Clusters(3, "data/titles_test1.txt"); //le chemin peut �tre celui pr�c�dememnt utilis� pour v.savefile()
		
		clusters.createDictionary();
		
		clusters.vectorizeData();
		clusters.create(0);
		
		List<List<List<String>>> clusterss = clusters.getResultClusters(); // on r�cup�re les clusters
		
		//exemple d'affichage de r�sultat
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
			
			//Affichage du r�sultat dans la console
			System.out.println("\nCluster " + i + " (" + nb+ " entr�es) :\n");
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
