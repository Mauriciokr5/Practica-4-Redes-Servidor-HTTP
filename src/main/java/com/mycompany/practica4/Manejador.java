/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.practica4;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Date;
import java.util.StringTokenizer;
import java.util.stream.Collectors;

/**
 *
 * @author Mauricio Beltrán
 */
public class Manejador extends Thread{
    protected Socket socket;
    protected PrintWriter pw;
    protected BufferedOutputStream bos;
    protected BufferedReader br;
    protected String method;
    protected int methodID;
    protected String FileName;
    protected boolean hasParameters;
    protected String parameters;
    protected String ContentType;
    

    public Manejador(Socket _socket) throws Exception{
        this.socket=_socket;
    }
    public void run(){
        try{
            br=new BufferedReader(new InputStreamReader(socket.getInputStream()));
            bos=new BufferedOutputStream(socket.getOutputStream());
            pw=new PrintWriter(new OutputStreamWriter(bos));
            String line =  br.readLine();

            if(line==null){
                    pw.print("<html><head><title>Servidor WEB");
                    pw.print("</title><body bgcolor=\"#AACCFF\"<br>Linea Vacia</br>");
                    pw.print("</body></html>");
                    socket.close();
                    return;
            }
            System.out.println("\nCliente Conectado desde: "+socket.getInetAddress());
            System.out.println("Por el puerto: "+socket.getPort());
            System.out.println("Datos: "+line+"\r\n\r\n");
            
            getInfoRequest(line);
            
            switch(methodID){
                case 1://HEAD
                    methodHEAD();
                    break;   
                case 2://GET
                    methodGET();
                    break;  
                case 3://POST
                    methodPOST();
                    break;  
                case 4://DELETE
                    methodDELETE();
                    break;  
                default:
                    pw.println("HTTP/1.0 501 Not Implemented");
                    pw.println();
            }
            
            
            pw.flush();
            bos.flush();
        
        }catch(Exception e){e.printStackTrace();}
        
        try{
            socket.close();
        }catch(Exception e){e.printStackTrace();}
    }
    
    public void methodGET(){
        System.out.println(parameters);
        File file= new File("." + this.FileName);
        if(!file.exists()){
            notFound();
        }else if(!this.hasParameters){
            if(FileName.compareTo("/")==0){
                FileName="/index.htm";  
            }
            SendA(FileName);
        }else{
            pw.println("HTTP/1.0 200 OK");
            pw.flush();
            pw.println();
            pw.flush();
            pw.print("<html><head><title>SERVIDOR WEB");
            pw.flush();
            pw.print("</title></head><body bgcolor=\"#4D7902\"><center><h1><br>Parametros Obtenidos por metodo GET..</br></h1>");
            pw.flush();
            pw.print("<h3><b>"+this.parameters+"</b></h3>");
            pw.flush();
            pw.print("</center></body></html>");
            pw.flush();
        }
        
    }
    
    public void methodPOST(){
        System.out.println(parameters);
        File file= new File("." + this.FileName);
        if(!file.exists()){
            notFound();
        }else if(!this.hasParameters){
            if(FileName.compareTo("/")==0){
                SendA("/index.htm");
            }else{
                SendA(FileName);
            }
        }else{
            pw.println("HTTP/1.0 200 OK");
            pw.flush();
            pw.println();
            pw.flush();
            pw.print("<html><head><title>SERVIDOR WEB");
            pw.flush();
            pw.print("</title></head><body bgcolor=\"#4D7902\"><center><h1><br>Parametros Obtenidos por metodo POST..</br></h1>");
            pw.flush();
            pw.print("<h3><b>"+this.parameters+"</b></h3>");
            pw.flush();
            pw.print("</center></body></html>");
            pw.flush();
        }
        
    }
    
