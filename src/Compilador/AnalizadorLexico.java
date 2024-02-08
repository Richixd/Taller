/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Compilador;

import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Ricardo Tzun
 */
public class AnalizadorLexico {

    ArrayList<String> asignacion = new ArrayList(); //ArrayList que almacena cada token, simbolo e identificador
    ArrayList<String> tokens = new ArrayList(); //ArrayList que almacena el resultado del analisis lexico
    ArrayList<String> auxClasificacion = new ArrayList(); //Array auxiliar para clasificar a la tabla de simbolos
    
    int erroresLexicos = 0;  //Variable que almacena todos los erroes de forma global
    int erroresSintacticos = 0;  //Variable que almacena todos los erroes de forma global
    int erroresSemanticos = 0;
    int erroresL = 0; //variable que almacena un error por linea
    int erroresAuxiliares = 0;
    

    AnalizadorSintactico analizadorSintactico = new AnalizadorSintactico();
    AnalizadorSemantico analizadorSemantico = new AnalizadorSemantico();
    CodigoHEX codigoHEX = new CodigoHEX();

    /**
     * Metodo para evaluar palabras reservadas dentro del texto
     * ReservedWords.txt
     * @param token
     * @param tokenLines
     * @return 
     */
    public boolean evaluarRW(String token, ArrayList<String> tokenLines) {
        int encontrado = 0;
        for (int i = 0; i < tokenLines.size(); i++) {
            if (tokenLines.get(i).equals(token)) {
                encontrado++;
            }
        }
        return encontrado >= 1;
    }

    /**
     * Metodo para evaluar simbolos y operadores dentro del texto Symbols.txt
     * @param token
     * @param symbolLines
     * @return 
     */
    public boolean evaluarSY(String token, ArrayList<String> symbolLines) {
        int encontrado = 0;
        for (int i = 0; i < symbolLines.size(); i++) {
            if (symbolLines.get(i).equals(token)) {
                encontrado++;
            }
        }
        return encontrado >= 1;
    }

    /**
     * Metodo para encontrar identificadores usando expresiones regulares.
     * @param token
     * @return 
     */
    public boolean evaluarID(String token) {
        Pattern pattern = Pattern.compile("(^[a-zA-Z]{1,100})+_?([0-9])*$");
        Matcher matcher = pattern.matcher(token);
        boolean matchFound = matcher.find();
        return matchFound;
    }

        /**
     * Metodo para encontrar identificadores usando expresiones regulares.
     * @param token
     * @return 
     */
    public boolean evaluarDevice(String token) {
        Pattern pattern = Pattern.compile("^[1-9]+[a-zA-Z]+[1-9]+[a-zA-Z]+");
        Matcher matcher = pattern.matcher(token);
        boolean matchFound = matcher.find();
        return matchFound;
    }
    
    /**
     * Metodo para encontrar numeros enteros y decimale usando expresiones
     * regulares.
     * @param token
     * @return 
     */
    public boolean evaluarNum(String token) {
        Pattern pattern = Pattern.compile("^[0-9]\\d*(\\.\\d+)?$");
        Matcher matcher = pattern.matcher(token);
        boolean matchFound = matcher.find();
        return matchFound;
    }

    public boolean evaluarOnOff(String token) {
        Pattern pattern = Pattern.compile("^(0|1)$");
        Matcher matcher = pattern.matcher(token);
        boolean matchFound = matcher.find();
        return matchFound;
    }
        
    /**
     * Metodo para evaluar si la cadena contiene espacios en blanco.Eso va a
 determinar si se trata de una palabra simple o es una cadena que deberá
 ser analizada de forma extensa.
     * @param token
     * @return 
     */
    public boolean evaluarString(String token) {
        Pattern pattern = Pattern.compile("(\\s+|\\n+)");
        Matcher matcher = pattern.matcher(token);
        boolean matchFound = matcher.find();
        return matchFound;
    }

    /**
     * Metodo para encontrar caracteres de texto usando expresiones regulares.
     * @param token
     * @return 
     */
    public boolean evaluarCar(String token) {
        Pattern pattern = Pattern.compile("\\'[a-zA-Z]\\'");
        Matcher matcher = pattern.matcher(token);
        boolean matchFound = matcher.find();
        return matchFound;
    }
    
