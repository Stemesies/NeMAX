package elements;

import managers.FriendManager;
import managers.JoinManager;
import managers.RegisterManager;

import java.util.ArrayList;

public class User extends AbstractUser {

    ArrayList<? extends SuperRequest> requests;

    @Override
    public void sendMessage(String text, int id) {
        // отправляет text второму пользователю id?
    }

    public void activateManager() {
        while (!this.requests.isEmpty()) {
            SuperRequest r = requests.getFirst();
//            Manager manager = new Manager();
            switch(r.type) {
                case Register:
                    RegisterManager manager1 = new RegisterManager();
                    /*что-нибудь про регистрацию*/
                    break;
                case Join:
                    JoinManager manager2 = new JoinManager();
                    // достаём пользователя и группу из базы данных по id, инициилизируем поля класса
                    manager2.applyManager();
                    break;
                case Friend:
                    FriendManager manager3 = new FriendManager();
                    // достаём пользователей из базы данных по id, инициилизируем поля класса
                    // response = true, пока мы не продумали ответ второго пользователя
                    manager3.applyManager();
                    break;
                default: return;
            }
        }
    }

}
