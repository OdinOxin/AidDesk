package de.odinoxin.aiddesk.plugins;

import de.odinoxin.aidcloud.provider.TranslatorProvider;
import de.odinoxin.aiddesk.plugins.people.PersonEditor;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

/**
 * Base class for plugins.
 * Initializes stage with title, icon, scene.
 * Tracks running plugins.
 */
public abstract class Plugin extends Stage {
    protected Parent root;

    /**
     * Currently running runningPlugins
     */
    private static List<Plugin> runningPlugins = new ArrayList<>();

    public Plugin(String res, String title) {
        this.setTitle(TranslatorProvider.getTranslation(title));
        this.getIcons().add(new Image(Plugin.class.getResource("/AidDesk.png").toString()));

        try {
            this.root = FXMLLoader.load(PersonEditor.class.getResource(res));
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        Plugin.runningPlugins.add(this);
        this.setOnCloseRequest(ev -> {
            Plugin.runningPlugins.remove(this);
        });

        this.setScene(new Scene(this.root));
    }

    /**
     * Adds ENTER as click action to the given button.
     *
     * @param btn The button to manipulate.
     */
    public static void setButtonEnter(Button btn) {
        btn.setOnKeyPressed(ev ->
        {
            if (ev.getCode() == KeyCode.ENTER) {
                btn.fire();
                ev.consume();
            }
        });
    }

    public static List<Plugin> getRunningPlugins() {
        return Plugin.runningPlugins;
    }
}
