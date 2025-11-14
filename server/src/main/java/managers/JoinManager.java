package managers;

//import elements.Group;
import elements.GroupClient;
import elements.UserClient;
import requests.JoinRequest;

public class JoinManager extends JoinRequest implements Manager{
    /**
     * @param user - пользователь
     * @param group - группа, в которую вступает пользователь
     */
    private void joinGroup(UserClient user, GroupClient group) {
        group.includeUser(user.getUserId());
    }

    // нужно будет задать значение обоим полям перед вызовом
    UserClient user;
    GroupClient group;

    @Override
    public void applyManager() {
        this.joinGroup(this.user, this.group);
    }

}
