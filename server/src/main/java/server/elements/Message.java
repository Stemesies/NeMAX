package server.elements;

import utils.Ansi;
import utils.StringPrintWriter;

import java.sql.Timestamp;

public class Message {
    private final int id;
    private final String content;
    private final String senderName; // по id
    private final int senderId;
    private final Timestamp time;

    public Message(int id, String content, String senderName, int senderId, Timestamp time) {
        this.id = id;
        this.content = content;
        this.senderName = senderName;
        this.senderId = senderId;
        this.time = time;
    }

    public String getFormatted() {
        return "[" + senderName + "] " + content;
    }

    public String getFormattedSelf(boolean isHtml) {
        var offset = getOffset(getFormatted());
        return Ansi.applyChoose(offset + getFormatted(), Ansi.Colors.YELLOW, isHtml);
    }

    public String getContent() {
        return content;
    }

    public String getSenderName() {
        return senderName;
    }

    public int getSenderId() {
        return senderId;
    }

    /**
     * Выводит сообщение от себя в чат пользователя.
     *
     * @param message - сообщение в чат
     */
    public static String getOffset(String message) {
        return " ".repeat(Math.max(0, (110 - message.length())));
    }

    public String asForeign() {
        StringPrintWriter writer = new StringPrintWriter();
        var partMsg = getFormatted();
        var last = getFormatted();
        for (int i = 0; !last.isEmpty(); i += 30) {
            if (last.length() <= 30) {
                writer.println(last);
                break;
            } else {
                partMsg = last.substring(0, 31);
                last = last.substring(31);
                writer.println(partMsg);
            }
        }
        return writer.toString();
    }

    public String asSelf(boolean isHtml) {
        StringPrintWriter writer = new StringPrintWriter();
        writer.printAsHtml = isHtml;
        var partMsg = getFormatted();
        var last = getFormatted();

        for (int i = 0; !last.isEmpty(); i += 30) {
            if (last.length() <= 30) {
                writer.stylePrintln(Ansi.Colors.YELLOW, Message.getOffset(last) + last);
                break;
            } else {
                partMsg = last.substring(0, 31);
                last = last.substring(31);
                writer.stylePrintln(Ansi.Colors.YELLOW, Message.getOffset(partMsg) + partMsg);
            }
        }
        return writer.toString();
    }

}
