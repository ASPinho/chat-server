package org.academiadecodigo.bootcamp.commands;

public class Tester {

    public static void main(String[] args) {

        Command quit = new Quit();

        System.out.println(quit.getClass());

    }

}
