package de.odinoxin.aiddesk.dialogs;

import javafx.scene.Parent;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import javafx.stage.Window;
import de.odinoxin.aiddesk.controls.translateable.Label;
import de.odinoxin.aiddesk.controls.translateable.Button;

public class MsgDialog {
    public static void showMsg(Window owner, String title, String msg) {
        MsgDialog.showMsg(owner, title, msg, 0, 0);
    }

    public static void showMsg(Window owner, String title, String msg, int titleId, int msgId) {
        Stage stage = Dialog.getStage(owner, title, titleId, "msgdialog", msg, msgId);

        Parent root = stage.getScene().getRoot();
        Label lblMsg = (Label) root.lookup("#lblMsg");
        lblMsg.setText(msg);
        lblMsg.setTextId(msgId);

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
