package de.odinoxin.aiddesk.plugins;

import de.odinoxin.aiddesk.controls.translateable.Translator;
import de.odinoxin.aiddesk.plugins.people.People;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public abstract class Plugin extends Stage {
    protected VBox root;

    public Plugin(String res, String title) {
        if (title != null) {
            String translation = Translator.getTranslation(title);
            if (translation != null)
                this.setTitle(translation);
            else
                this.setTitle(title);
        }
        this.setTitle(title);
        this.getIcons().add(new Image(Plugin.class.getResource("/AidDesk.png").toString()));

        try {
            this.root = FXMLLoader.load(People.class.getResource(res));
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        this.setScene(new Scene(this.root));
        this.show();
    }
}
