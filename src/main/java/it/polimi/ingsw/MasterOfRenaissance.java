package it.polimi.ingsw;

import it.polimi.ingsw.client.view.Cli;
import it.polimi.ingsw.client.view.Gui;
import it.polimi.ingsw.server.serverNetwork.Server;

import java.util.InputMismatchException;
import java.util.Scanner;

public class MasterOfRenaissance {

    public static void main(String[] args){
        System.out.print("Hi! Welcome to Master Of Renaissance!\nWhat would you like to do?");
        System.out.print("0. Launch Server\n1. Launch GUI\n2. Launch CLI\n3. Exit game\n>");
        Scanner scanner = new Scanner(System.in);
        int input = 0;
        while (true){
            try {
                input = scanner.nextInt();
                switch (input) {
                    case 0 : Server.main(null);
                    case 1 : Gui.main(null);
                    case 2 : Cli.main(null);
                    case 3 : System.exit(0);
                    default : System.out.println("Invalid argument, please type the number of the desired option:\n>");
                }
            } catch (InputMismatchException e) {
                System.out.println("Numeric format requested.\n>");
                scanner.next();
            }
        }
    }
}
