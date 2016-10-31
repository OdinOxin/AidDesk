package de.odinoxin.aiddesk.plugins;

import de.odinoxin.aidcloud.provider.TranslatorProvider;
import de.odinoxin.aiddesk.MainMenu;
import de.odinoxin.aiddesk.plugins.people.PersonEditor;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

public abstract class Plugin extends Stage {
    protected Parent root;

    public Plugin(String res, String title) {
        this.setTitle(TranslatorProvider.getTranslation(title));
        this.getIcons().add(new Image(Plugin.class.getResource("/AidDesk.png").toString()));

        try {
            this.root = FXMLLoader.load(PersonEditor.class.getResource(res));
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        MainMenu.addPlugin(this);
        this.setOnCloseRequest(ev -> {
            MainMenu.removePlugin(this);
        });

        this.setScene(new Scene(this.root));
    }

    public static void setButtonEnter(Button btn) {
        btn.setOnKeyPressed(ev ->
        {
            if (ev.getCode() == KeyCode.ENTER) {
                btn.fire();
                ev.consume();
            }
        });
    }
}
