package de.odinoxin.aiddesk.plugins;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public abstract class Plugin extends Stage
{
    protected GridPane mainGrid;

    public Plugin(String res, String title)
    {
        this.setTitle(title);

        try
        {
            this.mainGrid = FXMLLoader.load(Humans.class.getResource(res));
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

        this.setScene(new Scene(this.mainGrid));
        this.show();
    }
}
