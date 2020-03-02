package org.academiadecodigo.bootcamp.commands;

import org.academiadecodigo.bootcamp.ChatServer;

public class Group extends CommandStrategy{

    public Group(ChatServer server){
        super(server);
    }

    @Override
    public void execute(ChatServer.ClientHandler handler, String message) {

    }

    @Override
    public String getUsageMessage() {
        return "[USAGE] /group GroupName Message";
    }
}
