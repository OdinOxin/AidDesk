package de.odinoxin.aiddesk.plugins;

import de.odinoxin.aidcloud.mapper.TranslatorMapper;
import de.odinoxin.aiddesk.controls.refbox.RefBox;
import de.odinoxin.aiddesk.controls.translateable.Button;
import de.odinoxin.aiddesk.dialogs.Callback;
import de.odinoxin.aiddesk.dialogs.DecisionDialog;
import de.odinoxin.aiddesk.dialogs.MsgDialog;
import javafx.beans.property.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;

import java.util.Optional;

public abstract class RecordEditor<T extends RecordItem> extends Plugin {

    private TextField txfId;
    private RefBox refBoxKey;
    private Button btnSave;
    private Button btnDiscard;
    private Button btnDelete;

    private ObjectProperty<T> recordItem = new SimpleObjectProperty<>();
    private BooleanProperty storeable = new SimpleBooleanProperty(true);
    private BooleanProperty deletable = new SimpleBooleanProperty(true);
    private ReadOnlyIntegerWrapper idWrapper = new ReadOnlyIntegerWrapper();
    private ReadOnlyBooleanWrapper changedWrapper = new ReadOnlyBooleanWrapper();
    private T original;
    private int loading = -1;

    public RecordEditor(String res, String title) {
        super("/plugins/recordeditor.fxml", title);

        try {
            Node recordView = FXMLLoader.load(RecordEditor.class.getResource(res));

            this.refBoxKey = (RefBox) this.root.lookup("#refBoxKey");
            this.refBoxKey.setName(this.getRefBoxName());
            this.refBoxKey.setOnNewAction(ev ->
            {
                this.loadRecord(0);
                this.onNew();
            });
            this.refBoxKey.refProperty().addListener((observable, oldValue, newValue) -> this.loadRecord((int) newValue));
            this.txfId = (TextField) this.root.lookup("#txfId");
            ((ScrollPane) this.root.lookup("#scpDetails")).setContent(recordView);

            this.btnSave = (Button) this.root.lookup("#btnSave");
            this.btnSave.setOnAction(ev ->
            {
                int newId = this.onSave();
                if (newId != 0) {
                    this.getRecordItem().setChanged(false);
                    this.loadRecord(newId);
                }
            });
            setButtonEnter(this.btnSave);
            this.btnDiscard = (Button) this.root.lookup("#btnDiscard");
            this.btnDiscard.setOnAction(ev -> this.discard());
            setButtonEnter(this.btnDiscard);
            this.recordItem().addListener((observable, oldValue, newValue) ->
            {
                if (newValue == null) {
                    this.original = null;
                    this.txfId.setText("");
                    this.btnSave.setDisable(true);
                    this.btnDiscard.setDisable(true);
                    this.btnDelete.setDisable(true);
                } else {
                    this.original = (T) newValue.clone();
                    this.txfId.setText(newValue.getId() == 0 ? TranslatorMapper.getTranslation("Neu") : String.valueOf(newValue.getId()));
                    if (this.btnSave.disableProperty().isBound())
                        this.btnSave.disableProperty().unbind();
                    this.btnSave.disableProperty().bind(this.storeable.not().or(newValue.changedProperty().not()));
                    if (this.btnDiscard.disableProperty().isBound())
                        this.btnDiscard.disableProperty().unbind();
                    this.btnDiscard.disableProperty().bind(newValue.changedProperty().not());
                    if (this.btnDelete.disableProperty().isBound())
                        this.btnDelete.disableProperty().unbind();
                    this.btnDelete.disableProperty().bind(this.deletable.not().or(newValue.idProperty().isEqualTo(0)));
                }
            });

            this.btnDelete = (Button) this.root.lookup("#btnDelete");
            this.btnDelete.setOnAction(ev ->
            {
                if (this.getRecordItem() != null && this.getRecordItem().getId() != 0) {
                    DecisionDialog dialog = new DecisionDialog(this, "Daten löschen?", "Wollen Sie die Daten wirklich unwiderruflich löschen?");
                    Optional<ButtonType> dialogRes = dialog.showAndWait();
                    if (ButtonType.OK.equals(dialogRes.get())) {
                        boolean succeeded = this.onDelete();
                        if (succeeded) {
                            this.loadRecord(0);
                            this.onNew();
                            new MsgDialog(this, Alert.AlertType.INFORMATION, "Gelöscht!", "Die Daten wurden erfolgreich gelöscht.").show();
                        }
                    }
                }
            });
            setButtonEnter(this.btnDelete);
            this.show();
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
        if (this.idWrapper.isBound())
            this.idWrapper.unbind();
        this.idWrapper.bind(recordItem.idProperty());
        if (this.changedWrapper.isBound())
            this.changedWrapper.unbind();
        this.changedWrapper.bind(recordItem.changedProperty());
    }

    private ObjectProperty<T> recordItem() {
        return recordItem;
    }

    public ReadOnlyIntegerProperty recordId() {
        return this.idWrapper.getReadOnlyProperty();
    }

    public ReadOnlyBooleanProperty isChanged() {
        return this.changedWrapper.getReadOnlyProperty();
    }

    protected void loadRecord(int id) {

        if (id == this.loading)
            return;

        Callback load = () ->
        {
            this.loading = id;
            if (this.refBoxKey.getRef() == id)
                this.refBoxKey.update();
            else
                this.refBoxKey.setRef(id);
            if (this.setRecord(id))
                this.bind();
            this.loading = -1;
        };

        if (this.getRecordItem() != null && this.getRecordItem().isChanged()) {
            DecisionDialog dialog = new DecisionDialog(this, "Änderugen verwerfen?", "Möchten Sie die aktuellen Änderungen verwerfen?");
            Optional<ButtonType> dialogRes = dialog.showAndWait();
            if (ButtonType.OK.equals(dialogRes.get()))
                load.call();
        } else
            load.call();
    }

    protected abstract void onNew();

    protected abstract int onSave();

    protected void setStoreable(boolean storeable) {
        this.storeable.set(storeable);
    }

    protected abstract boolean onDelete();

    protected void setDeletable(boolean deletable) {
        this.deletable.set(deletable);
    }

    protected abstract boolean setRecord(int id);

    protected abstract void bind();

    protected abstract String getRefBoxName();
}
