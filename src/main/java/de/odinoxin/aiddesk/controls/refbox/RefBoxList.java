package de.odinoxin.aiddesk.controls.refbox;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.control.ListView;
import javafx.stage.Popup;

import java.io.IOException;

public class RefBoxList extends Popup {

    private ListView<RefBoxListItem> lvSuggestions;

    public RefBoxList(Point2D pos) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(RefBoxList.class.getResource("/controls/refboxlist.fxml"));
            this.lvSuggestions = fxmlLoader.load();
            this.lvSuggestions.setStyle("-fx-selection-bar: #039ED3");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        setX(pos.getX());
        setY(pos.getY());
        setAutoHide(true);
        getContent().add(this.lvSuggestions);
    }

    public ListView<RefBoxListItem> getSuggestionsList() {
        return this.lvSuggestions;
    }

    public void setPrefWidth(double prefWidth) {
        super.setWidth(prefWidth);
        lvSuggestions.setPrefWidth(prefWidth);
    }
}
