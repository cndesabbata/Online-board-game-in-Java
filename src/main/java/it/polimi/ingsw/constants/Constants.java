package it.polimi.ingsw.constants;

/**
 * Class Constants contains the constants used in the project.
 *
 */
public class Constants {
    private static int port;
    private static String address;
    private static final String chooseAction = "0.  Buy resources from the market  " + "4.  Play leader card              " + "8.  Show market\n" +
                                               "1.  Buy development card           " + "5.  Move resources                " + "9.  Show development card decks\n" +
                                               "2.  Start production               " + "6.  Show your game board          " + "10. Show your hand leader cards\n" +
                                               "3.  Discard leader card            " + "7.  Show other player game board  " + "11. End Turn\n";

    public static final String devCardBorder = "***********************************";
    public static final String leadCardBorder = "***************************************";
    public static final String emptyDevCardBorder = "*                                 * ";

    /**
     * Returns the address attribute of this Constants object, which represents
     * the ip address of the server.
     *
     * @return the ip address of the server
     */
    public static String getAddress() {
        return address;
    }

    /**
     * Sets the address attribute of this Constants object, which represents
     * the ip address of the server.
     *
     * @param address the ip address of the server
     */
    public static void setAddress(String address) {
        Constants.address = address;
    }

    /**
     * Returns the chooseAction attribute of this Constants object, which
     * represents the menu used in CLI during the game.
     *
     * @return the menu used in CLI during the game
     */
    public static String getChooseAction() {
        return chooseAction;
    }

    /**
     * Sets the port attribute of this Constants object, which represents
     * the port of the server.
     *
     * @param port the port the server is listening on
     */
    public static void setPort(int port) {
        Constants.port = port;
    }

    /**
     * Returns the port attribute of this Constants object, which represents
     * the port of the server.
     *
     * @return the port of the server
     */
    public static int getPort() {
        return port;
    }
}
