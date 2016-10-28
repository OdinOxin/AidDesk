package de.odinoxin.aiddesk.dialogs;

import de.odinoxin.aidcloud.provider.TranslatorProvider;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Modality;
import javafx.stage.Window;

public class MsgDialog extends Alert {

    public MsgDialog(Window owner, AlertType type, String title, String msg) {
        super(type, TranslatorProvider.getTranslation(msg));

        if (title != null)
            this.setHeaderText(TranslatorProvider.getTranslation(title));

        javafx.scene.control.Button btnOK = (javafx.scene.control.Button) this.getDialogPane().lookupButton(ButtonType.OK);
        btnOK.setDefaultButton(true);

        this.initModality(Modality.APPLICATION_MODAL);
        this.initOwner(owner);
    }
}
