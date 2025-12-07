import cli.CustomCommandProcessor;
import elements.Client;
import elements.ServerConnectManager;

@SuppressWarnings("checkstyle:LineLength")
public class ServerCommands {

    public static class ServerContextData {

    }

    public static final CustomCommandProcessor<ServerContextData> processor = new CustomCommandProcessor<>();

    private static void registerMsg() {
        System.out.println("you are logged out! Please, log in or register your account.");
    }

    void friendAddMsg() {
        System.out.println("Your friend request is approved!");
    }

    private static void newMessageMsg() {
        System.out.println("You have new massage!");
    }

    private static void initFriendResponse() {
        processor.register("friends", (a) -> a
                .description("Add user to your friends or not?")
                .subcommand("add", (b) -> b
                        .description("Add user to friends")
                        .executes((success) -> {
                            System.out.println(success.getString("argumentName"));
                        }))
                .requireArgument("argumentName")
        );
        processor.register("friends", (a) -> a
                .description("Delete friend")
                .subcommand("del", (b) -> b
                        .executes((deletion) -> {
                            System.out.println(deletion.getString("argument"));
                        })
                ).requireArgument("argument")
        );

    }

    private static void initRegisterResponse() {
        processor.register("register", (a) -> a
                .description("Register message")
                .subcommand("request", (b) -> b
                        .executes(ServerCommands::registerMsg))
        );
    }

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
                            }
                        })
                        .requireArgument("groupId")
                        .requireArgument("message")
                )
        );
        processor.register("chat", (a) -> a
                .description("User open chat with unread messages.")
                .subcommand("read", (b) -> b
                        .executes((msg) -> {
                            Client.readMessage(msg.getString("groupId"));
                        })
                        .requireArgument("groupId")
                )
        );
    }

    public static void initGeneral() {
        initFriendResponse();
        initRegisterResponse();
        initGroupResponse();
    }
}
