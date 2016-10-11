package de.odinoxin.aiddesk.dialogs;

import de.odinoxin.aiddesk.controls.translateable.Label;
import de.odinoxin.aiddesk.controls.translateable.Translator;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;

public abstract class Dialog {

    public static Stage getStage(Window owner, String title, String res, String msg) {
        Stage stage = new Stage();
        if (title != null) {
            String translation = Translator.getTranslation(title);
            if (translation != null)
                stage.setTitle(translation);
        } else
            stage.setTitle(title);
        stage.getIcons().add(new Image(MsgDialog.class.getResource("/AidDesk.png").toString()));

        Parent root = null;
        try {
            root = FXMLLoader.load(MsgDialog.class.getResource("/dialogs/" + res + ".fxml"));
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
