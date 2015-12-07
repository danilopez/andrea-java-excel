package programadietas;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author pmateo
 */


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

/**
 * Clase simple que se encarga de pasar la conexión a la base de datos.
 * @author Gustavo Hernández Salinas
 */
public final class baseDeDatos {
   //Declaramos la conexión:
  private Connection conn = null;
  private int numeroIngredientes;
  private int numeroNutrientes;
  private String ruta=null;
  
 /**
  * Conecta a una base de datos en la ruta pasada como argumento.
  * Si el archivo señalado en la ruta no existe, se crea.
  * @param ruta - La ruta de la DB a conectar.
  * @return La conexión a la ruta.
  */
 protected Connection conectar(String ruta){
  try {
   //Aquí cargamos el driver de SQLITE.
   Class.forName("org.sqlite.JDBC");
  }
  catch (ClassNotFoundException e) {
   //Esto se ejecuta si hay un error con el driver de la base de datos.
   e.printStackTrace();
  }
  


  try {
   //Aquí se obtiene la conexión:
   conn = DriverManager.getConnection("jdbc:sqlite:FEDNA.xls");
   //Un mensaje en la consola para saber si se realizó la conexión y donde está el archivo:
   System.out.println("Conexión realizada correctamente - Ruta de base de datos: " + "C:\\Users\\Andreita\\Desktop\\Matemáticas\\Trabajo Fin de Grado\\ProgramaDietas\\src\\programadietas\\FEDNA.xls");
  }
  catch (SQLException e) {
   //Esto se ejecuta si hay un error en la base de datos:
   e.printStackTrace();
  }
  
  //Devolvemos la conexión:
  return conn;
 }
 
 /**
  * Determina el número de ingredientes, es decir el número de registros 
  * en la base de alimentos/ingredientes
  */
 private void obtenNumeroIngredientes(){
	 ResultSet rs=null;
	 numeroIngredientes=0;
	 try {
      Statement statement = conn.createStatement();
      rs = statement.executeQuery("SELECT Count(*) FROM Ingredientes");
      numeroIngredientes=Integer.parseInt(rs.getString(1));
	 }
	 catch (SQLException e) {
		 //Esto se ejecuta si hay algún problema al realizar la conexión.
		 e.printStackTrace();
	 }
	
 }
/**
 * Determina el número de nutrientes para cada ingrediente. Incluye 
 * el codigo y el precio del ingrediente.
 */
 private void obtenNumeroNutrientes(){
	 ResultSet rs=null;
	 numeroNutrientes=0;
	 try {
          Statement statement = conn.createStatement();
          rs = statement.executeQuery("SELECT Count(*) FROM Nutrientes");
          numeroNutrientes=Integer.parseInt(rs.getString(1));
	 }
	 catch (SQLException e) {
		 //Esto se ejecuta si hay algún problema al realizar la conexión.
		 e.printStackTrace();
	 }
	System.out.println("Numero nutrientes="+numeroNutrientes);
 }
 

 String [] obtenListaIngredientes(){ 
	 ResultSet rs=null;
	 String lista[]=null;
	 obtenNumeroIngredientes();
	 lista = new String[numeroIngredientes];
	 try {
	      Statement statement = conn.createStatement();
	      rs = statement.executeQuery("select INGREDIENT from Ingredientes");
	      int i=0;
	      while(rs.next())
	      {
	    	  lista[i]=rs.getString("INGREDIENT");
	    	  i++;
	      }
	 }
	 catch (SQLException e) {
		 //Esto se ejecuta si hay algún problema al realizar la conexión.
		 e.printStackTrace();
	 }
	 
	 return lista;
 }
 
 

 
 String [] obtenListaNutrientes(){ 
	 ResultSet rs=null;
	 String lista[]=null;
	 obtenNumeroNutrientes();
	 lista = new String[numeroNutrientes+1];
         lista[0]="Nombre";
	 try {
	      Statement statement = conn.createStatement();
	      rs = statement.executeQuery("select CODE_NUTRIENTE,ABREVIATUR,UNIDADES_S  from Nutrientes");
	     int i=0;
	      while(rs.next())
	      {
	    	  lista[i+1]="("+rs.getString("CODE_NUTRIENTE")+") "+rs.getString("ABREVIATUR")+" "+rs.getString("UNIDADES_S");//+"--->"+rs.getString("DESCRIPCIO")+"--->"+rs.getString("UNIDADES_S");
	    	  i++;
	      }
	 }
	 catch (SQLException e) {
		 //Esto se ejecuta si hay algún problema al realizar la conexión.
		 e.printStackTrace();
	 }
	 
	 return lista;
 }
 
