package de.odinoxin.aiddesk.controls.refbox;

import de.odinoxin.aiddesk.plugins.RecordItem;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.control.ListView;
import javafx.stage.Popup;

import java.io.IOException;

/**
 * Suggestion list as {@link Popup} below the {@link RefBox}.
 * @param <T> The type of the records.
 */
class RefBoxList<T extends RecordItem> extends Popup {

    private ListView<RefBoxListItem<T>> lvSuggestions;

    RefBoxList(Point2D pos) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(RefBoxList.class.getResource("/controls/refboxlist.fxml"));
            this.lvSuggestions = fxmlLoader.load();
            this.lvSuggestions.setStyle("-fx-selection-bar: #039ED3");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        if (pos != null) {
            setX(pos.getX());
            setY(pos.getY());
        }
        this.setAutoHide(true);
        this.getContent().add(this.lvSuggestions);
    }

    ListView<RefBoxListItem<T>> getSuggestionsList() {
        return this.lvSuggestions;
    }

    void setPrefWidth(double prefWidth) {
        super.setWidth(prefWidth);
        lvSuggestions.setPrefWidth(prefWidth);
    }
}
