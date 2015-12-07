/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


package programadietas;

/**
 *
 * @author pmateo
 */
public  class datosGlobales {
 static baseDeDatos bdd=null;
 static String[] listaIngredientes=null;
 static String[] listaNutrientes=null;
 static String[] listaUnidades=null;
 static String[][] tablaAlimentosSS=null;
 static double[][] tablaAlimentos=null;
 boolean baseDedatosModificada=false;
 /**
  * Se encarga de dibujar la matriz completa de ingredientes con sus nutrientes
  * 
  */
 static void pintaMatrizA(){
    for(int i=0;i<tablaAlimentos.length;i++)   {
      for(int j=0;j<tablaAlimentos[i].length;j++)
          System.out.print(tablaAlimentos[i][j]+"  "); 
      System.out.println();
    }
   
 }
}
