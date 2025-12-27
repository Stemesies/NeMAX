package client;

import client.elements.Client;
import client.elements.InputManager;
import utils.elements.ClientTypes;
import client.elements.cli.ServerCommands;

public class ClientMain {

    public static void main(String[] args) {
        ServerCommands.initGeneral();
        Client.launch(ClientTypes.CONSOLE);
        InputManager.startInputThread();
    }
}
