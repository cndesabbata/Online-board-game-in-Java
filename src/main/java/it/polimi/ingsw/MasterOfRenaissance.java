package it.polimi.ingsw;

import it.polimi.ingsw.client.view.Cli;
import it.polimi.ingsw.client.view.Gui;
import it.polimi.ingsw.server.serverNetwork.Server;

import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Class MasterOfRenaissance is the main class of the whole project.
 *
 */
public class MasterOfRenaissance {

    /**
     * Allows the user to select GUI, CLI or Server based on the input provided.
     *
     * @param args the arguments provided
     */
    public static void main(String[] args){
        System.out.print("Hi! Welcome to Master Of Renaissance!\nWhat would you like to do?\n");
        System.out.print("0. Launch Server\n1. Launch GUI\n2. Launch CLI\n3. Exit game\n>");
        Scanner scanner = new Scanner(System.in);
        int input;
        boolean request = true;
        while (request){
            try {
                input = scanner.nextInt();
                switch (input) {
                    case 0 -> {Server.main(null); request = false;}
                    case 1 -> {Gui.main(null); request = false;}
                    case 2 -> {Cli.main(null); request = false;}
                    case 3 -> System.exit(0);
                    default -> System.out.println("Invalid argument, please type the number of the desired option:\n>");
                }
            } catch (InputMismatchException e) {
                System.out.println("Numeric format requested.\n>");
                scanner.next();
            }
        }
    }
}
