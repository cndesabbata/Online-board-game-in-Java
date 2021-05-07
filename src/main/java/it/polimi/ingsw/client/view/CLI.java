package it.polimi.ingsw.client.view;

import it.polimi.ingsw.client.clientNetwork.ClientConnectionSocket;
import it.polimi.ingsw.constants.Constants;
import it.polimi.ingsw.client.clientNetwork.MessageHandler;
import it.polimi.ingsw.messages.Message;
import it.polimi.ingsw.server.observer.Observer;

import java.io.IOException;
import java.io.PrintStream;
import java.util.Scanner;

public class CLI implements Observer {
    private final PrintStream output;
    private final Scanner input;
    private final ClientView clientView;
    private final MessageHandler messageHandler;
    private ActionFactory actionFactory;
    private boolean active;
    private ClientConnectionSocket connectionSocket;

    public CLI() {
        input = new Scanner(System.in);
        output = new PrintStream(System.out);
        clientView = new ClientView(this);
        messageHandler = new MessageHandler(clientView);
        active = true;
    }

    public static void main(String[] args){
        Scanner scanner = new Scanner(System.in);
        System.out.print("Insert the server IP address\n>");
        String ip = scanner.nextLine();
        System.out.print("Insert the server port\n>");
        int port = scanner.nextInt();
        Constants.setAddress(ip);
        Constants.setPort(port);
        CLI cli = new CLI();
        cli.setup();
    }

    public boolean isActive() {
        return active;
    }

    private void setup(){
        connectionSocket = new ClientConnectionSocket(this, messageHandler);
        try {
            while(!connectionSocket.setupConnection(clientView)) {
                output.println("The entered IP/port doesn't match any active server. Please try again!");
                output.print("Insert the server IP address\n>");
                Constants.setAddress(input.nextLine());
                output.print("Insert the server port\n>");
                Constants.setPort(input.nextInt());
            }
            System.out.println("Socket Connection setup completed!");
        } catch (IOException e){
            System.err.println("Error during socket configuration! Application will now close.");
            System.exit(0);
        }
        actionFactory = new ActionFactory(this, connectionSocket);
        String nickname = null;
        boolean confirmation = false;
        boolean newPlayer = true;
        while (!confirmation) {
            boolean ok = false;
            while (!ok){
                output.print("Would you like to start a new match or resume an existing one? [start/resume]\n>");
                String answer = input.nextLine().toUpperCase();
                switch (answer) {
                    case "START" -> {
                        newPlayer = true;
                        ok = true;
                    }
                    case "RESUME" -> {
                        newPlayer = false;
                        ok = true;
                    }
                    default -> output.println("Please select one of the two options.");
                }
            }
            output.print("Insert your nickname:\n>");
            nickname = input.nextLine();
            if (nickname != null && !nickname.isEmpty() && connectionSocket.setupNickname(nickname, newPlayer))
                confirmation = true;
        }
        clientView.setNickname(nickname);
    }

    @Override
    public void update(Message message) {

    }
}