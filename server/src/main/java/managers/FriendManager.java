package managers;

import elements.UserClient;
import requests.FriendRequest;
//import client.elements.UserClient;

public class FriendManager extends FriendRequest {
    /**
    * @param user_1 - кого принимают в друзья
    * @param user_2 - кто принимает в друзья
     */
    public void addToFriends(UserClient user_1, UserClient user_2) {
        user_2.getFriends().add(user_1.getUserId());
    }
}
