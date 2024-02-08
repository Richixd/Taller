/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Compilador;

import java.io.File;
import java.util.ArrayList;

/**
 *
 * @author Ricardo Tzun
 */
public class AnalizadorSintactico {
    
    int erroresSintacticos = 0;
    int noIteraciones = 0;
    
    File rules = new File("src/Files/Reglas.txt");
    Texto txtRules = new Texto(rules);
    ArrayList<String> reglas = txtRules.getLines();
    int contador = 1;
    
    
    ArrayList<String> tablaIdentificadores = new ArrayList();

    public boolean evaluarSintaxis(ArrayList<String> tokens) {
        String sentencia = "";
        Boolean errorSLocal = false;
        
        

        //Convertir el ArrayList a un String
        for (int i = 0; i < tokens.size(); i++) {
            if (i < tokens.size() - 1) {
                sentencia += tokens.get(i) + " "; 
            } else if (i < tokens.size()) {
                sentencia += tokens.get(i);
            }
        }

        //Verificara la sentencia con cada una de las reglas
        for (int i = 0; i < reglas.size(); i++) {
            if (sentencia.equals(reglas.get(i))) {
                errorSLocal = true;    
            }
        }
        if (errorSLocal == false) {
            erroresSintacticos += 1;
        }
        noIteraciones += 1;
        return errorSLocal;
    }
    
    
    
    
    public void clasificarSentencias(ArrayList<String> tokens){
        String sentencia = "";
        
        //Convertir el ArrayList a un String
        for (int i = 0; i < tokens.size(); i++) {
            if (i < tokens.size() - 1) {
                sentencia += tokens.get(i) + " ";
            } else if (i < tokens.size()) {
                sentencia += tokens.get(i);
            }
        }
        
        if (sentencia.equals("device dev")){
            tablaIdentificadores.add(sentencia);
        }
    }
    
     
     
    public int erroresSintacticosT() {
        return erroresSintacticos;
    }

    public void resetErrores(int numero) {
        erroresSintacticos = numero;
    }
    
    
    public ArrayList returnTablaIdentificadores(){
        return tablaIdentificadores;
    }
    

    
    
    
}
