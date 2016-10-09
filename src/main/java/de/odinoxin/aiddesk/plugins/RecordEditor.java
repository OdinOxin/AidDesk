package de.odinoxin.aiddesk.plugins;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;

public class RecordEditor extends Plugin {

    private ScrollPane scpDetails;
    private GridPane grdRecord;

    public RecordEditor(String res, String title) {
        super("/plugins/recordeditor.fxml", title);

        try {
            this.grdRecord = FXMLLoader.load(Humans.class.getResource(res));
            this.scpDetails = (ScrollPane) this.root.lookup("#scpDetails");
            this.scpDetails.setContent(this.grdRecord);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
