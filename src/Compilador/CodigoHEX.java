/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Compilador;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JFileChooser;
import java.io.*;

/**
 *
 * @author Ricardo Tzun
 */
public class CodigoHEX {
    

    JFileChooser selecFile = new JFileChooser();
    File file;

    File instructions = new File("src/Files/Instrucciones.txt");
    File traduction = new File("src/Files/traduccion.txt");
    File hex = new File("src/Files/codigoHEX.hex");

    Texto txtInstructions = new Texto(instructions);
    Texto txtTraduction = new Texto(traduction);

    ArrayList<String> instrucciones = txtInstructions.getLines();
    ArrayList<String> traduccion = txtTraduction.getLines();
    ArrayList<String> GoToID = new ArrayList();

    
    String[] porta1 = {"0130", "8500"};
    String[] portb1 = {"0130", "8600"};
    String[] trisa0 = {"8316", "8501"};
    String[] trisa1 = {"0130", "8316", "8500"};
    String[] trisb0 = {"8316", "8601"};
    String[] trisb1 = {"0130", "8316", "8600"};

    String codigoHex = "";
    String codHEXtemp = "";

    int inst = 0;
    int contador = 0;
    int contadorLineas = 0;
    int contadorActual = 0;
    int contadorGoTo = 0;
    int posStart = 0;
    boolean trisPos = false;
    boolean device = false;
    boolean busquedaDelay = false;
    boolean delayPos = false;


    public void traduccion(String codigoAux) throws FileNotFoundException, IOException {
        
        for (String sentencia : codigoAux.split("\n")) {
            contadorLineas++;
            if (evaluarDelayMS(sentencia) == true){
                busquedaDelay = true;
            }
        }
        if(busquedaDelay == true){
            analizarDelay(codigoAux);
        }else{
            analizar(codigoAux);
        }
        
    }
    
