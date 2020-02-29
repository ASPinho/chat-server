package org.academiadecodigo.bootcamp.commands;

public interface Command {

    public void execute(String message);

    public String getUsageMessage();

}
