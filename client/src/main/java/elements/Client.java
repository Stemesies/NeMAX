package elements;

import java.util.ArrayList;
import java.util.HashMap;

public class Client {

    public HashMap<String, ArrayList<String>> map = new HashMap<>(20);

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
     * @param groupName - "строковый" id открытой группы
     */
    public void readMessage(String groupName) {
        this.map.remove(groupName);
    }
}
