package com.hjl.openssl;

import java.io.BufferedReader;  
import java.io.FileInputStream;  
import java.io.IOException;  
import java.io.InputStreamReader;  
import java.io.PrintWriter;  
import java.net.Socket;  
import java.security.KeyStore;  
  
import java.util.Scanner;

import javax.net.ssl.KeyManagerFactory;  
import javax.net.ssl.SSLContext;  
import javax.net.ssl.SSLSocketFactory;  
import javax.net.ssl.TrustManagerFactory;  
  
public class FoxClient {  
    public static void main(String[] args) throws Exception {  
        String clientKeyStoreFile = "/Users/karyuu/Documents/keystore/foxclient.keystore";  
        String clientKeyStorePwd = "foxclientks";  
        String foxclientKeyPwd = "foxclient";  
        String clientTrustKeyStoreFile = "/Users/karyuu/Documents/keystore/foxclienttrust.keystore";  
        String clientTrustKeyStorePwd = "foxclienttrustks";  
  
        KeyStore clientKeyStore = KeyStore.getInstance("JKS");  
        clientKeyStore.load(new FileInputStream(clientKeyStoreFile), clientKeyStorePwd.toCharArray());  
  
        KeyStore clientTrustKeyStore = KeyStore.getInstance("JKS");  
        clientTrustKeyStore.load(new FileInputStream(clientTrustKeyStoreFile), clientTrustKeyStorePwd.toCharArray());  
  
        KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());  
        kmf.init(clientKeyStore, foxclientKeyPwd.toCharArray());  
  
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());  
        tmf.init(clientTrustKeyStore);  
  
        SSLContext sslContext = SSLContext.getInstance("TLSv1");  
        sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);  
          
        SSLSocketFactory socketFactory = sslContext.getSocketFactory();  
        Socket socket = socketFactory.createSocket("localhost", CatServer.SERVER_PORT);  
          
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);  
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));  
        //send("hello", out);  
        //send("exit", out);  
          
        Scanner sc = new Scanner(System.in);
        sc.useDelimiter("\n");
        String input = "";
        while  ((input = sc.nextLine()) != null && !input.trim().equalsIgnoreCase("exit")) {
        	send(input, out);
        	System.out.println("Received: " + in.readLine());
        	//receive(in); 
        }
        socket.close();  
    }  
      
    public static void send(String s, PrintWriter out) throws IOException {  
        System.out.println("Sending: " + s);       
        out.println(s);  
    }  
  
    public static void receive(BufferedReader in) throws IOException {  
        String s;  
        while ((s = in.readLine()) != null) {  
            System.out.println("Reveived: " + s);  
        }  
    }  
}
