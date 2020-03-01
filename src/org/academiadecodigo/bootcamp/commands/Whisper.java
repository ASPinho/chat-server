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
    public void execute(String message) {

        String[] splitMessage = message.split(" ");

        if (splitMessage.length < 3){
            handler.send(getUsageMessage());
            return;
        }
        String messageOut = message.substring(message.indexOf(" ", message.indexOf(" ") + 1));

        server.getClientHandlerList().get(splitMessage[1]).send(handler.getAlias() + " (whisper):" + messageOut);
    }

    @Override
    public String getUsageMessage() {
        return "[USAGE] /w Alias Message";
    }
}
