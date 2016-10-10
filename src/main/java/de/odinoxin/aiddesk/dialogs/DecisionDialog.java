package de.odinoxin.aiddesk.dialogs;

import javafx.scene.Parent;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import javafx.stage.Window;
import de.odinoxin.aiddesk.controls.translateable.Label;
import de.odinoxin.aiddesk.controls.translateable.Button;

public class DecisionDialog {

    public static void showDialog(Window owner, String title, String msg, Callback positve, Callback negative) {
        DecisionDialog.showDialog(owner, title, msg, positve, negative, 0, 0);
    }

    public static void showDialog(Window owner, String title, String msg, Callback positve, Callback negative, int titleId, int msgId) {
        Stage stage = Dialog.getStage(owner, title, titleId, "decisiondialog", msg, msgId);

        Parent root = stage.getScene().getRoot();
        Label lblMsg = (Label) root.lookup("#lblMsg");
        lblMsg.setText(msg);
        lblMsg.setTextId(msgId);
        Button btnPositive = (Button) root.lookup("#btnPositive");
        btnPositive.setOnAction(ev ->
        {
            stage.close();
            if (positve != null)
                positve.call();
        });
        btnPositive.setOnKeyPressed(ev ->
        {
            if (ev.getCode() == KeyCode.ENTER) {
                btnPositive.fire();
                ev.consume();
            }
        });
        Button btnNegative = (Button) root.lookup("#btnNegative");
        btnNegative.setOnAction(ev ->
        {
            stage.close();
            if (negative != null)
                negative.call();
        });
        btnNegative.setOnKeyPressed(ev ->
        {
            if (ev.getCode() == KeyCode.ENTER) {
                btnNegative.fire();
                ev.consume();
            }
        });
        stage.show();
    }
}
