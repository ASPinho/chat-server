package org.academiadecodigo.bootcamp.commands;

import org.academiadecodigo.bootcamp.ChatServer;

import java.io.IOException;

public class Quit implements Command{

    private ChatServer server;
    private ChatServer.ClientHandler handler;

    public Quit(ChatServer server, ChatServer.ClientHandler handler){
        this.server = server;
        this.handler = handler;
    }

    @Override
    public void execute(String message) {

        try {
            handler.getClientSocket().close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        server.removeHandler(handler.getAlias());

        for (String alias : server.getClientHandlerList().keySet()){
            handler.send(this.handler.getAlias() + " has left the server.");
        }
    }

    @Override
    public String getUsageMessage() {
        return "";
    }
}
