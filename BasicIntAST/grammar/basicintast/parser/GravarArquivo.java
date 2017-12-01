/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package basicintast.parser;


        
       
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author a120093
 */
public class GravarArquivo {
    public static void get(String texto){
        try {
            texto(texto);
        } catch (IOException ex) {
            Logger.getLogger(GravarArquivo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public static void texto(String texto) throws IOException {
       File arquivo = new File( "teste.cpp" );
       arquivo.delete();
       arquivo.createNewFile();
    
FileWriter fw = new FileWriter(arquivo, true);
BufferedWriter bw = new BufferedWriter(fw);

bw.write(texto);
 
bw.newLine();
 
bw.close();
fw.close();
    }
}

