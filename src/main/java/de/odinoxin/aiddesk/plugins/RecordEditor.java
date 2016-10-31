package de.odinoxin.aiddesk.plugins;

import de.odinoxin.aidcloud.provider.Provider;
import de.odinoxin.aidcloud.provider.TranslatorProvider;
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
    private ReadOnlyBooleanWrapper changedWrapper = new ReadOnlyBooleanWrapper();
    private T original;

    public RecordEditor(String res, String title) {
        super("/plugins/recordeditor.fxml", title);

        try {
            Node recordView = FXMLLoader.load(RecordEditor.class.getResource(res));

            this.refBoxKey = (RefBox) this.root.lookup("#refBoxKey");
            this.refBoxKey.setProvider(this.getProvider());
            this.refBoxKey.setOnNewAction(ev ->
            {
                this.setRecord(null);
                this.onNew();
            });
            this.refBoxKey.objProperty().addListener((observable, oldValue, newValue) -> this.loadRecord((T) newValue));
            this.txfId = (TextField) this.root.lookup("#txfId");
            ((ScrollPane) this.root.lookup("#scpDetails")).setContent(recordView);

            this.btnSave = (Button) this.root.lookup("#btnSave");
            this.btnSave.setOnAction(ev ->
            {
                T newObj = this.onSave();
                if (newObj != null) {
                    this.getRecordItem().setChanged(false);
                    this.loadRecord(newObj);
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
                    this.txfId.setText(newValue.getId() == 0 ? TranslatorProvider.getTranslation("New") : String.valueOf(newValue.getId()));
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
                    DecisionDialog dialog = new DecisionDialog(this, "Delete data?", "Delete data irrevocably?");
                    Optional<ButtonType> dialogRes = dialog.showAndWait();
                    if (ButtonType.OK.equals(dialogRes.get())) {
                        boolean succeeded = this.onDelete();
                        if (succeeded) {
                            this.loadRecord(null);
                            this.onNew();
                            new MsgDialog(this, Alert.AlertType.INFORMATION, "Deleted!", "Successfully deleted.").show();
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
        this.loadRecord(this.original);
    }

    public T getRecordItem() {
        return recordItem.get();
    }

    protected void setRecordItem(T recordItem) {
        this.recordItem.set(recordItem);
        if (this.changedWrapper.isBound())
            this.changedWrapper.unbind();
        this.changedWrapper.bind(recordItem.changedProperty());
    }

    public ObjectProperty<T> recordItem() {
        return recordItem;
    }

    public ReadOnlyBooleanProperty isChanged() {
        return this.changedWrapper.getReadOnlyProperty();
    }

    protected void loadRecord(T record) {
        Callback apply = () ->
        {
            this.setRecord(record);
            this.bind();
        };

        if ((this.getRecordItem() == null && record == null) || (this.getRecordItem() != null && record != null && this.getRecordItem().getId() == record.getId()))
            return;

        if (this.getRecordItem() != null && this.getRecordItem().isChanged()) {
            DecisionDialog dialog = new DecisionDialog(this, "Discard changes?", "Discard current changes?");
            Optional<ButtonType> dialogRes = dialog.showAndWait();
            if (ButtonType.OK.equals(dialogRes.get()))
                apply.call();
            else
                this.refBoxKey.setObj(this.getRecordItem());
        } else
            apply.call();
    }

    protected abstract void onNew();

    protected abstract T onSave();

    protected void setStoreable(boolean storeable) {
        this.storeable.set(storeable);
    }

    protected abstract boolean onDelete();

    protected void setDeletable(boolean deletable) {
        this.deletable.set(deletable);
    }

    protected abstract void setRecord(T record);

    protected abstract void bind();

    protected abstract Provider<T> getProvider();
}
