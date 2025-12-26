package client.elements;

import utils.elements.ClientTypes;
import utils.kt.Apply;

import java.util.ArrayList;
import java.util.List;

public class OutputManager {

//    public OutputManager() {
//        ServerConnectManager.addOutPutListener();
//    }

    private static final List<Apply<String>> outputListeners = new ArrayList<>();

    public static void addOutPutListener(Apply<String> listener) {
        outputListeners.add(listener);
    }

    public static void print(String message) {
        outputListeners.forEach(it -> it.run(message));
    }

    public static void stylePrint(String message) {
        if (Client.getType() == ClientTypes.CONSOLE) {

        } else {

        }
    }
}