    /**
     * Metodo para encontrar cadenas de texto (sin espacios, comas o puntos)
     * usando expresiones regulares.
     * @param token
     * @return 
     */
    public boolean evaluarCad(String token) {
        Pattern pattern = Pattern.compile("\\\"\\w+\\\"");
        Matcher matcher = pattern.matcher(token);
        boolean matchFound = matcher.find();
        return matchFound;
    }
    
    public boolean evaluarTris(String token) {
        Pattern pattern = Pattern.compile("tris[a-d]");
        Matcher matcher = pattern.matcher(token);
        boolean matchFound = matcher.find();
        return matchFound;
    }
    
    public boolean evaluarPort(String token) {
        Pattern pattern = Pattern.compile("port[a-d]{1}");
        Matcher matcher = pattern.matcher(token);
        boolean matchFound = matcher.find();
        return matchFound;
    }
    
    public boolean evaluarBin(String token) {
        Pattern pattern = Pattern.compile("\\%[0-1]{8}$");
        Matcher matcher = pattern.matcher(token);
        boolean matchFound = matcher.find();
        return matchFound;
    }

    public String evaluarLinea(String lineas) throws IOException {
        erroresL = 0;
        int contadorLineas = 1;
        String resultado = "";
        String codigoAux = ""; //String temporal que almacena el codigo con comentarios de linea unica eliminados.
        
        //METODO PARA ELIMINAR COMENTARIOS DE LINEA UNICA
        for (String lines : lineas.split("\\n")) {
            String comentarioEliminado = "";

            //PRIMERO VA A COMPARAR SI EXISTEN COMENTARIOS DE LINEA UNICA
            Pattern pattern = Pattern.compile("\\/\\/(.*?)\\/\\/");
            Matcher matcher = pattern.matcher(lines);
            boolean matchFound = matcher.find();
            if (matchFound) {
                int inicioComentario = matcher.start();
                String primeraCadena = "";
                if (inicioComentario == 0) {
                    comentarioEliminado = "";
                } else {
                    primeraCadena = lines.substring(0, inicioComentario - 1);
                    comentarioEliminado = primeraCadena;
                }
            } else {
                comentarioEliminado = lines;
                codigoAux += comentarioEliminado + "\n";
            }

            //ENVIA AL ANALIZADOR LEXICO LA CADENA SIN EL COMENTARIO
            if (comentarioEliminado.length() >= 1) {
                resultado += "Linea " + contadorLineas + ":\n\t" + analisis(comentarioEliminado);
                contadorLineas++;
            } else {
                contadorLineas++;
            }
        }
        
        erroresSintacticos = analizadorSintactico.erroresSintacticosT();
        if(erroresAuxiliares==0){
            resultado += analizadorSemantico.analizadorS(analizadorSintactico.returnTablaIdentificadores(), codigoAux);            
        }else{
            resultado += "\n\n\t>[ADVERTENCIA]- Corrija los errrores lexicos/sintacticos.";
        }
        erroresSemanticos = analizadorSemantico.returnErrores();
        
        if (erroresTotales() == 0){
            codigoHEX.traduccion(codigoAux);
        }
        
        
        return resultado;
    }
    
    public String analisis(String lineaActualizada){
        asignacion.clear(); //ArrayList que almacena cada token, simbolo e identificador
        tokens.clear(); //ArrayList que almacena el resultado del analisis lexico
        String resultado = "";

        //SPLIT PARA CADA ESPACIO
        String[] linea = lineaActualizada.split(" ");
        for (int i = 0; i < linea.length; i++) {
            splitPunto(linea[i]);
        }

        System.out.println(asignacion);
        resultado += clasificacion();

        //Si no existe ningun error lexico que deba revisarse
        if (erroresL == 0) {
            //Si se retorna false quiere decir que hubo un error sintactico
            if (analizadorSintactico.evaluarSintaxis(tokens) == false) {
                resultado += "\n\t\t>[ERROR]- Sentencia mal declarada.";
            }
            analizadorSintactico.clasificarSentencias(tokens);
            resultado += "\n\n";
        }
        erroresL = 0;
        return resultado;
    }