 String [] obtenListaUnidadesNutrientes(){ 
	 ResultSet rs=null;
	 String lista[]=null;
	 obtenNumeroNutrientes();
	 lista = new String[numeroNutrientes];
	 try {
	      Statement statement = conn.createStatement();
	      rs = statement.executeQuery("select UNIDADES_S  from Nutrientes");
	     int i=0;
	      while(rs.next())
	      {
	    	  lista[i]=rs.getString("UNIDADES_S");//+"--->"+rs.getString("DESCRIPCIO")+"--->"+rs.getString("UNIDADES_S");
	    	  i++;
	      }
	 }
	 catch (SQLException e) {
		 //Esto se ejecuta si hay algún problema al realizar la conexión.
		 e.printStackTrace();
	 }
	 
	 return lista;
 }
 
 double[][] obtenMatrizA(){
	 this.obtenNumeroIngredientes();
	 this.obtenNumeroNutrientes();
	 double A[][]= new double[this.numeroIngredientes][this.numeroNutrientes+1];//La primera posicion para el codigo
	 NumberFormat format = NumberFormat.getInstance(Locale.FRANCE);
	 Number number = null;
	 
	 ResultSet rs=null;	 
	 try {
	      Statement statement = conn.createStatement();
	      statement.setMaxRows(500);
	      rs = statement.executeQuery("select * from Ingredientes");
	      int i=0;
	      
	      while(rs.next())
	      {
	    	  try{
	    	     A[i][0]=Double.parseDouble(rs.getString(1));  
	    	  }catch(NumberFormatException nfe){
	    		  System.out.println("****"+rs.getString(1)+"i="+i+"j="+0);
	    	  }  	    	    	   
	    	  for(int j=0;j<this.numeroNutrientes;j++){
	    		  try{
	    		  A[i][j+1]=Double.parseDouble(rs.getString(j+3));  
	    		  }catch(NumberFormatException nfe){
	    			  System.out.println("*BB**"+rs.getString(j+3)+"i="+i+"j="+j);
	    		  }  	    
	    	  }
	    	  i++;
	    	 
	      }
	 }
	 catch (SQLException e) {
		 //Esto se ejecuta si hay algún problema al realizar la conexión.
		 e.printStackTrace();
	 }
	 
	 
	 return A;
	 
 }
 
 String[][] obtenMatrizAString(){
	 this.obtenNumeroIngredientes();
	 this.obtenNumeroNutrientes();
	 String A[][]= new String[this.numeroIngredientes][1+this.numeroNutrientes];
	 NumberFormat format = NumberFormat.getInstance(Locale.FRANCE);
	 Number number = null;
	 
	 ResultSet rs=null;	 
	 try {
	      Statement statement = conn.createStatement();
	      statement.setMaxRows(500);
	      rs = statement.executeQuery("select * from Ingredientes");
	      int i=0;
	      
	      while(rs.next())
	      {
	    	   try{
	    		  A[i][0]=rs.getString(1)+" : "+rs.getString(2);  
	    		  }catch(NumberFormatException nfe){
	    			  System.out.println("*ff*"+rs.getString(1)+":"+rs.getString(2)+"i="+i+"j="+0);
	    		  }
	    	  for(int j=1;j<this.numeroNutrientes+1;j++){
	    		  //try{
	    		  // number=format.parse(rs.getString(j+2));
	    	       //A[i][j]=number.doubleValue();
	    	     //}catch(ParseException pae){System.out.println(rs.getString(j+2)+"i="+i);}
	    		  try{
	    		  A[i][j]=rs.getString(j+2);  
	    		  }catch(NumberFormatException nfe){
	    			  System.out.println("*lll*"+rs.getString(j+2)+"i="+i+"j="+j);
	    		  }  	    
	    	  }
	    	  i++;
	    	 
	      }
	 }
	 catch (SQLException e) {
		 //Esto se ejecuta si hay algún problema al realizar la conexión.
		 e.printStackTrace();
	 }
	 
	 
	 return A;
	 
 }
 
 
 
}

