package de.odinoxin.aiddesk.dialogs;

import de.odinoxin.aidcloud.provider.TranslatorProvider;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;

import de.odinoxin.aiddesk.controls.translateable.Label;

public abstract class Dialog {

    public static Stage getStage(Window owner, String res, String title, String msg) {
        Stage stage = new Stage();
        stage.setTitle(TranslatorProvider.getTranslation(title));
        stage.getIcons().add(new Image(Dialog.class.getResource("/AidDesk.png").toString()));

        Parent root;
        try {
            root = FXMLLoader.load(Dialog.class.getResource("/dialogs/" + res + ".fxml"));
            Label lblMsg = (Label) root.lookup("#lblMsg");
            lblMsg.setText(msg);
            stage.setScene(new Scene(root));
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(owner);
            return stage;
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return null;
    }

}