    //SPLIT PARA CADA COMA ,
    public void splitPunto(String linea) {
        String[] subLinea = linea.split("(?<=\\.)|(?=\\.)");
        for (int j = 0; j < subLinea.length; j++) {
            splitIgual(subLinea[j]);
        }
    }

    //SPLIT PARA CADA IGUAL =
    public void splitIgual(String linea) {     
        String[] subLinea2 = linea.split("(?<==)|(?==)");
        for (int k = 0; k < subLinea2.length; k++) {
            splitPyC(subLinea2[k]);
        }
    }

    //SPLIT PARA CADA PUNTO Y COMA ;    
    public void splitPyC(String linea) {
        String[] subLinea = linea.split("(?<=;)|(?=;)");
        for (int j = 0; j < subLinea.length; j++) {
            splitSuma(subLinea[j]);
        }
    }

    //SPLIT PARA CADA SUMA +
    public void splitSuma(String linea) {
        String[] subLinea3 = linea.split("(?<=\\+)|(?=\\+)");
        for (int l = 0; l < subLinea3.length; l++) {
            splitResta(subLinea3[l]);
        }
    }

    //SPLIT PARA CADA RESTA -
    public void splitResta(String linea) {
        String[] subLinea4 = linea.split("(?<=\\-)|(?=\\-)");
        for (int m = 0; m < subLinea4.length; m++) {
            splitMulti(subLinea4[m]);
        }
    }

    //SPLIT PARA CADA MULTIPLICACION *
    public void splitMulti(String linea) {
        String[] subLinea5 = linea.split("(?<=\\*)|(?=\\*)");
        for (int n = 0; n < subLinea5.length; n++) {
            splitDiv(subLinea5[n]);
        }
    }

    //SPLIT PARA CADA MULTIPLICACION /
    public void splitDiv(String linea) {
        String[] subLinea6 = linea.split("(?<=\\/)|(?=\\/)");
        for (int o = 0; o < subLinea6.length; o++) {
            splitParI(subLinea6[o]);
        }
    }
    
    //SPLIT PARA CADA PARENTESIS IZQUIERDO (
    public void splitParI(String linea) {
        String[] subLinea6 = linea.split("(?<=\\()|(?=\\()");
        for (int o = 0; o < subLinea6.length; o++) {
            splitParD(subLinea6[o]);
        }
    }
    
    //SPLIT PARA CADA PARENTESIS DERECHO )
    public void splitParD(String linea) {
        String[] subLinea6 = linea.split("(?<=\\))|(?=\\))");
        for (int o = 0; o < subLinea6.length; o++) {
            splitPleca(subLinea6[o]);
        }
    }
    
    //SPLIT PARA CADA PLECA |
    public void splitPleca(String linea) {
        String[] subLinea6 = linea.split("(?<=\\|)|(?=\\|)");
        for (int o = 0; o < subLinea6.length; o++) {
            splitLlaveIzq(subLinea6[o]);
        }
    }
    
    //SPLIT PARA CADA LLAVE IZQUIERDA {
    public void splitLlaveIzq(String linea) {
        String[] subLinea6 = linea.split("(?<=\\{)|(?=\\{)");
        for (int o = 0; o < subLinea6.length; o++) {
            splitLlaveDer(subLinea6[o]);
        }
    }
    
    //SPLIT PARA CADA LLAVE DERECHA }
    public void splitLlaveDer(String linea) {
        String[] subLinea6 = linea.split("(?<=\\})|(?=\\})");
        for (int o = 0; o < subLinea6.length; o++) {
            splitDosPuntos(subLinea6[o]);
        }
    }
    
    //SPLIT PARA CADA DOS PUNTOS :
    public void splitDosPuntos(String linea) {
        String[] subLinea6 = linea.split("(?<=\\:)|(?=\\:)");
        for (int o = 0; o < subLinea6.length; o++) {
            asignacion.add(subLinea6[o]);
        }
    }
    

