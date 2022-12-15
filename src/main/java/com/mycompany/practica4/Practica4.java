/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Project/Maven2/JavaApp/src/main/java/${packagePath}/${mainClassName}.java to edit this template
 */

package com.mycompany.practica4;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author Mauricio Beltrán
 */
public class Practica4 {

    public static void main(String[] args) throws IOException, Exception {
        System.out.println("Iniciando Servidor.......");
        ServerSocket ss= new ServerSocket(8000);
        System.out.println("Servidor iniciado:---OK");
        System.out.println("Esperando por Cliente....");
        for(;;)
        {
            Socket accept=ss.accept();
            new Manejador(accept).start();
        }
    }
}
