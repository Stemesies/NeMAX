import cli.CustomCommandProcessor;
import elements.Client;
import elements.Group;
import elements.User;
import network.SimpleSocket;
import requests.FriendRequest;

@SuppressWarnings("checkstyle:LineLength")
public class ServerCommands {

    public static class ServerContextData {
        public Client client;
        public User user;
        public Group group;
        public FriendRequest request;
        public SimpleSocket socket;

        public ServerContextData(Client client, User user, Group group, FriendRequest request, SimpleSocket socket) {
            this.client = client;
            this.user = user;
            this.group = group;
            this.request = request;
            this.socket = socket;
        }
    }

    public static final CustomCommandProcessor<ServerContextData> processor = new CustomCommandProcessor<>();

    private static void registerMsg() {
        System.out.println("you are logged out! Please, log in or register your account.");
    }

    void friendAddMsg() {
        System.out.println("Your friend request is approved!");
    }

    void friendCancelMsg() {
        System.out.println("Your friend request is cancel.");
    }

    void deleteFriendMsg() {
        System.out.println("User has deleted from your friends.");
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
                            c.data.socket.sendMessage("/response chat " + c.data.group.getIdGroup());
                        })
                )
        );
        processor.register("chat", (a) -> a
                .description("Add new message to unread.")
                .subcommand("new", (b) -> b
                        .executes((msg) -> {
                            if (!msg.data.group.getGroupName().equals(msg.getString("groupId"))) {
                                msg.data.client.addUnreadMsg(msg.getString("groupId"), msg.getString("message"));
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
                            msg.data.client.readMessage(msg.getString("groupId"));
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
