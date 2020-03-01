package org.academiadecodigo.bootcamp.commands;

import org.academiadecodigo.bootcamp.ChatServer;

public class ChangeAlias extends CommandStrategy {

    public ChangeAlias(ChatServer server){
       super(server);
    }

    @Override
    public void execute(ChatServer.ClientHandler handler, String message) {

        handler.askForAlias();
    }

    @Override
    public String getUsageMessage() {
        return "";
    }
}
