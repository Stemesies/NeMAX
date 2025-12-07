package elements;

import java.util.ArrayList;
import java.util.HashMap;

public class Client {

    public static String openChatId;

    static ServerConnectManager scm = new ServerConnectManager("127.0.0.1", 8080);

    static InputManager input = new InputManager();

    public HashMap<String, ArrayList<String>> map = new HashMap<>(20);

    public static void launch() {
        scm.connect();
        input.processInput();
    }

    /**
     * Добавляет непрочитанное сообщение в контейнер непрочитанных. <br>
     * Сохраняет данные по ключу #groupName
     *
     * @param groupName - "строковый" id группы
     * @param msg - новое сообщение
     */
    public void addUnreadMsg(String groupName, Object msg) {
        ArrayList<String> unread = this.map.get(groupName);
        unread.add(msg.toString());
        this.map.put(groupName, unread);
    }

    /**
     * Удаляет сообщения из непрочитанных при открытии группы.
     *
     * @param groupName - "строковый" id открытого чата
     */
    public void readMessage(String groupName) {
        this.map.remove(groupName);
    }
}
