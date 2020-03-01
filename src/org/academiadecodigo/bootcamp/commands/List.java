package org.academiadecodigo.bootcamp.commands;

import org.academiadecodigo.bootcamp.ChatServer;

public class List extends CommandStrategy{

    public List(ChatServer server){
        super(server);
    }

    @Override
    public void execute(ChatServer.ClientHandler handler, String message) {

        for (String alias : server.getClientHandlerList().keySet()){

            handler.send(alias);
        }
    }

    @Override
    public String getUsageMessage() {
        return "";
    }
}
