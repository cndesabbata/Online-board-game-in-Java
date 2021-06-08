package it.polimi.ingsw.constants;

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

    public static String getAddress() {
        return address;
    }

    public static void setAddress(String address) {
        Constants.address = address;
    }

    public static String getChooseAction() {
        return chooseAction;
    }

    public static void setPort(int port) {
        Constants.port = port;
    }

    public static int getPort() {
        return port;
    }
}
