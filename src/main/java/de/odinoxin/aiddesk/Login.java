package de.odinoxin.aiddesk;

import de.odinoxin.aidcloud.service.LoginService;
import de.odinoxin.aiddesk.dialogs.Dialog;
import de.odinoxin.aiddesk.plugins.Plugin;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.application.Application;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.GridPane;
import de.odinoxin.aidcloud.mapper.LoginMapper;
import de.odinoxin.aidcloud.mapper.PeopleMapper;
import de.odinoxin.aiddesk.controls.refbox.RefBox;
import de.odinoxin.aiddesk.dialogs.MsgDialog;
import de.odinoxin.aiddesk.plugins.people.Person;
import de.odinoxin.aiddesk.plugins.people.PersonEditor;

import javax.xml.ws.WebServiceException;
import java.net.MalformedURLException;
import java.net.URL;

public class Login extends Application {

    private static String serverUrl;
    private static Person person;

    private Stage stage;
    private TextField txfServer;
    private Button btnConnect;
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

        GridPane root = FXMLLoader.load(this.getClass().getResource("/login.fxml"));
        this.txfServer = (TextField) root.lookup("#txfServer");
        this.txfServer.textProperty().addListener((observable, oldValue, newValue) -> {
            this.refboxUser.setDisable(true);
            this.pwfPwd.setDisable(true);
            this.btnLogin.setDisable(true);
        });
        this.txfServer.setOnAction(ev -> this.btnConnect.fire());
        this.btnConnect = (Button) root.lookup("#btnConnect");
        this.btnConnect.setOnAction(ev -> {
            try {
                String url = this.txfServer.getText();
                LoginService loginSvc = new LoginService(new URL(url + "/Login?wsdl"));
                Login.serverUrl = url;
                this.refboxUser.setDisable(false);
                this.refboxUser.setName("Login");
                this.pwfPwd.setDisable(false);
                this.btnLogin.setDisable(false);
                this.refboxUser.requestFocus();
            } catch (WebServiceException | MalformedURLException ex) {
                MsgDialog.showMsg(this.stage, "Error!", ex.getLocalizedMessage());
            }
        });
        Plugin.setButtonEnter(this.btnConnect);
        this.refboxUser = (RefBox) root.lookup("#refboxUser");
        this.pwfPwd = (PasswordField) root.lookup("#pwfPwd");
        this.btnLogin = (Button) root.lookup("#btnLogin");
        this.refboxUser.setOnAction(ev -> this.tryLogin());
        this.pwfPwd.setOnAction(ev -> this.tryLogin());
        this.btnLogin.setOnAction(ev -> this.tryLogin());
        this.btnLogin.setOnKeyPressed(ev -> {
            if (ev.getCode() == KeyCode.ENTER)
                this.tryLogin();
        });
        Plugin.setButtonEnter(this.btnLogin);

        this.stage.setScene(new Scene(root));
        this.stage.show();

        this.txfServer.setText("http://localhost:15123/AidCloud");
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

    public static String getServerUrl() {
        return Login.serverUrl;
    }

    public static Person getPerson() {
        return Login.person;
    }
}