    /*
    Metodo que evaluara que tipo de elementos posee el Arraylist
     */
    public String clasificacion() {
        String resultado = "";
        for (int i = 0; i < asignacion.size(); i++) {
            if (evaluarTris(asignacion.get(i)) == true) {
                resultado += "<" + asignacion.get(i) + ", tris>";
                tokens.add("tris");
            } else if (evaluarPort(asignacion.get(i)) == true){
                resultado += "<" + asignacion.get(i) + ", port>";
                tokens.add("port");
            }else if (asignacion.get(i).equals(".")) {
                resultado += "< " + asignacion.get(i) + " , dot>";
                tokens.add("dot");
            } else if (asignacion.get(i).equals(";")) {
                resultado += "< " + asignacion.get(i) + " , fin>";
                tokens.add("fin");
            } else if (asignacion.get(i).equals("=")) {
                resultado += "< " + asignacion.get(i) + " , asig>";
                tokens.add("asig");
            } else if (asignacion.get(i).equals("delayms")) {
                resultado += "<" + asignacion.get(i) + " , delay>";
                tokens.add("delay");
            } else if (asignacion.get(i).equals("goto")) {
                resultado += "<" + asignacion.get(i) + " , goto>";
                tokens.add("goto");
            } else if (asignacion.get(i).equals("end")) {
                resultado += "<" + asignacion.get(i) + " , end>";
                tokens.add("end");
            } else if (asignacion.get(i).equals("device")) {
                resultado += "<" + asignacion.get(i) + " , device>";
                tokens.add("device");
            } else if (asignacion.get(i).equals("byte")) {
                resultado += "<" + asignacion.get(i) + " , byte>";
                tokens.add("byte");
            } else if (asignacion.get(i).equals("(")) {
                resultado += "<" + asignacion.get(i) + ", parI>";
                tokens.add("parI");
            } else if (asignacion.get(i).equals(")")) {
                resultado += "<" + asignacion.get(i) + ", parD>";
                tokens.add("parD");
            } else if (asignacion.get(i).equals("|")) {
                resultado += "<" + asignacion.get(i) + ", or >";
                tokens.add("or");
            } else if (asignacion.get(i).equals("{")) {
                resultado += "<" + asignacion.get(i) + ", llaveI>";
                tokens.add("llaveI");
            } else if (asignacion.get(i).equals("}")) {
                resultado += "<" + asignacion.get(i) + ", llaveD>";
                tokens.add("llaveD");
            } else if (asignacion.get(i).equals(":")) {
                resultado += "<" + asignacion.get(i) + ", colon>";
                tokens.add("colon");
            } else if (evaluarNum(asignacion.get(i)) == true) {
                resultado += "<" + asignacion.get(i) + ", num>";
                tokens.add("num");
            } else if (evaluarCar(asignacion.get(i)) == true) {
                resultado += "<" + asignacion.get(i) + ", car>";
                tokens.add("car");
            } else if (evaluarCad(asignacion.get(i)) == true) {
                resultado += "<" + asignacion.get(i) + ", cad>";
                tokens.add("cad");
            } else if (evaluarDevice(asignacion.get(i)) == true) {
                resultado += "<" + asignacion.get(i) + ", dev>";
                tokens.add("dev");
            } else if (evaluarBin(asignacion.get(i)) == true) {
                resultado += "<" + asignacion.get(i) + ", binary>";
                tokens.add("binary");
            } else if (evaluarID(asignacion.get(i)) == true) {
                resultado += "<" + asignacion.get(i) + ", id>";
                tokens.add("id");
            } else {
                resultado += "<" + asignacion.get(i) + ", error>";
                erroresLexicos += 1;
                erroresL += 1;
            }
        }
        if (erroresL >= 1) {
            resultado += "\n\t\t>[ERROR]- Sentencia o asignacion mal declarada.\n";
        }
        return resultado;
    }

    public int erroresTotales() {
        int erroresTotales = erroresLexicos + erroresSintacticos + erroresSemanticos;
        return erroresTotales;
    }
    
    
    //METODO QUE DEVUELVE ERRORES LEXICOS Y SINTACTICOS PARA DETERMINAR SI ENTRA AL ANALIZADOR SEMANTICO
    public void erroresAux() {
        erroresAuxiliares = erroresLexicos + erroresSintacticos;
    }

    public void resetErrores() {
        erroresLexicos = 0;
        analizadorSintactico.resetErrores(0);
    }
}