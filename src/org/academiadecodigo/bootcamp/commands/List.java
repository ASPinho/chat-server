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
    public void execute() {

        for (ChatServer.ClientHandler handler : server.getClientHandlerList()){

            this.handler.send(handler.getAlias());
        }
    }
}
