package de.odinoxin.aiddesk.dialogs;

import javafx.scene.Parent;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import javafx.stage.Window;
import de.odinoxin.aiddesk.controls.translateable.Label;
import de.odinoxin.aiddesk.controls.translateable.Button;

public class DecisionDialog {

    public static void showDialog(Window owner, String title, String msg, Callback positve, Callback negative) {
        Stage stage = Dialog.getStage(owner, title, "decisiondialog", msg);

        Parent root = stage.getScene().getRoot();
        Label lblMsg = (Label) root.lookup("#lblMsg");
        lblMsg.setText(msg);
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
