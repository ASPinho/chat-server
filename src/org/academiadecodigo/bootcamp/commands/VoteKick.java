package org.academiadecodigo.bootcamp.commands;

import org.academiadecodigo.bootcamp.ChatServer;

import java.util.concurrent.atomic.AtomicInteger;

public class VoteKick implements Command{

    private ChatServer server;
    private ChatServer.ClientHandler handler;

    public VoteKick(ChatServer server, ChatServer.ClientHandler handler){
        this.server = server;
        this.handler = handler;
    }

    @Override
    public void execute(String message) {

        AtomicInteger votes = new AtomicInteger(1);

        AtomicInteger votesToPass = new AtomicInteger(server.getClientHandlerList().size() / 2);

        String aliasToKick = message.split(" ")[1];

        ChatServer.ClientHandler handlerToKick = server.getClientHandlerList().get(aliasToKick);
        
        votes.incrementAndGet();

        if(votes.equals(votesToPass)){

            handlerToKick.getKicked();

            server.removeHandler(aliasToKick);
        }

    }

    @Override
    public String getUsageMessage() {
        return "";
    }
}
