package elements;

import cli.CommandProcessor;
import network.SimpleSocket;

public class ServerConnectManager {

    public final String host;
    public final int port;

    private SimpleSocket socket = null;
    private final CommandProcessor commandProcessor = new CommandProcessor();

    public ServerConnectManager(String host, int port) {
        this.host = host;
        this.port = port;
        registerClientsideCommands();
    }

    boolean isConnected() {
        return socket != null;
    }

    /**
     * Создает, если это возможно, соединение с сервером и начинает прослушивать сообщения.
     */
    public void connect() {
        socket = new SimpleSocket(host, port);
        if (socket.isClosed())
            socket = null;
        else {
            System.out.println("Connected to the server");
            processConnection();
        }
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

    boolean isDisconnected() {
        return socket == null;
    }

    public void processConnection() {
        new Thread(() -> {
            while (isConnected()) {
                if (socket.hasNewMessage())
                    System.out.println(socket.receiveMessage());
                else {
                    disconnect();
                    break;
                }
            }
        }).start();
    }

    public void exit() {
        disconnect();
        System.exit(0);
    }

    private void registerClientsideCommands() {

        commandProcessor.register("exit", (it) -> it
                .executes(this::exit)
        );
        commandProcessor.register("retry", (it) -> it
                .require("Already connected.", this::isDisconnected)
                .executes(this::connect)
        );

    }
}
