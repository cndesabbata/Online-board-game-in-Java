/* package it.polimi.ingsw.client.view;


import it.polimi.ingsw.client.clientNetwork.ClientConnectionSocket;
import it.polimi.ingsw.constants.Constants;
import it.polimi.ingsw.server.exceptions.InvalidNicknameException;

import java.io.IOException;
import java.io.PrintStream;
import java.util.Scanner;

public class CLI implements Runnable{
    private final PrintStream output;
    private final Scanner input;
    private final ClientView clientView;
    private final MessageHandler messageHandler;
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
        System.out.println(">Insert the server IP address");
        System.out.print(">");
        String ip = scanner.nextLine();
        System.out.println(">Insert the server port");
        System.out.print(">");
        int port = scanner.nextInt();
        Constants.setAddress(ip);
        Constants.setPort(port);
        CLI cli = new CLI();
        cli.run();
    }

    private void setup(){
        String nickname = null;
        boolean confirmation = false;
        while (!confirmation) {
            System.out.println(">Insert your nickname: ");
            System.out.print(">");
            nickname = input.nextLine();
            if (nickname != null) confirmation = true;
        }
        connectionSocket = new ClientConnectionSocket();
        clientView.setNickname(nickname);
        try {
            if(!connectionSocket.setup(nickname, clientView, messageHandler)) {
                System.out.println("The entered IP/port doesn't match any active server. Please try again!");
                CLI.main(null);
            }
            System.out.println("Socket Connection setup completed!");
        } catch (InvalidNicknameException e) {
            setup();
        } catch (IOException e){
            System.err.println("Error during socket configuration! Application will now close.");
            System.exit(0);
        }
    }

    @Override
    public void run() {
        setup();
        while (active) {
            //...
        }
        input.close();
        output.close();
    }
} */