    public void analizar(String codigoAux){
        codHEXtemp = "";
        inst = 0;
        contador = 0;
        contadorActual = 0;
        contadorGoTo = 0;
        posStart = 0;
        trisPos = false;
        
        for (String sentencia : codigoAux.split("\n")) {
            for (int i = 0; i < instrucciones.size(); i++) {
                if (sentencia.equals(instrucciones.get(i))) {
                    if (i == 3) {
                        if (trisPos == true) {
                            codHEXtemp += "8312";
                            analizarLinea();
                            trisPos = false;
                        }
                        for(String instruct : porta1){
                            codHEXtemp += instruct;
                            analizarLinea();
                        }
                    } else if(i == 5){
                        if (trisPos == true) {
                            codHEXtemp += "8312";
                            analizarLinea();
                            trisPos = false;
                        }
                        for(String instruct : portb1){
                            codHEXtemp += instruct;
                            analizarLinea();
                        }
                    }else if (i == 38) {
                        for(String instruct : trisa0){
                            codHEXtemp += instruct;
                            analizarLinea();
                        }
                        trisPos = true;
                    } else if(i == 40){
                        for(String instruct : trisb0){
                            codHEXtemp += instruct;
                            analizarLinea();
                        }
                        trisPos = true;
                    }else if (i == 39) {
                        for(String instruct : trisa1){
                            codHEXtemp += instruct;
                            analizarLinea();
                        }
                        trisPos = true;
                    }else if(i == 41){
                        for(String instruct : trisb1){
                            codHEXtemp += instruct;
                            analizarLinea();
                        }
                        trisPos = true;
                    }else {
                        if (trisPos == true) {
                            codHEXtemp += "8312";
                            inst += 2;
                            contadorGoTo += 1;
                            //analizarLinea();
                            trisPos = false;
                        }
                        codHEXtemp += traduccion.get(i);
                        analizarLinea();
                    }
                }
            }
            if (evaluarID(sentencia) == true) {
                String[] ID = sentencia.split(":");
                GoToID.add(ID[0]);
                System.out.println("---------" + ID[0] + " - " + contadorGoTo);
                GoToID.add(Integer.toString(contadorGoTo));
            } else if (evaluarGoTo(sentencia) == true) {
                String[] ID = sentencia.split(" ");
                for (int j = 0; j < GoToID.size(); j++) {
                    if (ID[1].equals(GoToID.get(j))) {
                        int posGoTo = Integer.parseInt(GoToID.get(j + 1));
                        String acumuladorGoTo = String.format("%1$02X", posGoTo);
                        codHEXtemp += acumuladorGoTo + "28";
                        inst += 2;
                        contadorGoTo += 1;
                        saltoLinea();
                    }
                }
            }
            contadorActual++;
        }
        codigoHex += ":02400E00223F4F\n:00000001FF";

        if (hex.exists()) {
            try ( PrintWriter pw = new PrintWriter(hex)) {
                pw.print(codigoHex);
            } catch (FileNotFoundException e) {
                System.out.println("PROBLEMAS");
            }
        }
        System.out.println("\n\n" + codigoHex);
    }
    
    
        
    
    public void analizarDelay(String codigoAux){
        codigoHex += ":100000002828A301A200FF30A207031CA307031C9A\n" +
                        ":1000100023280330A100DF300F200328A101E83E90\n" +
                        ":10002000A000A109FC30031C1828A00703181528FC\n" +
                        ":10003000A0070000A10F152820181E28A01C2228A8\n" +
                        ":100040000000222808008313831203130000080015\n";
        codHEXtemp = "";
        inst = 0;
        contador = 80;
        contadorActual = 0;
        contadorGoTo = 0;
        posStart = 0;
        trisPos = false;
        
        for (String sentencia : codigoAux.split("\n")) {
            for (int i = 0; i < instrucciones.size(); i++) {
                if (sentencia.equals(instrucciones.get(i))) {
                    if (i == 0 || i == 1) {
                        inst = inst;
                        device = true;
                    }
                    if (i == 3) {
                        if (trisPos == true) {
                            codHEXtemp += "8312";
                            analizarLinea();
                            trisPos = false;
                        }
                        for(String instruct : porta1){
                            codHEXtemp += instruct;
                            analizarLinea();
                        }
                    } else if(i == 5){
                        if (trisPos == true) {
                            codHEXtemp += "8312";
                            analizarLinea();
                            trisPos = false;
                        }
                        for(String instruct : portb1){
                            codHEXtemp += instruct;
                            analizarLinea();
                        }
                    }else if (i == 38) {
                        for(String instruct : trisa0){
                            codHEXtemp += instruct;
                            analizarLinea();
                        }
                        trisPos = true;
                    } else if(i == 40){
                        for(String instruct : trisb0){
                            codHEXtemp += instruct;
                            analizarLinea();
                        }
                        trisPos = true;
                    }else if (i == 39) {
                        for(String instruct : trisa1){
                            codHEXtemp += instruct;
                            analizarLinea();
                        }
                        trisPos = true;
                    }else if(i == 41){
                        for(String instruct : trisb1){
                            codHEXtemp += instruct;
                            analizarLinea();
                        }
                        trisPos = true;
                    }else if(i != 0 && i != 1 && i != 3 && i != 5 && i != 38 && i != 39 && i != 40 && i != 41){
                        if (trisPos == true) {
                            codHEXtemp += "8312";
                            inst += 2;
                            contadorGoTo += 1;
                            saltoLineaAux();
                            trisPos = false;
                        }
                        codHEXtemp += traduccion.get(i);
                        analizarLinea();
                    }
                }
            }
            if (evaluarID(sentencia) == true) {
                String[] ID = sentencia.split(":");
                GoToID.add(ID[0]);
                System.out.println("---------" + ID[0] + " - " + contadorGoTo);
                GoToID.add(Integer.toString(contadorGoTo));
            } else if (evaluarGoTo(sentencia) == true) {
                String[] ID = sentencia.split(" ");
                for (int j = 0; j < GoToID.size(); j++) {
                    if (ID[1].equals(GoToID.get(j))) {
                        int posGoTo = Integer.parseInt(GoToID.get(j + 1));
                        String acumuladorGoTo = String.format("%1$02X", posGoTo);
                        codHEXtemp += "2C" + "28";
                        inst += 2;
                        contadorGoTo += 1;
                        saltoLinea();
                        //analizarLinea();
                    }
                }
            } else if (evaluarDelayMS(sentencia) == true){
                String[] MS = sentencia.split(" ");
                int delayMSS = Integer.parseInt(MS[1]);
                String delayMS = String.format("%02X", delayMSS);
                codHEXtemp += delayMS + "30";
                inst += 2;
                contadorGoTo += 1;
                saltoLinea();
                //codHEXtemp += "8312";
                codHEXtemp += "0120";
                contadorGoTo += 1;
                analizarLinea();
                System.out.println("DEBUG - inst: " + inst);
                System.out.println("DEBUG - contadorGoTo: " + contadorGoTo);
                System.out.println("DEBUG - delayMSS: " + delayMSS);
            }
            contadorActual++;
        }
        codigoHex += ":02400E00223F4F\n:00000001FF";

        if (hex.exists()) {
            try ( PrintWriter pw = new PrintWriter(hex)) {
                pw.print(codigoHex);
            } catch (FileNotFoundException e) {
                System.out.println("PROBLEMAS");
            }
        }
        System.out.println("\n\n" + codigoHex);
    }
     
