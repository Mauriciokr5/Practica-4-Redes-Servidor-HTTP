/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.practica4;

import java.io.File;
import java.util.StringTokenizer;

/**
 *
 * @author Mauricio Beltrán
 */
public class Prueba {
    public static void main(String[] args){
        String cadena = "GET /pdf.pdf?nose HTTP/1.1";
        StringTokenizer tokens=new StringTokenizer(cadena," ?");
        System.out.println(tokens.countTokens());
        System.out.println(tokens.nextToken());
        String arc = tokens.nextToken();
        System.out.println(arc);
        System.out.println(tokens.nextToken());
        try{
            File file= new File("./"+arc);
            System.out.println(file.getAbsolutePath());
            System.out.println(file.exists());
        }catch(Exception e){
            System.out.println("sin archivo");
            e.printStackTrace();
        }
    }
    
}
