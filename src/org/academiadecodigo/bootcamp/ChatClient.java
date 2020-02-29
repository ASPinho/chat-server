package org.academiadecodigo.bootcamp;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class ChatClient {

    private Socket mySocket;
    private Scanner clientIn;

    private PrintWriter clientOut;
    private BufferedReader serverIn;

    public static void main(String[] args) {

        ChatClient client = new ChatClient();

        try {

            client.setHost();

        } catch (IOException e) {
            e.printStackTrace();
        }

        client.streamStarter();

        Thread clientThread = new Thread(client.newHandler());
        clientThread.start();

        while (client.socketIsOpen()){

            client.getServerInput();
        }

        System.exit(1);

    }

    private void getServerInput() {

        String serverMsg = null;

        try {

            serverMsg = serverIn.readLine();

        } catch (IOException e) {
            e.printStackTrace();
        }

        if (serverMsg == null){
            try {

                mySocket.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }

        System.out.println(serverMsg);

    }

    private void sendClientInput(){

        String msg = null;

        while (!mySocket.isClosed()){

            msg = clientIn.nextLine();
            clientOut.println(msg);

        }

    }

    private Runnable newHandler(){

        return new Runnable() {

            @Override
            public void run() {

                sendClientInput();
            }
        };
    }

    private void setHost() throws IOException {

        clientIn = new Scanner(System.in);

        System.out.print("Connect to host: ");
        String server = clientIn.nextLine();

        System.out.print("Server port: ");
        int serverPort = Integer.parseInt(clientIn.nextLine());

        mySocket = new Socket(server, serverPort);
        System.out.println("Connecting to " + server + ":" + serverPort + "...");
    }

    private void streamStarter(){

        try {

            clientOut = new PrintWriter(mySocket.getOutputStream(), true);
            serverIn = new BufferedReader(new InputStreamReader(mySocket.getInputStream()));

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private boolean socketIsOpen(){
        return !mySocket.isClosed();
    }
}
