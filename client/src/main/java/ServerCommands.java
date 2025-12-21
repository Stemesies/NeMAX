import cli.CommandProcessor;
import elements.Client;
import elements.ServerConnectManager;

@SuppressWarnings("checkstyle:LineLength")
public class ServerCommands {

    public static final CommandProcessor processor = new CommandProcessor();

    /**
     * Сообщение о том, что нужно войти в систему.
     */
    private static void registerMsg() {
        System.out.println("you are logged out! Please, log in or register your account.");
    }

    private void friendAddMsg() {
        System.out.println("Your friend request is approved!");
    }

    /**
     * Уведомление о новом сообщении группы или чата,
     * который не открыт на данный момент.
     */
    private static void newMessageMsg() {
        System.out.println("You have new massage!");
    }

    /**
     * Команды по запросу в друзья, отклонению заявки и пр.
     */
    private static void initFriendResponse() {
        processor.register("friends", (a) -> a
                .description("Friend request")
                .subcommand("add", (b) -> b
                        .description("Add user to friends")
                        .executes((success) -> {
                            System.out.println("You receive a friend request from user "
                                    + success.getString("argument"));
                        }))
                .requireArgument("argument")
        );
        processor.register("friends", (a) -> a
                .description("Delete friend")
                .subcommand("del", (b) -> b
                        .executes((deletion) -> {
                            System.out.println("User " + deletion.getString("argument")
                                    + "deleted from your friends.");
                        })
                ).requireArgument("argument")
        );

    }

    /**
     * Команды регистрации пользователя.
     */
    private static void initRegisterResponse() {
        processor.register("register", (a) -> a
                .description("Register message")
                .subcommand("request", (b) -> b
                        .executes(ServerCommands::registerMsg))
        );
    }

    /**
     * Команды по отправке уведомлений о непрочитанных сообщениях
     * и передаче данных об открытом чате на сервер.
     */
    private static void initGroupResponse() {
        processor.register("chat", (a) -> a
                .description("Send id of open chat.")
                .subcommand("fetch", (b) -> b
                        .executes((c) -> {
                            ServerConnectManager.socket.sendMessage("/response chat " + Client.openChatId);
                        })
                )
        );
        processor.register("chat", (a) -> a
                .description("Add new message to unread.")
                .subcommand("new", (b) -> b
                        .executes((msg) -> {
                            if (!Client.openChatId.equals(msg.getString("groupId"))) {
                                Client.addUnreadMsg(msg.getString("groupId"), msg.getString("message"));
                                newMessageMsg();
                            } else {
                                System.out.println(msg.getString("message"));
                                Client.readMessage(msg.getString("groupId"));
                            }
                        })
                        .requireArgument("groupId")
                        .requireArgument("message")
                )
        );
        processor.register("chat", (a) -> a
                .subcommand("open", (b) -> {})
                .requireArgument("listMessages")
                .executes((msg) -> {
                    System.out.println(msg.getString("ListMessages"));
                })
        );
    }

    /**
     * Регистрация команд сервера на клиенте.
     */
    public static void initGeneral() {
        initFriendResponse();
        initRegisterResponse();
        initGroupResponse();
    }
}
