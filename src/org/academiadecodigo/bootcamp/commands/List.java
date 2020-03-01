package org.academiadecodigo.bootcamp.commands;

import org.academiadecodigo.bootcamp.ChatServer;

public class List implements Command{

    private ChatServer server;
    private ChatServer.ClientHandler handler;

    public List(ChatServer server, ChatServer.ClientHandler handler){
        this.server = server;
        this.handler = handler;
    }

    @Override
    public void execute(String message) {

        for (String alias : server.getClientHandlerList().keySet()){

            this.handler.send(alias);
        }
    }

    @Override
    public String getUsageMessage() {
        return "";
    }
}
