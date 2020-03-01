package org.academiadecodigo.bootcamp.commands;

import org.academiadecodigo.bootcamp.ChatServer;

public class Whisper extends CommandStrategy{

    public Whisper(ChatServer server){
        super(server);
    }

    @Override
    public void execute(ChatServer.ClientHandler handler, String message) {

        String[] splitMessage = message.split(" ");

        if (splitMessage.length < 3){
            handler.send(getUsageMessage());
            return;
        }
        String messageOut = message.substring(message.indexOf(" ", message.indexOf(" ") + 1));

        server.getHandler(splitMessage[1]).send(handler.getAlias() + " (whisper):" + messageOut);
    }

    @Override
    public String getUsageMessage() {
        return "[USAGE] /w Alias Message";
    }
}
