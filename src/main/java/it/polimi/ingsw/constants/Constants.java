package it.polimi.ingsw.constants;

public class Constants {
    private static int port;
    private static String address;
    private static final String chooseAction = "0.  Buy resources from the market\n" +
                                        "1.  Buy development card\n" +
                                        "2.  Start production\n" +
                                        "3.  Discard leader card\n" +
                                        "4.  Play leader card\n" +
                                        "5.  Move resources\n" +
                                        "6.  Show your game board\n" +
                                        "7.  Show other player game board\n" +
                                        "8.  Show market\n" +
                                        "9.  Show development card decks\n" +
                                        "10. Show your hand leader cards\n" +
                                        "11. End Turn";

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
