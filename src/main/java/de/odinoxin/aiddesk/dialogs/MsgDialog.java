package de.odinoxin.aiddesk.dialogs;

import javafx.scene.Parent;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import javafx.stage.Window;
import de.odinoxin.aiddesk.controls.translateable.Label;
import de.odinoxin.aiddesk.controls.translateable.Button;

public class MsgDialog {
    public static void showMsg(Window owner, String title, String msg) {
        Stage stage = Dialog.getStage(owner, "msgdialog", title, msg);

        Parent root = stage.getScene().getRoot();
        Label lblMsg = (Label) root.lookup("#lblMsg");
        lblMsg.setText(msg);

        Button btnOK = (Button) root.lookup("#btnOK");
        btnOK.setOnAction(ev -> stage.close());
        btnOK.setOnKeyPressed(ev ->
        {
            if (ev.getCode() == KeyCode.ENTER)
                stage.close();
        });
        stage.show();
    }
}
