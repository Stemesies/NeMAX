import cli.CustomCommandProcessor;
import elements.Client;
import elements.Group;
import elements.User;
import requests.FriendRequest;

import java.util.Scanner;

@SuppressWarnings("checkstyle:LineLength")
public class ServerCommands {

    public static class ServerContextData {
        public Client client;
        public User user;
        public Group group;

        public ServerContextData(/*Client client, User user, Group group*/) {
//            this.client = client;
//            this.user = user;
//            this.group = group;
        }
    }

    public static final CustomCommandProcessor<ServerContextData> processor = new CustomCommandProcessor<>();

    ServerContextData data = new ServerContextData(/*clientMain, clientMain.user, clientMain.group*/);

    void registerMsg() {
        System.out.println("you are logged out! Please, log in or register your account.");

    }

    FriendRequest friendRequestMsg() {
        var request = new FriendRequest();

        System.out.printf("You received a friend request from user %s\n! Are you want to approve it?",
                data.user.getUserName());
        Scanner responseInput = new Scanner(System.in);
        String resp = "";

        if (responseInput.hasNextLine()) {
            resp = responseInput.nextLine();

            if (resp.equals("y")) {
                request.requested = true;
                request.isResponsed = true;
            } else if (resp.equals("n")) {
                request.requested = false;
                request.isResponsed = true;
            } else {
                request.isResponsed = false;
            }
        }

        responseInput.close();
        return request;
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

    void sendMsgIntoChat() {
        System.out.printf("Your message was send by chat %s\n", data.group.getGroupName());
    }

    void initFriendResponse() {
        var req = friendRequestMsg();
        if (!req.isResponsed)
            throw new Error(); // Ошибка: получена неверная команда.
        var answer = req.requested ? "y" : "n";
        processor.register("friend", (a) -> a
                .description("add user to your friends or not?")
                .requireArgument(answer)
                .subcommand("add", (b) -> b
                        .description("Add user to friends"))
                .subcommand("cancel", (c) -> c
                        .description("cancel friend request"))
                .executes((success) -> {
                    if (success.getString(answer).equals("y")) {
                        success.data.user.addFriend(req.friendId);
                    }
                })
        );
    }
}
