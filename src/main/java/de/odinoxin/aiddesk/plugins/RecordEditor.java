package de.odinoxin.aiddesk.plugins;

import de.odinoxin.aiddesk.controls.translateable.Button;
import de.odinoxin.aiddesk.dialogs.Callback;
import de.odinoxin.aiddesk.dialogs.DecisionDialog;
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
    private Button btnDelete;

    private ObjectProperty<T> recordItem = new SimpleObjectProperty<T>();
    private T original;

    private Callback saveAction;
    private Callback deleteAction;

    public RecordEditor(String res, String title) {
        super("/plugins/recordeditor.fxml", title);

        try {
            this.grdRecord = FXMLLoader.load(People.class.getResource(res));
            ((ScrollPane) this.root.lookup("#scpDetails")).setContent(this.grdRecord);

            this.btnSave = (Button) this.root.lookup("#btnSave");
            this.btnSave.setOnAction(ev ->
            {
                if (this.saveAction != null)
                    this.saveAction.call();
            });
            this.btnDiscard = (Button) this.root.lookup("#btnDiscard");
            this.btnDiscard.setOnAction(ev -> this.discard());
            this.recordItemProperty().addListener((observable, oldValue, newValue) ->
            {
                this.original = (T) newValue.clone();
                this.btnSave.disableProperty().bind(newValue.changedProperty().not());
                this.btnDiscard.disableProperty().bind(newValue.changedProperty().not());
                this.btnDelete.disableProperty().bind(newValue.idProperty().isEqualTo(0));
            });

            this.btnDelete = (Button) this.root.lookup("#btnDelete");
            this.btnDelete.setOnAction(ev ->
            {
                if (this.deleteAction != null)
                    this.deleteAction.call();
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
                "Möchten Sie diese Änderungen verwerfen?", () -> this.setRecordItem(this.original), null);
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

    public void setSaveAction(Callback saveAction) {
        this.saveAction = saveAction;
    }

    public void setDeleteAction(Callback deleteAction) {
        this.deleteAction = deleteAction;
    }
}
