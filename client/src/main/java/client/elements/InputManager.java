package client.elements;

import client.elements.cli.ServersideCommands;
import utils.Ansi;
import utils.cli.CommandProcessor;
import utils.elements.ClientTypes;

import java.util.Scanner;

import static client.elements.ServerConnectManager.exit;
import static utils.cli.CommandErrors.PHANTOM_COMMAND;

public class InputManager {

    private final CommandProcessor commandProcessor = new CommandProcessor();
    private final Scanner in = new Scanner(System.in);

    public InputManager() {
        ServersideCommands.init(commandProcessor);
    }

    boolean isConnected() {
        return ServerConnectManager.socket != null;
    }

    public void startInputThread() {
        new Thread(() -> {
            while (true) {
                if (!in.hasNextLine()) {
                    exit();
                    return;
                }
                var msg = in.nextLine();
                processInput(msg);
            }
        }).start();

    }

    /**
     * Получение сообщения от клиента.
     */
    @SuppressWarnings("checkstyle:LineLength")
    public void processInput(String msg) {
        if (msg == null || msg.isEmpty())
            return;
        if (msg.charAt(0) == '/') {
            commandProcessor.execute(msg, null);
            var procError = commandProcessor.getLastError();
            var procOutput = commandProcessor.getOutput();
            if (procError != null) {
                if (procError.type == PHANTOM_COMMAND)
                    send(msg);
                else {
                    boolean isHtml = Client.getType() != ClientTypes.CONSOLE;
                    OutputManager.print(procError.getMessage(isHtml));
                }
            } else if (!procOutput.isEmpty()) {
                OutputManager.print(procOutput);
            }
            return;
        }
        send(msg);

    }

    private void send(String msg) {
        if (isConnected())
            ServerConnectManager.socket.sendln(msg);
        else {
            OutputManager.stylePrint("Not connected to server.", Ansi.Colors.RED);
        }
    }
}
