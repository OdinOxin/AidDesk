package de.odinoxin.aiddesk.plugins;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import de.odinoxin.aiddesk.dialogs.Callback;
import de.odinoxin.aiddesk.dialogs.DecisionDialog;
import de.odinoxin.aiddesk.controls.refbox.RefBox;
import de.odinoxin.aiddesk.controls.translateable.Button;
import de.odinoxin.aiddesk.controls.translateable.Translator;

public abstract class RecordEditor<T extends RecordItem> extends Plugin {

    private TextField txfId;
    private RefBox refBoxKey;
    private GridPane grdRecord;
    private Button btnSave;
    private Button btnDiscard;
    private Button btnDelete;

    private ObjectProperty<T> recordItem = new SimpleObjectProperty<T>();
    private T original;

    private Callback newAction;
    private Callback saveAction;
    private Callback deleteAction;

    public RecordEditor(String res, String title) {
        super("/plugins/recordeditor.fxml", title);

        try {
            this.grdRecord = FXMLLoader.load(RecordEditor.class.getResource(res));

            this.refBoxKey = (RefBox) this.root.lookup("#refBoxKey");
            this.refBoxKey.setView(this.getRefBoxKeyView());
            this.refBoxKey.setOnNewAction(ev ->
            {
                this.loadRecord(0);
                this.refBoxKey.setRef(0);
                if (this.newAction != null)
                    this.newAction.call();
            });
            this.refBoxKey.refProperty().addListener((observable, oldValue, newValue) -> this.loadRecord((int) newValue));
            this.txfId = (TextField) this.root.lookup("#txfId");
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
                if (newValue == null) {
                    this.original = null;
                    this.txfId.setText("");
                    this.btnSave.setDisable(true);
                    this.btnDiscard.setDisable(true);
                    this.btnDelete.setDisable(true);
                } else {
                    this.original = (T) newValue.clone();
                    this.txfId.setText(newValue.getId() == 0 ? Translator.getTranslation("Neu") : String.valueOf(newValue.getId()));
                    this.btnSave.disableProperty().bind(newValue.changedProperty().not());
                    this.btnDiscard.disableProperty().bind(newValue.changedProperty().not());
                    this.btnDelete.disableProperty().bind(newValue.idProperty().isEqualTo(0));
                }
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
        if (this.getRecordItem() != null)
            this.loadRecord(this.getRecordItem().getId());
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

    public void setNewAction(Callback newAction) {
        this.newAction = newAction;
    }

    public void setSaveAction(Callback saveAction) {
        this.saveAction = saveAction;
    }

    public void setDeleteAction(Callback deleteAction) {
        this.deleteAction = deleteAction;
    }

    protected void loadRecord(int id) {
        if (this.getRecordItem() != null && this.getRecordItem().isChanged()) {
            DecisionDialog.showDialog(this, "Änderugen verwerfen?", "Möchten Sie die aktuellen Änderungen verwerfen?", () -> this.setRecord(id), null);
        } else this.setRecord(id);
    }

    protected abstract void setRecord(int id);

    protected abstract String getRefBoxKeyView();
}
