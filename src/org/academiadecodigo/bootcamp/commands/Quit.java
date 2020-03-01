package org.academiadecodigo.bootcamp.commands;

import org.academiadecodigo.bootcamp.ChatServer;

import java.io.IOException;

public class Quit extends CommandStrategy{

    public Quit(ChatServer server){
        super(server);
    }

    @Override
    public void execute(ChatServer.ClientHandler handler, String message) {

        try {
            handler.getClientSocket().close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        server.removeHandler(handler.getAlias());

        server.broadcast(handler.getAlias() + " has left the server.");

    }

    @Override
    public String getUsageMessage() {
        return "";
    }
}
