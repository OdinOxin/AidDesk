package de.odinoxin.aiddesk;

import de.odinoxin.aidcloud.mapper.LoginMapper;
import de.odinoxin.aidcloud.mapper.PeopleMapper;
import de.odinoxin.aiddesk.controls.refbox.RefBox;
import de.odinoxin.aiddesk.dialogs.MsgDialog;
import de.odinoxin.aiddesk.plugins.people.Person;
import de.odinoxin.aiddesk.plugins.people.PersonEditor;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class Login extends Application {

    private static Person person;

    private Stage stage;
    private RefBox refboxUser;
    private PasswordField pwfPwd;
    private Button btnLogin;

    public static void main(String[] args) {
        Login.launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.stage = primaryStage;
        this.stage.setTitle("Login");
        this.stage.getIcons().add(new Image(Login.class.getResource("/AidDesk.png").toString()));

        GridPane rootGrid = FXMLLoader.load(this.getClass().getResource("/login.fxml"));
        this.refboxUser = (RefBox) rootGrid.lookup("#refboxUser");
        this.pwfPwd = (PasswordField) rootGrid.lookup("#pwfPwd");
        this.btnLogin = (Button) rootGrid.lookup("#btnLogin");
        this.refboxUser.setOnAction(ev -> this.tryLogin());
        this.pwfPwd.setOnAction(ev -> this.tryLogin());
        this.btnLogin.setOnAction(ev -> this.tryLogin());
        this.btnLogin.setOnKeyPressed(ev ->
        {
            if (ev.getCode() == KeyCode.ENTER)
                this.tryLogin();
        });

        this.stage.setScene(new Scene(rootGrid));
        this.stage.show();
    }

    private void tryLogin() {
        if (LoginMapper.checkLogin(this.refboxUser.getRef(), this.pwfPwd.getText())) {
            Person p = PeopleMapper.get(this.refboxUser.getRef());
            if (p != null) {
                Login.person = p;
                this.stage.close();
                new PersonEditor(Login.person.getId());
                return;
            }
        }
        MsgDialog.showMsg(this.stage, "Login", "User or password incorrect!");
    }

    public static Person getPerson() {
        return Login.person;
    }
}
