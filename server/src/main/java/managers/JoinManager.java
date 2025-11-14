package managers;

//import elements.Group;
import elements.GroupClient;
import elements.UserClient;
import requests.JoinRequest;

public class JoinManager extends JoinRequest {
    /**
     * @param user - пользователь
     * @param group - группа, в которую вступает пользователь
     */
    public void joinGroup(UserClient user, GroupClient group) {
        group.includeUser(user.getUserId());
    }
}
