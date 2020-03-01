package org.academiadecodigo.bootcamp.commands;

import org.academiadecodigo.bootcamp.ChatServer;

import java.util.concurrent.atomic.AtomicInteger;

public class VoteKick extends CommandStrategy{

    public VoteKick(ChatServer server){
        super(server);
    }

    @Override
    public void execute(ChatServer.ClientHandler handler, String message) {

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
