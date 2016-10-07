package de.odinoxin.aiddesk.plugins;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public abstract class Plugin extends Stage
{
    protected GridPane grdMain;

    public Plugin(String res, String title)
    {
        this.setTitle(title);
        this.getIcons().add(new Image(Plugin.class.getResource("/AidDesk.png").toString()));

        try
        {
            this.grdMain = FXMLLoader.load(Humans.class.getResource(res));
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

        this.setScene(new Scene(this.grdMain));
        this.show();
    }
}
