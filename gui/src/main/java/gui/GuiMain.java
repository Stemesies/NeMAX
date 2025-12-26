package gui;

import client.elements.Client;
import javafx.application.Application;
import utils.elements.ClientTypes;

import java.util.concurrent.CompletableFuture;

public class GuiMain {

    public static void main(String[] args) {
        CompletableFuture.supplyAsync(() -> {
            Client.launch(ClientTypes.GUI);
            return null;
        });
        Application.launch(ClientApplication.class);
    }
}
