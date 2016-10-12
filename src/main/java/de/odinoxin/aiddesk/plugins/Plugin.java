package de.odinoxin.aiddesk.plugins;

import de.odinoxin.aiddesk.controls.translateable.Translator;
import de.odinoxin.aiddesk.plugins.people.PersonEditor;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public abstract class Plugin extends Stage {
    protected VBox root;

    public Plugin(String res, String title) {
        this.setTitle(Translator.getTranslation(title));
        this.getIcons().add(new Image(Plugin.class.getResource("/AidDesk.png").toString()));

        try {
            this.root = FXMLLoader.load(PersonEditor.class.getResource(res));
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        this.setScene(new Scene(this.root));
        this.show();
    }
}
