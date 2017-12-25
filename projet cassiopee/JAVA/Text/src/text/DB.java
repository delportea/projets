package text;

import java.sql.*;

/*DB*/

/*
 * Cette classe gère la connexion avec la base de donnée
 * 
 * Utilisation simple avec les methodes query(String s) et insert(String s)
 * Il vaut mieux fermer la connexion avec close() après utilisation
 */

public class DB {
	
    private static final String MYSQLDRIVER = "com.mysql.jdbc.Driver";
    private static final String URLDBMYSQL = "jdbc:mysql://localhost/";
    
    Connection laConnexion;
    Statement stmp = null;
    
    
    public DB(String dbName){
    	try{
    		Class.forName(MYSQLDRIVER);
    		laConnexion = DriverManager.getConnection(URLDBMYSQL + dbName + "?useSSL=false", "root", ""); //useSLL=false permet d'éviter des warnings, la base de donnée
    																									  //est ici en localhost d'où le mot de passe vide
    		stmp = laConnexion.createStatement();
    		
    	}catch(SQLException e){
    		e.printStackTrace();
    	}catch(ClassNotFoundException e){
    		e.printStackTrace();
    	}
    	
    }
    
    public void close(){
    	try {
			laConnexion.close();
			stmp.close();
	}catch(SQLException e) {
			e.printStackTrace();
	}
		
    }
    
    public ResultSet query(String q){
    	ResultSet r = null;
    	try {
    		r = stmp.executeQuery(q);
    	}catch(SQLException e) {
			e.printStackTrace();
    	}
    	return r;
    	
    }
    
    public void insert(String q){
    	
    	try {
    		stmp.executeUpdate(q);
    	}catch(SQLException e) {
			e.printStackTrace();
    	}
    	
    }

	
}