package org.academiadecodigo.bootcamp.commands;

import org.academiadecodigo.bootcamp.ChatServer;

public class Whisper implements Command{

    private ChatServer server;
    private ChatServer.ClientHandler handler;

    public Whisper(ChatServer server, ChatServer.ClientHandler handler){
        this.server = server;
        this.handler = handler;
    }

    @Override
    public void execute() {

    }
}
