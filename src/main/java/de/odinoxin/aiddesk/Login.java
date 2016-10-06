package de.odinoxin.aiddesk;

import de.odinoxin.aiddesk.controls.RefBox;
import de.odinoxin.aiddesk.dialogs.MsgDialog;
import de.odinoxin.aiddesk.plugins.Humans;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Login extends Application {

    public static int HumanID;

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

        this.stage.setScene(new Scene(rootGrid));
        this.stage.show();
    }

    private void tryLogin() {
        try {
            PreparedStatement statement = Database.DB.prepareStatement("SELECT 'OK' FROM Humans WHERE ID = ? AND Pwd = ?");
            statement.setInt(1, this.refboxUser.getRef());
            statement.setString(2, this.pwfPwd.getText());
            ResultSet dbRes = statement.executeQuery();
            if (dbRes.next() && dbRes.getString(1).equals("OK")) {
                this.stage.close();
                Login.HumanID = this.refboxUser.getRef();
                new Humans();
            } else
                MsgDialog.showMsg(this.stage, "Login", "User or password incorrect!");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
