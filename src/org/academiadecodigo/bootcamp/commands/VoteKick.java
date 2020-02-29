package org.academiadecodigo.bootcamp.commands;

import org.academiadecodigo.bootcamp.ChatServer;

public class VoteKick implements Command{

    private ChatServer server;
    private ChatServer.ClientHandler handler;

    public VoteKick(ChatServer server, ChatServer.ClientHandler handler){
        this.server = server;
        this.handler = handler;
    }

    @Override
    public void execute() {

    }
}