    public void analizarLinea(){
        inst += 2;
        contadorGoTo += 1;
        saltoLinea();
    }

    public void saltoLinea() {
        if (codHEXtemp.length() >= 32) {
            int acumuladorAux = 256;
            String hexAddress = String.format("%1$02X", inst);
            String contadorEspacio = String.format("%1$04X", contador);
            String lineaAux = hexAddress + contadorEspacio + "00" + codHEXtemp;

            for (String instruccionesSplit : lineaAux.split("(?<=\\G..)")) {
                acumuladorAux -= Integer.parseInt(instruccionesSplit, 16);
                if (acumuladorAux < 0) {
                    acumuladorAux += 256;
                }
            }

            String acumuladorHEX = String.format("%1$02X", acumuladorAux);
            codigoHex += ":" + lineaAux + acumuladorHEX + "\n";
            codHEXtemp = "";
            contador += 16;
            inst = 0;

        } else {
            if (contadorActual == contadorLineas - 1) {
                int acumuladorAux = 256;
                String hexAddress = String.format("%1$02X", inst);
                String contadorEspacio = String.format("%1$04X", contador);
                String lineaAux = hexAddress + contadorEspacio + "00" + codHEXtemp;

                for (String instruccionesSplit : lineaAux.split("(?<=\\G..)")) {
                    acumuladorAux -= Integer.parseInt(instruccionesSplit, 16);
                    if (acumuladorAux < 0) {
                        acumuladorAux += 256;
                    }
                }

                String acumuladorHEX = String.format("%1$02X", acumuladorAux);
                codigoHex += ":" + lineaAux + acumuladorHEX + "\n";
            }
        }
    }
    
    public void saltoLineaAux(){
        if (codHEXtemp.length() >= 32) {
            int acumuladorAux = 256;
            String hexAddress = String.format("%1$02X", inst);
            String contadorEspacio = String.format("%1$04X", contador);
            String lineaAux = hexAddress + contadorEspacio + "00" + codHEXtemp;

            for (String instruccionesSplit : lineaAux.split("(?<=\\G..)")) {
                acumuladorAux -= Integer.parseInt(instruccionesSplit, 16);
                if (acumuladorAux < 0) {
                    acumuladorAux += 256;
                }
            }
            

            String acumuladorHEX = String.format("%1$02X", acumuladorAux);
            codigoHex += ":" + lineaAux + acumuladorHEX + "\n";
            codHEXtemp = "";
            contador += 16;
            inst = 0;

        }
    }
    
    public boolean evaluarID(String token) {
        Pattern pattern = Pattern.compile("[a-zA-Z]+\\:");
        Matcher matcher = pattern.matcher(token);
        boolean matchFound = matcher.find();
        return matchFound;
    }
    
    public boolean evaluarGoTo(String token) {
        Pattern pattern = Pattern.compile("goto.[a-zA-Z]+");
        Matcher matcher = pattern.matcher(token);
        boolean matchFound = matcher.find();
        return matchFound;
    }
    
    public boolean evaluarDelayMS(String token) {
        Pattern pattern = Pattern.compile("delayms.([0-9]{1,2}|1[0-9]{2}|2[0-4][0-9]|25[0-5])");
        Matcher matcher = pattern.matcher(token);
        boolean matchFound = matcher.find();
        return matchFound;
    }
    
}