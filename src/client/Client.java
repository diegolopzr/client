/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.net.*;
import java.io.*;


interface ActionResultClientDelegate {
    public void actionExecuted(boolean completed, String message);
}


/**
 *
 * @author ASUS
 */
public class Client {
 private Socket socket;
    private PrintWriter outputWriter;
    private BufferedReader inputBufferReader;
    public ActionResultClientDelegate resultDelegate;

    private String infoToSend;
    public void startConnectionWithInfo(String info) throws Exception {
        try {
            String address = "127.0.0.1";
            int port = 5520;
            this.socket = new Socket(address, port);
            //System.out.println("Conectado a :" + this.socket.getInetAddress().getHostName());
            this.inputBufferReader = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            this.outputWriter = new PrintWriter(this.socket.getOutputStream(), true);
            this.infoToSend = info;

            this.receiveData();
        } catch (Exception e) {
            System.out.println("error connection with host");
            System.out.println(e.getMessage());
            throw e;
        }
    }

    public void receiveData() throws IOException {
        String incomingMessage = "";
        this.sendInfo(this.infoToSend);
        this.infoToSend = null;

        while ((incomingMessage = this.inputBufferReader.readLine()) != null) {
            incomingMessage = incomingMessage.trim();
            //System.out.println(incomingMessage);
            String[] info = incomingMessage.split(",");
            String action = info[0].trim();

            if (action.contains("action_end")) {
                System.out.println(info[1].trim());
                break;
            }
        }
        //System.out.println("out of the action loop");
        this.resultDelegate.actionExecuted(true, "");
        this.closeSocketConnection();
    }

    public void sendInfo(String info) {
        //System.out.println(info);
        this.outputWriter.println(info);
    }

    public void closeSocketConnection() {
        try {
            this.inputBufferReader.close();
            this.outputWriter.close();
            this.socket.close();
            System.out.println("Conexión terminada");
        } catch (IOException e) {
            System.out.println("IOException on cerrarConexion()");
        } finally {
            System.exit(0);
        }
    }   
}
