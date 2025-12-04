package elements;

import network.SimpleSocket;

/**
 * Репрезентация клиента со стороны сервера.
 * <br>Содержит в себе всю информацию о подключении,
 * привязку пользователя и т.д.
 */
public class Client {

    /**
     * Текущий привязанный профиль пользователя.
     * <ul>
     *     <li> <code>-1</code> - если профиль отсутствует
     *     <li> <code>int</code> - если на клиенте был произведен вход/регистрация
     * </ul>
     */
    int user = -1;

    SimpleSocket socket;

    public Client(SimpleSocket socket) {
        this.socket = socket;
    }

    public void close() {
        socket.close();
    }


    /**
     * @see SimpleSocket#sendMessage(String)
     */
    public void sendMessage(String message) throws IllegalStateException {
        socket.sendMessage(message);
    }

    /**
     * @see SimpleSocket#hasNewMessage()
     */
    public boolean hasNewMessage() {
        return socket.hasNewMessage();
    }

    /**
     * @see SimpleSocket#receiveMessage()
     */
    public String receiveMessage() throws IllegalStateException {
        return socket.receiveMessage();
    }

}
