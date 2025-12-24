package client;

import client.elements.Client;

public class ClientMain {

    public static void main(String[] args) {
        ServerCommands.initGeneral();
        Client.launch();
        Client.inputManager.startInputThread();
    }
}
