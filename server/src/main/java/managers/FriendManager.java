package managers;

import elements.UserClient;
import requests.FriendRequest;
//import client.elements.UserClient;

public class FriendManager extends FriendRequest implements Manager{
    /**
    * @param user_1 - кого принимают в друзья
    * @param user_2 - кто принимает в друзья
     * @param response - ответ пользователя, которому отправили завявку (принял/отклонил)
     */
    private void addToFriends(UserClient user_1, UserClient user_2, boolean response) {
        if (response)
            user_2.getFriends().add(user_1.getUserId());
    }

    // нужно будет определить все 3 поля перед вызовом метода
    UserClient user_1;
    UserClient user_2;
    boolean response;

    @Override
    public void applyManager() {
        this.addToFriends(this.user_1, this.user_2, this.response);
    }
}
