package it.polimi.ingsw.communication.client;

import it.polimi.ingsw.view.ViewInterface;
import it.polimi.ingsw.view.gui.GUI;
import javafx.application.Application;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client extends Thread implements Closeable {
    private static String host;
    private static int port;
    private boolean go;
    private Socket connection;
    private BufferedReader in;
    private PrintWriter out;
    private ViewInterface view;
    private MessageHandler messageHandler;

    public Client(ViewInterface view) {
        this.view = view;
        this.go = false;
    }

    public void setMessageHandler(MessageHandler messageHandler) {
        this.messageHandler = messageHandler;
    }

    public void setGo(boolean go) {
        this.go = go;
    }

    public void init() throws IOException {
        connection = new Socket(host, port);
        in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        out = new PrintWriter(connection.getOutputStream(), true);
    }

    public String receive() throws IOException {
        return in.readLine();
    }

    public void send(String message) {
        out.println(message);
    }

    public void close() throws IOException {
        in.close();
        out.close();
        connection.close();

        try {
            view.stopView();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void run(){
        Scanner writed = new Scanner(System.in);
        try {
            String received = null;
            do {
                received = this.receive();
                System.out.println(received); //sarà da togliere
                messageHandler.understandMessage(received);
                synchronized (this){
                    if(!go){
                        this.wait();
                    }
                    this.send(messageHandler.correctToSend());
                }
                received = this.receive();
                System.out.println(received); //sarà da togliere
                messageHandler.understandReceived(received);
            } while (received != null);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            try {
                this.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws IOException {
        if (args.length == 0) {
            System.out.println("Provide host:port please");
            return;
        }

        String[] tokens = args[0].split(":");

        if (tokens.length < 2) {
            throw new IllegalArgumentException("Bad formatting: " + args[0]);
        }

        host = tokens[0];
        port = Integer.parseInt(tokens[1]);

        Application.launch(GUI.class,args);
    }

    //platform.runlater

}