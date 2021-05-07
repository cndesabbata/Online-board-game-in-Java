package it.polimi.ingsw.constants;

public class Constants {
    private static int port;
    private static String address;

    public static String getAddress() {
        return address;
    }

    public static void setAddress(String address) {
        Constants.address = address;
    }

    public static void setPort(int port) {
        Constants.port = port;
    }

    public static int getPort() {
        return port;
    }
}
