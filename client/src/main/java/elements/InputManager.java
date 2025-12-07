package elements;

import cli.CommandProcessor;
import network.SimpleSocket;

import java.util.Scanner;

import static cli.CommandResults.PHANTOM_COMMAND;

public class InputManager {

    private final CommandProcessor commandProcessor = new CommandProcessor();
    private SimpleSocket socket = null;
    private final Scanner in = new Scanner(System.in);

    boolean isConnected() {
        return socket != null;
    }

    public void exit() {
        disconnect();
        System.exit(0);
    }

    /**
     * Разрывает соединение с сервером, если таковое имеется. <br>
     * Все потоки, работающие с ним также будут автоматически остановлены.
     */
    public void disconnect() {
        if (socket == null)
            return;

        socket.close();
        socket = null;
        System.out.println("Disconnected from the server");
    }

    public void processInput() {
        while (true) {

            if (!in.hasNextLine()) {
                exit();
                return;
            }
            var msg = in.nextLine();

            if (msg.charAt(0) == '/') {
                commandProcessor.execute(msg,
                        new ServerCommands.ServerContextData(null, null, null, null, socket));
                var procError = commandProcessor.getLastError();
                var procOutput = commandProcessor.getOutput();
                if (procError != null) {
                    if (procError.type == PHANTOM_COMMAND)
                        send(msg);
                    else
                        procError.explain();
                } else if (!procOutput.isEmpty())
                    System.out.print(procOutput);

                continue;
            }

            send(msg);
        }
    }

    private void send(String msg) {
        if (isConnected())
            socket.sendMessage(msg);
        else
            System.err.println("Not connected to server.");
    }

}
