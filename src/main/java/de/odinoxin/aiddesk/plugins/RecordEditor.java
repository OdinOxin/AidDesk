package de.odinoxin.aiddesk.plugins;

import de.odinoxin.aiddesk.controls.translateable.Button;
import de.odinoxin.aiddesk.dialogs.Callback;
import de.odinoxin.aiddesk.dialogs.DecisionDialog;
import de.odinoxin.aiddesk.dialogs.MsgDialog;
import de.odinoxin.aiddesk.plugins.people.People;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;

public class RecordEditor<T extends RecordItem> extends Plugin {

    private GridPane grdRecord;
    private Button btnSave;
    private Button btnDiscard;

    private ObjectProperty<T> recordItem = new SimpleObjectProperty<T>();
    private T original;

    public RecordEditor(String res, String title, int titleId) {
        super("/plugins/recordeditor.fxml", title, titleId);

        try {
            this.grdRecord = FXMLLoader.load(People.class.getResource(res));
            ((ScrollPane) this.root.lookup("#scpDetails")).setContent(this.grdRecord);

            this.btnSave = (Button) this.root.lookup("#btnSave");
            this.btnDiscard = (Button) this.root.lookup("#btnDiscard");
            this.btnDiscard.setOnAction(ev -> this.discard());
            this.recordItemProperty().addListener((observable, oldValue, newValue) ->
            {
                this.original = (T) newValue.clone();
                this.btnSave.disableProperty().bind(newValue.changedProperty().not());
                this.btnDiscard.disableProperty().bind(newValue.changedProperty().not());
            });
            this.sizeToScene();
            this.centerOnScreen();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void discard() {
        DecisionDialog.showDialog(this, "Änderugen verwerfen?", "Es sind noch nicht gespeicherte Änderungen vorhanden!\n" +
                "\n" +
                "Möchten Sie diese Änderungen verwerfen?", () -> this.setRecordItem(this.original), null, 17, 18);
    }

    public T getRecordItem() {
        return recordItem.get();
    }

    public void setRecordItem(T recordItem) {
        this.recordItem.set(recordItem);
    }

    public ObjectProperty<T> recordItemProperty() {
        return recordItem;
    }
}
