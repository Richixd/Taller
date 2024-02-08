/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Compilador;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author Ricardo Tzun
 */
public class Texto {
    
    ArrayList<String> lines = new ArrayList();
    File file;
    
    public Texto(File file){
        this.file = file;
    }
    
    public ArrayList<String> getLines(){
        lines.clear();
        try{
            FileReader fr = new FileReader(this.file);
            BufferedReader br = new BufferedReader(fr);
            String line;
            while ((line = br.readLine()) != null){
                lines.add(line);
            }
        } catch(IOException e){
            System.err.println(e);
        }
        return this.lines;
    }   
}
