package de.odinoxin.aiddesk.dialogs;

import de.odinoxin.aidcloud.provider.TranslatorProvider;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.stage.Modality;
import javafx.stage.Window;

public class DecisionDialog extends Alert {

    public DecisionDialog(Window owner, String title, String msg) {
        super(AlertType.CONFIRMATION, TranslatorProvider.getTranslation(msg));

        this.setHeaderText(TranslatorProvider.getTranslation(title));

        Button btnOK = (Button) this.getDialogPane().lookupButton(ButtonType.OK);
        btnOK.setDefaultButton(false);

        Button btnCancel = (Button) this.getDialogPane().lookupButton(ButtonType.CANCEL);
        btnCancel.setDefaultButton(true);
        Platform.runLater(() -> btnCancel.requestFocus());

        this.initModality(Modality.APPLICATION_MODAL);
        this.initOwner(owner);
    }
}
