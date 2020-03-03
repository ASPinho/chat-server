package org.academiadecodigo.bootcamp;

import org.academiadecodigo.bootcamp.commands.*;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ChatServer {

    private ServerSocket mySocket;

    private BufferedReader serverIn = new BufferedReader(new InputStreamReader(System.in));
    private BufferedWriter serverOut;

    private ConcurrentHashMap<String, Command> commandlist;
    private ConcurrentHashMap<String, ClientHandler> clientHandlerList;
    private ConcurrentHashMap<String, ConcurrentHashMap<String, ClientHandler>> groupList;
    private ExecutorService fixedPool;

    public static void main(String[] args) {

        ChatServer server = new ChatServer();

        server.serverBoot();

        server.commandListStarter();

        server.listen();
    }

    private void listen() {

        while (true) {

            Socket clientSocket = null;

            try {
                clientSocket = mySocket.accept();
            } catch (IOException e) {
                e.printStackTrace();
            }

            System.out.println("Accepted connection from: " + getAddress(clientSocket));

            ClientHandler clientHandler = new ClientHandler(clientSocket, this);

            fixedPool.submit(clientHandler);

            clientHandlerList.put(clientHandler.getAlias(), clientHandler);
        }
    }

    public void broadcast(String message) {

        for (String alias : clientHandlerList.keySet()) {

            ClientHandler handler = clientHandlerList.get(alias);

            handler.send(message);

        }
    }

    public void removeHandler(String alias) {
        clientHandlerList.remove(alias);
    }

    public ClientHandler getHandler(String alias){
        return clientHandlerList.get(alias);
    }

    public int getHandlerListSize(){
        return clientHandlerList.size();
    }

    public ConcurrentHashMap<String, ClientHandler> getClientHandlerList() {
        return clientHandlerList;
    }

    private String getAddress(Socket socket) {

        return socket.getInetAddress().getHostName() + ":" + socket.getPort();
    }

    public ConcurrentHashMap<String, Command> getCommandlist() {
        return commandlist;
    }

    public boolean groupExists(String groupname){

        return groupList.containsKey(groupname);
    }

    public void newGroup(String groupname, ClientHandler handler){
        groupList.put(groupname, new ConcurrentHashMap<>());
    }

    private void commandListStarter(){

        commandlist = new ConcurrentHashMap<>();

        commandlist.put("/quit", new Quit(this));
        commandlist.put("/changealias", new ChangeAlias(this));
        commandlist.put("/list", new List(this));
        commandlist.put("/help", new Help(this));
        commandlist.put("/votekick", new VoteKick(this));
        commandlist.put("/w", new Whisper(this));
        commandlist.put("/group", new Group(this));
    }

    private void serverBoot() {

        System.out.print("Port to open: ");

        try {

            int portNumber = Integer.parseInt(serverIn.readLine());
            mySocket = new ServerSocket(portNumber);
            System.out.println("Server booted. Awaiting connections...");

        } catch (IOException e) {
            e.printStackTrace();
        }

        clientHandlerList = new ConcurrentHashMap<>();
        groupList = new ConcurrentHashMap<>();
        fixedPool = Executors.newFixedThreadPool(1000);

    }

    public class ClientHandler implements Runnable {

        private volatile String alias;
        private Socket clientSocket;
        private ChatServer server;

        private BufferedReader clientIn;
        private PrintWriter clientOut;

        ClientHandler(Socket socket, ChatServer server) {

            clientSocket = socket;
            this.server = server;
            streamStarter();
            askForAlias();
            broadcast(alias + " has entered the server.");
        }

        @Override
        public void run() {

            listenToClient();
        }

        public synchronized void askForAlias() {
            clientOut.println("Welcome to HigherArki's Server!");

            try {
                clientOut.println("Pick an alias: ");
                alias = clientIn.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }

            clientOut.println("Name accepted.");
            notifyAll();
        }

        private synchronized void listenToClient() {

            while (!clientSocket.isClosed()) {

                String message = null;
                String messageCommand = "";

                try {

                    message = clientIn.readLine();
                    messageCommand = message.split(" ")[0];

                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (message == null) {
                    try {

                        clientSocket.close();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                }

                if (commandlist.containsKey(messageCommand)){

                    commandlist.get(messageCommand).execute(this, message);
                    continue;
                }
                broadcast(alias + ": " + message);
            }
            removeHandler(alias);
        }

        private void streamStarter() {

            try {
                clientIn = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                clientOut = new PrintWriter(clientSocket.getOutputStream(), true);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public String getAlias() {
            return alias;
        }

        public void send(String message) {

            clientOut.println(message);

        }

        public void getKicked(){
            try {

                clientSocket.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
            server.removeHandler(alias);
        }

        public Socket getClientSocket() {
            return clientSocket;
        }
    }
}
