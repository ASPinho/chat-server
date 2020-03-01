package org.academiadecodigo.bootcamp.commands;

import org.academiadecodigo.bootcamp.ChatServer;

public abstract class CommandStrategy implements Command {

    protected ChatServer server;

    public CommandStrategy(ChatServer server){
        this.server = server;
    }

}
