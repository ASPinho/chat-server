package org.academiadecodigo.bootcamp;

import org.academiadecodigo.bootcamp.commands.*;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ChatServer {

    private int portNumber;
    private ServerSocket mySocket;

    private BufferedReader serverIn = new BufferedReader(new InputStreamReader(System.in));
    private BufferedWriter serverOut;

    private LinkedList<ClientHandler> clientHandlerList;
    private ExecutorService fixedPool;


    public static void main(String[] args) {

        ChatServer server = new ChatServer();

        server.serverBoot();

        server.listen();

    }

    private void serverBoot() {

        System.out.print("Port to open: ");

        try {

            portNumber = Integer.parseInt(serverIn.readLine());
            mySocket = new ServerSocket(portNumber);
            System.out.println("Server booted. Awaiting connections...");

        } catch (IOException e) {
            e.printStackTrace();
        }

        clientHandlerList = new LinkedList<>();
        fixedPool = Executors.newFixedThreadPool(1000);

    }

    private void listen() {

        while (true) {

            Socket clientSocket = null;

            try {
                clientSocket = mySocket.accept();
            } catch (IOException e) {
                e.printStackTrace();
            }

            ClientHandler clientHandler = new ClientHandler(clientSocket, this);

            clientHandlerList.offer(clientHandler);

            System.out.println("Accepted connection from: " + getAddress(clientSocket));

            fixedPool.submit(clientHandler);
        }


    }

    private void broadcast(String message) {

        for (ClientHandler clientHandler : clientHandlerList) {

            clientHandler.send(message);

        }
    }

    private void removeHandler(ClientHandler handler) {
        clientHandlerList.remove(handler);
    }

    private String getAddress(Socket socket) {

        return socket.getInetAddress().getHostName() + ":" + socket.getPort();

    }

    public class ClientHandler implements Runnable {

        private volatile String alias;
        private Socket clientSocket;
        private ChatServer server;

        private BufferedReader clientIn;
        private PrintWriter clientOut;

        private ConcurrentHashMap<String, Command> commandlist;

        ClientHandler(Socket socket, ChatServer server) {

            clientSocket = socket;
            this.server = server;
            streamStarter();
            commandListStarter();
        }

        @Override
        public void run() {

            askForAlias();

            listenToClient();

        }

        private synchronized void askForAlias() {
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
                try {

                    message = clientIn.readLine();

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

                if (commandlist.containsKey(message)){

                    commandlist.get(message).execute();
                    continue;

                }

                server.broadcast(alias + ": " + message);
            }

            server.removeHandler(this);

        }

        private void streamStarter() {

            try {
                clientIn = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                clientOut = new PrintWriter(clientSocket.getOutputStream(), true);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private void commandListStarter(){

            commandlist = new ConcurrentHashMap<>();

            commandlist.put("/quit", new Quit(server, this));
            commandlist.put("/changealias", new ChangeAlias(server, this));
            commandlist.put("/list", new List(server, this));
            commandlist.put("/help", new Help(server, this));
            commandlist.put("/votekick", new VoteKick(server, this));
            commandlist.put("/w", new Whisper(server, this));

        }

        public String getAlias() {
            return alias;
        }

        void send(String message) {

            clientOut.println(message);

        }

        private void close() {
            try {
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}
