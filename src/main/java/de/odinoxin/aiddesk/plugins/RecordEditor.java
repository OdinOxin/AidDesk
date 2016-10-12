package de.odinoxin.aiddesk.plugins;

import de.odinoxin.aiddesk.dialogs.MsgDialog;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
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
    private Button btnSave;
    private Button btnDiscard;
    private Button btnDelete;

    private ObjectProperty<T> recordItem = new SimpleObjectProperty<>();
    private T original;
    private int loading = -1;

    private Callback newAction;
    private Callback saveAction;
    private Callback deleteAction;

    public RecordEditor(String res, String title) {
        super("/plugins/recordeditor.fxml", title);

        try {
            Node recordView = FXMLLoader.load(RecordEditor.class.getResource(res));

            this.refBoxKey = (RefBox) this.root.lookup("#refBoxKey");
            this.refBoxKey.setView(this.getRefBoxKeyView());
            this.refBoxKey.setOnNewAction(ev ->
            {
                if (this.newAction != null)
                    this.newAction.call();
            });
            this.refBoxKey.refProperty().addListener((observable, oldValue, newValue) -> this.loadRecord((int) newValue));
            this.txfId = (TextField) this.root.lookup("#txfId");
            ((ScrollPane) this.root.lookup("#scpDetails")).setContent(recordView);

            this.btnSave = (Button) this.root.lookup("#btnSave");
            this.btnSave.setOnAction(ev ->
            {
                if (this.saveAction != null)
                    this.saveAction.call();
            });
            setButtonEnter(this.btnSave);
            this.btnDiscard = (Button) this.root.lookup("#btnDiscard");
            this.btnDiscard.setOnAction(ev -> this.discard());
            setButtonEnter(this.btnDiscard);
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
                if (this.getRecordItem() != null && this.getRecordItem().getId() != 0)
                    DecisionDialog.showDialog(this, "Daten löschen?", "Wollen Sie die Daten wirklich unwiderruflich löschen?", this.deleteAction, null);
            });
            setButtonEnter(this.btnDelete);
            this.sizeToScene();
            this.centerOnScreen();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void discard() {
        if (this.original != null)
            this.loadRecord(this.original.getId());
    }

    protected T getRecordItem() {
        return recordItem.get();
    }

    protected void setRecordItem(T recordItem) {
        this.recordItem.set(recordItem);
    }

    protected ObjectProperty<T> recordItemProperty() {
        return recordItem;
    }

    protected void setNewAction(Callback newAction) {
        this.newAction = () ->
        {
            this.loadRecord(0);
            if (newAction != null)
                newAction.call();
        };
    }

    protected void setSaveAction(Action<Integer> saveAction) {
        this.saveAction = () ->
        {
            int res = 0;
            if (saveAction != null)
                res = saveAction.run();
            if (res != 0) {
                this.getRecordItem().setChanged(false);
                this.loadRecord(res);
            }
        };
    }

    protected void setDeleteAction(Action<Boolean> deleteAction) {
        this.deleteAction = () ->
        {
            boolean succeeded = false;
            if (deleteAction != null)
                succeeded = deleteAction.run();
            if (succeeded)
                MsgDialog.showMsg(this, "Gelöscht!", "Die Daten wurden erfolgreich gelöscht.");
        };
    }

    protected void loadRecord(int id) {

        if (id == this.loading)
            return;

        Callback load = () ->
        {
            this.loading = id;
            this.refBoxKey.setRef(id);
            this.setRecord(id);
            this.loading = -1;
        };

        if (this.getRecordItem() != null && this.getRecordItem().isChanged()) {
            DecisionDialog.showDialog(this, "Änderugen verwerfen?", "Möchten Sie die aktuellen Änderungen verwerfen?", load, null);
        } else
            load.call();
    }

    protected abstract void setRecord(int id);

    protected abstract String getRefBoxKeyView();

    private void setButtonEnter(Button btn) {
        btn.setOnKeyPressed(ev ->
        {
            if (ev.getCode() == KeyCode.ENTER) {
                btn.fire();
                ev.consume();
            }
        });
    }
}
