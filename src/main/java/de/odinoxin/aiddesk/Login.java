package de.odinoxin.aiddesk;

import de.odinoxin.aidcloud.mapper.LoginMapper;
import de.odinoxin.aidcloud.mapper.PeopleMapper;
import de.odinoxin.aidcloud.service.LoginService;
import de.odinoxin.aiddesk.controls.refbox.RefBox;
import de.odinoxin.aiddesk.dialogs.MsgDialog;
import de.odinoxin.aiddesk.plugins.Plugin;
import de.odinoxin.aiddesk.plugins.people.Person;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;

import javax.xml.ws.WebServiceException;
import java.net.MalformedURLException;
import java.net.URL;

public class Login extends Plugin {

    private static String serverUrl;
    private static Person person;

    private TextField txfServer;
    private Button btnConnect;
    private RefBox refboxUser;
    private PasswordField pwfPwd;
    private Button btnLogin;

    public Login() {
        super("/login.fxml", "Login");

        this.txfServer = (TextField) this.root.lookup("#txfServer");
        this.txfServer.textProperty().addListener((observable, oldValue, newValue) -> {
            this.refboxUser.setDisable(true);
            this.pwfPwd.setDisable(true);
            this.btnLogin.setDisable(true);
        });
        this.txfServer.setOnAction(ev -> this.btnConnect.fire());
        this.btnConnect = (Button) this.root.lookup("#btnConnect");
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
                MsgDialog.showMsg(this, "Error!", ex.getLocalizedMessage());
            }
        });
        Plugin.setButtonEnter(this.btnConnect);
        this.refboxUser = (RefBox) this.root.lookup("#refboxUser");
        this.pwfPwd = (PasswordField) this.root.lookup("#pwfPwd");
        this.btnLogin = (Button) this.root.lookup("#btnLogin");
        this.refboxUser.setOnAction(ev -> this.tryLogin());
        this.pwfPwd.setOnAction(ev -> this.tryLogin());
        this.btnLogin.setOnAction(ev -> this.tryLogin());
        this.btnLogin.setOnKeyPressed(ev -> {
            if (ev.getCode() == KeyCode.ENTER)
                this.tryLogin();
        });
        Plugin.setButtonEnter(this.btnLogin);

        this.txfServer.setText("http://localhost:15123/AidCloud");
        this.show();
    }

    private void tryLogin() {
        if (LoginMapper.checkLogin(this.refboxUser.getRef(), this.pwfPwd.getText())) {
            Person p = PeopleMapper.get(this.refboxUser.getRef());
            if (p != null) {
                Login.person = p;
                this.close();
                new MainMenu();
                return;
            }
        }
        MsgDialog.showMsg(this, "Login", "User or password incorrect!");
    }

    public static String getServerUrl() {
        return Login.serverUrl;
    }

    public static Person getPerson() {
        return Login.person;
    }
}