    public void methodHEAD(){
        File file= new File("." + this.FileName);
        if(file.exists()){
            System.out.println("Existe");
            pw.println("HTTP/1.0 200 OK");
            pw.flush();
        }else{
            System.out.println("No existe");
            pw.println("HTTP/1.0 404 Not Found");
            pw.flush();
        }
        pw.println();
        pw.flush();
    }
    
    public void methodDELETE(){
        File file= new File("." + this.FileName);
        
        if(!file.exists()){
            System.out.println("No existe");
            pw.println("HTTP/1.0 404 Not Found");
            pw.flush();
            pw.println();
            pw.flush();
            
        }else if(file.isDirectory()){
            pw.println("HTTP/1.0 403 Forbidden");
            pw.flush();
            pw.println();
            pw.flush();
        }else{
            pw.println("HTTP/1.0 200 OK");
            pw.flush();
            pw.println();
            pw.flush();
            file.delete();
        }
        
        
    }
    
    public void notFound(){
        pw.println("HTTP/1.0 404 Not Foud");
        pw.flush();
        pw.println();
        pw.flush();
        pw.print("<html><head><title>Error 404");
        pw.flush();
        pw.print("</title></head><body bgcolor=\"#8B0000\"><center><h1>Error 404</h1><h2>No se encontro el archivo solicitado</h2>");
        pw.flush();
        pw.print("</center></body></html>");
        pw.flush();
    }
    
    
    
    public void getInfoRequest(String line){
        StringTokenizer tokens=new StringTokenizer(line," ?");
        if(tokens.countTokens()==3){//sin parametros
            this.hasParameters = false;
            this.method = tokens.nextToken();
            this.methodID = setMethodID();
            this.FileName = tokens.nextToken();
        }else{//con parametros
            this.hasParameters = true;
            this.method = tokens.nextToken();
            this.methodID = setMethodID();
            this.FileName = tokens.nextToken();
            this.parameters=tokens.nextToken();
        }
        setContentType();
        printInfoRequest();
    }
    
    public void setContentType(){
        if(FileName.endsWith(".jpg"))
            ContentType="image/jpeg";
        if(FileName.endsWith(".htm"))
            ContentType="text/html";
        if(FileName.endsWith(".html"))
            ContentType="text/html";
        if(FileName.endsWith(".pdf"))
            ContentType="application/pdf";
        if(FileName.endsWith(".mp3"))
            ContentType="audio/mpeg";
    }
    
    public void printInfoRequest(){
        System.out.println("Method: " + this.method + "(" + this.methodID + ")");
        System.out.println("Filename: " + this.FileName);
        System.out.println((hasParameters)?"Parameters: " + this.parameters:" ");
    }
    
    public int setMethodID(){
        if(method.equalsIgnoreCase("HEAD"))
            return 1;
        if(method.equalsIgnoreCase("GET"))
            return 2;
        if(method.equalsIgnoreCase("POST"))
            return 3;
        if(method.equalsIgnoreCase("DELETE"))
            return 4;
        return 0;
    }
    
    public void SendA(String arg) {
        try{
            int b_leidos=0;
            BufferedInputStream bis2=new BufferedInputStream(new FileInputStream(arg.substring(1)));
            byte[] buf=new byte[1024];
            int tam_bloque=0;

            if(bis2.available()>=1024){
                tam_bloque=1024;
            }else{
                bis2.available();
            }

            int tam_archivo=bis2.available();
                    String sb = "";
                    sb = sb+"HTTP/1.0 200 ok\n";
                    sb = sb +"Server: Practica4/1.0 \n";
                    sb = sb +"Date: " + new Date()+" \n";
                    sb = sb +"Content-Type: "+this.ContentType+" \n";
                    sb = sb +"Content-Length: "+tam_archivo+" \n";
                    sb = sb +"\n";
                    bos.write(sb.getBytes());
                    bos.flush();


            while((b_leidos=bis2.read(buf,0,buf.length))!=-1){
                bos.write(buf,0,b_leidos);
            }
            bos.flush();
            bis2.close();

        }catch(Exception e){System.out.println(e.getMessage());}


    }
			
}
