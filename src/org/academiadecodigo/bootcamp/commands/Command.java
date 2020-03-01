package org.academiadecodigo.bootcamp.commands;

import org.academiadecodigo.bootcamp.ChatServer;

public interface Command {

    public void execute(ChatServer.ClientHandler handler, String message);

    public String getUsageMessage();

}
