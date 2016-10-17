package de.odinoxin.aiddesk.plugins.people;

import javafx.scene.control.Alert;
import javafx.stage.Modality;
import javafx.scene.control.PasswordField;
import de.odinoxin.aidcloud.mapper.LoginMapper;
import de.odinoxin.aiddesk.dialogs.MsgDialog;
import de.odinoxin.aiddesk.plugins.Plugin;
import de.odinoxin.aiddesk.controls.translateable.Button;

public class PwdEditor extends Plugin {

    private PasswordField pwfCurrent;
    private PasswordField pwfNew;
    private PasswordField pwfRepeat;
    private Button btnOK;
    private Button btnCancel;

    public PwdEditor(PersonEditor personEditor) {
        super("/plugins/passwordeditor.fxml", "Passwort");

        this.btnOK = (Button) this.root.lookup("#btnOK");
        Plugin.setButtonEnter(this.btnOK);
        this.btnOK.setOnAction(ev -> {
            this.pwfCurrent = (PasswordField) this.root.lookup("#pwfCurrent");
            this.pwfNew = (PasswordField) this.root.lookup("#pwfNew");
            this.pwfRepeat = (PasswordField) this.root.lookup("#pwfRepeat");
            if (this.pwfNew.getText().isEmpty() || this.pwfRepeat.getText().isEmpty() || !this.pwfNew.getText().equals(this.pwfRepeat.getText())) {
                new MsgDialog(this, Alert.AlertType.ERROR, "Ungültige Eingabe!", "Geben Sie das neue Passwort ein und wiederholen Sie dieses korrekt.").showAndWait();
                return;
            }
            if (!LoginMapper.checkLogin(personEditor.recordId().get(), this.pwfCurrent.getText())) {
                new MsgDialog(this, Alert.AlertType.ERROR, "Ungültige Eingabe!", "Geben Sie das aktuelle Passwort an, um ein neues Passwort zu speichern.").showAndWait();
                return;
            }
            personEditor.changePwd(this.pwfCurrent.getText(), this.pwfNew.getText());
            this.close();
        });
        this.btnCancel = (Button) this.root.lookup("#btnCancel");
        Plugin.setButtonEnter(this.btnCancel);
        this.btnCancel.setOnAction(ev -> this.close());

        this.initModality(Modality.WINDOW_MODAL);
        this.initOwner(personEditor);
        this.show();
    }
}
