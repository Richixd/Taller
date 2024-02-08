/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Compilador;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Ricardo Tzun
 */
public class AnalizadorSemantico {
    
    int erroresSemanticos = 0;
    
    ArrayList<String> tablaTrisA = new ArrayList();
    ArrayList<String> tablaTrisB = new ArrayList();
    ArrayList<String> tablaPortA = new ArrayList();
    ArrayList<String> tablaPortB = new ArrayList();
    ArrayList<String> tablaID = new ArrayList();
    
    
    public boolean evaluarTrisA(String token) {
        Pattern pattern = Pattern.compile("trisa\\s?=\\s?(0|1|\\%[0-1]{8})");
        Matcher matcher = pattern.matcher(token);
        boolean matchFound = matcher.find();
        return matchFound;
    }
    
    public boolean evaluarPortA(String token) {
        Pattern pattern = Pattern.compile("porta\\s?=\\s?(0|1|\\%[0-1]{8})");
        Matcher matcher = pattern.matcher(token);
        boolean matchFound = matcher.find();
        return matchFound;
    }
    
        public boolean evaluarTrisB(String token) {
        Pattern pattern = Pattern.compile("trisb\\s?=\\s?(0|1|\\%[0-1]{8})");
        Matcher matcher = pattern.matcher(token);
        boolean matchFound = matcher.find();
        return matchFound;
    }
    
    public boolean evaluarPortB(String token) {
        Pattern pattern = Pattern.compile("portb\\s?=\\s?(0|1|\\%[0-1]{8})");
        Matcher matcher = pattern.matcher(token);
        boolean matchFound = matcher.find();
        return matchFound;
    }
    
    public boolean evaluarTrisADot(String token) {
        Pattern pattern = Pattern.compile("trisa\\.[0-7]\\s?=\\s?(0|1|\\%[0-1]{8})");
        Matcher matcher = pattern.matcher(token);
        boolean matchFound = matcher.find();
        return matchFound;
    }
    
    public boolean evaluarPortADot(String token) {
        Pattern pattern = Pattern.compile("porta\\.[0-7]{1}\\s?=\\s?(0|1|\\%[0-1]{8})");
        Matcher matcher = pattern.matcher(token);
        boolean matchFound = matcher.find();
        return matchFound;
    }
    
        public boolean evaluarTrisBDot(String token) {
        Pattern pattern = Pattern.compile("trisb\\.[0-7]\\s?=\\s?(0|1|\\%[0-1]{8})");
        Matcher matcher = pattern.matcher(token);
        boolean matchFound = matcher.find();
        return matchFound;
    }
    
    public boolean evaluarPortBDot(String token) {
        Pattern pattern = Pattern.compile("portb\\.[0-7]\\s?=\\s?(0|1|\\%[0-1]{8})");
        Matcher matcher = pattern.matcher(token);
        boolean matchFound = matcher.find();
        return matchFound;
    }
    
    public boolean evaluarID(String token) {
        Pattern pattern = Pattern.compile("byte\\s([a-zA-Z]{1,100})_?([0-9])*");
        Matcher matcher = pattern.matcher(token);
        boolean matchFound = matcher.find();
        return matchFound;
    }
    
    
    
    
    public String analizadorS(ArrayList<String> tablaIdentificadores, String codigoAux){
       
        String resultado = "";

        String sentenciaIdentificadores = "";
        tablaTrisA.clear();
        tablaTrisB.clear();
        tablaPortA.clear();
        tablaPortB.clear();
        tablaID.clear();
        
        for (int i = 0; i < tablaIdentificadores.size(); i++) {
            if (i < tablaIdentificadores.size() - 1) {
                sentenciaIdentificadores += tablaIdentificadores.get(i) + "\n";
            } else if (i < tablaIdentificadores.size()) {
                sentenciaIdentificadores += tablaIdentificadores.get(i);
            }
        }
        
        
        
        if(sentenciaIdentificadores.isEmpty()){
            System.out.println("No hay dispositivos declarados");
            resultado += "\n[ERROR]- No existe ningun dispositivo declarado.";
            erroresSemanticos++;
        } else {
            if(tablaIdentificadores.size()>=2){
                erroresSemanticos++;
                resultado += "\n[ERROR]- No pueden haber mas de dos dispositivos declarados.";
                erroresSemanticos++;
            }
        } 

        
        
        System.out.println("\nIdentificadores");
        System.out.println(sentenciaIdentificadores);
        System.out.println(erroresSemanticos);
        return resultado;
    }
    
    public int returnErrores(){
        return erroresSemanticos;
    }
}
    
