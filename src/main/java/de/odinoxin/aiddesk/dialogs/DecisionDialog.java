package de.odinoxin.aiddesk.dialogs;

import javafx.scene.Parent;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import javafx.stage.Window;
import de.odinoxin.aiddesk.controls.translateable.Label;
import de.odinoxin.aiddesk.controls.translateable.Button;

public class DecisionDialog {

    public static void showDialog(Window owner, String title, String msg, Callback positve, Callback negative) {
        Stage stage = Dialog.getStage(owner, "decisiondialog", title, msg);
        Parent root = stage.getScene().getRoot();
        Label lblMsg = (Label) root.lookup("#lblMsg");
        lblMsg.setText(msg);
        DecisionDialog.initButton(stage, (Button) root.lookup("#btnPositive"), positve);
        Button btnNegative = (Button) root.lookup("#btnNegative");
        DecisionDialog.initButton(stage, btnNegative, negative);
        btnNegative.requestFocus();
        stage.show();
    }

    private static void initButton(Stage stage, Button btn, Callback callback) {
        if (btn == null)
            return;

        btn.setOnAction(ev ->
        {
            if (stage != null)
                stage.close();
            if (callback != null)
                callback.call();
        });
        btn.setOnKeyPressed(ev ->
        {
            if (ev.getCode() == KeyCode.ENTER) {
                btn.fire();
                ev.consume();
            }
        });
    }
}
