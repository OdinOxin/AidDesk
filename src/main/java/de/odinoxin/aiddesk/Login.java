package de.odinoxin.aiddesk;

import de.odinoxin.aidcloud.provider.LoginProvider;
import de.odinoxin.aidcloud.provider.PersonProvider;
import de.odinoxin.aidcloud.service.LoginService;
import de.odinoxin.aiddesk.controls.refbox.RefBox;
import de.odinoxin.aiddesk.dialogs.MsgDialog;
import de.odinoxin.aiddesk.plugins.Plugin;
import de.odinoxin.aiddesk.plugins.people.Person;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;

import javax.xml.ws.WebServiceException;
import java.net.MalformedURLException;
import java.net.URL;

public class Login extends Plugin {

    private static String serverUrl;
    private static String session;
    private static Person person;

    private TextField txfServer;
    private Button btnConnect;
    private RefBox<Person> refboxUser;
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
                new LoginService(new URL(url + "/Login?wsdl")); //May throw an Exception
                Login.serverUrl = url;
                this.refboxUser.setDisable(false);
                this.pwfPwd.setDisable(false);
                this.btnLogin.setDisable(false);
                this.refboxUser.requestFocus();
            } catch (WebServiceException | MalformedURLException ex) {
                new MsgDialog(this, Alert.AlertType.ERROR, null, ex.getLocalizedMessage()).showAndWait();
            }
        });
        Plugin.setButtonEnter(this.btnConnect);
        this.refboxUser = (RefBox<Person>) this.root.lookup("#refboxUser");
        this.refboxUser.setOnAction(ev -> this.tryLogin());
        this.refboxUser.setProvider(new LoginProvider());
        this.pwfPwd = (PasswordField) this.root.lookup("#pwfPwd");
        this.pwfPwd.setOnAction(ev -> this.tryLogin());
        this.btnLogin = (Button) this.root.lookup("#btnLogin");
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
        Person p = this.refboxUser.getObj();
        if (p == null)
            return;
        if (this.openSession(p.getId(), this.pwfPwd.getText())) {
            this.close();
            new MainMenu();
            return;
        }
        new MsgDialog(this, Alert.AlertType.ERROR, "Login", "User or password incorrect!").showAndWait();
    }

    private boolean openSession(int id, String pwd) {
        Login.session = LoginProvider.getSession(id, pwd);
        if (Login.session != null) {
            Login.person = new Person(id); //Not useless, cause of auth info source; Used to perform next line!
            Login.person = new PersonProvider().get(id);
            Login.person.setPwd(pwd);
            Login.person.setChanged(false);
            return true;
        }
        return false;
    }

    public static String getServerUrl() {
        return Login.serverUrl;
    }

    public static String getSession() {
        return Login.session;
    }

    public static Person getPerson() {
        return Login.person;
    }
}
