package org.academiadecodigo.bootcamp.commands;

import org.academiadecodigo.bootcamp.ChatServer;

public class Help extends CommandStrategy {

    public Help(ChatServer server){
        super(server);
    }

    @Override
    public void execute(ChatServer.ClientHandler handler, String message) {

        for (String key : handler.getCommandlist().keySet()){

            handler.send(key);
        }
    }

    @Override
    public String getUsageMessage() {
        return "";
    }
}
