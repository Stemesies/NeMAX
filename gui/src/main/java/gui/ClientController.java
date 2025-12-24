package gui;

import client.ServerCommands;
import client.elements.Client;
import client.elements.ServerConnectManager;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.CompletableFuture;

public class ClientController implements Initializable {

    @FXML
    private Label titleText;

    @FXML
    private Label welcomeText;

    @FXML
    private TextField tf;

    @FXML
    private TextArea receivedMsg;

    @FXML
    private Button serverButton;

    public void setInput(String input) {

        if (tf != null) {
            System.out.println("input: " + input);
            Client.inputManager.processInput(input);
            tf.setText(null);
        }

    }

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Добро пожаловать в мессенджер NeMax!");
    }

    @FXML
    public void showServerResult() {
        CompletableFuture.supplyAsync(() -> {
            Client.launch();
            return null;
        });
    }

    public static String getMsg() {
        return MSG.get();
    }

    public static void setMsg(String msg) {
        MSG.set(msg);
    }

    public static StringProperty msgProperty() {
        return MSG;
    }

    private static final StringProperty MSG = new SimpleStringProperty("");

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ServerCommands.initGeneral();
//        System.out.println("text: " + tf.getText());
//        System.out.println("label: " + welcomeText.getText());

        // Если поле только для отображения (не может изменять значение MSG)
        receivedMsg.textProperty().bind(msgProperty());
        // Если через это поле можно менять значение MSG
//        tf.textProperty().bindBidirectional(MSGProperty());
        ServerConnectManager.addOutPutListener(ClientController::setMsg);
//        titleText.setText("NeMax");
    }

    @FXML
    private Button sendBtn;

    @FXML
    public void onSend() {
        setInput(tf.getText());
    }

    @FXML
    private Button register;

    @FXML
    private Button groupCreate;

    @FXML
    public void setRegister() {
        setInput("/register " + tf.getText());
    }

    @FXML
    public void setGroup() {
        setInput("/groups create " + tf.getText());
    }

    @FXML
    private Button openChatBtn;

    @FXML
    public void openChat() {
        setInput("/open " + tf.getText());
    }
}