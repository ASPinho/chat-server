package org.academiadecodigo.bootcamp.commands;

import org.academiadecodigo.bootcamp.ChatServer;

public class Group extends CommandStrategy{

    public Group(ChatServer server){
        super(server);
    }

    @Override
    public void execute(ChatServer.ClientHandler handler, String message) {

        String[] splitMessage = message.split(" ");

        switch (splitMessage.length){

            case 1:
                handler.send(getUsageMessage());
                break;

            case 2:
                if (server.groupExists(splitMessage[1])){



                }




        }

    }

    @Override
    public String getUsageMessage() {
        return "[USAGE] /group GroupName Message";
    }
}
