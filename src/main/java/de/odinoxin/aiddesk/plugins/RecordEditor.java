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

/**
 * Base class for RecordEditors.
 * @param <T> The type of the RecordItem to edit
 */
public abstract class RecordEditor<T extends RecordItem> extends Plugin {

    private TextField txfId;
    private RefBox<T> refBoxKey;
    private Button btnRefresh;
    private Button btnSave;
    private Button btnDiscard;
    private Button btnDelete;

    private Provider<T> provider;
    /**
     * The current record.
     */
    private ObjectProperty<T> recordItem = new SimpleObjectProperty<>();
    private BooleanProperty storeable = new SimpleBooleanProperty(true);
    private BooleanProperty deletable = new SimpleBooleanProperty(true);
    private ReadOnlyBooleanWrapper changedWrapper = new ReadOnlyBooleanWrapper();
    /**
     * The original, unmodified RecordItem from the service.
     * For resetting use-cases.
     */
    private T original;

    public RecordEditor(String res, String title) {
        super("/plugins/recordeditor.fxml", title);

        try {
            Node recordView = FXMLLoader.load(RecordEditor.class.getResource(res));

            this.provider = this.initProvider();

            this.refBoxKey = (RefBox<T>) this.root.lookup("#refBoxKey");
            this.refBoxKey.setProvider(this.provider);
            this.refBoxKey.setOnNewAction(ev ->
            {
                this.attemptLoadRecord(null);
                this.refBoxKey.setRecord(this.getRecordItem());
                this.onNew();
            });
            this.refBoxKey.recordProperty().addListener((observable, oldValue, newValue) -> this.attemptLoadRecord(newValue == null || this.provider == null ? null : this.provider.get(newValue.getId())));
            this.btnRefresh = (Button) this.root.lookup("#btnRefresh");
            this.btnRefresh.setOnAction(ev -> this.attemptLoadRecord(this.provider.get(this.getRecordItem().getId())));
            this.txfId = (TextField) this.root.lookup("#txfId");
            ((ScrollPane) this.root.lookup("#scpDetails")).setContent(recordView);

            this.btnSave = (Button) this.root.lookup("#btnSave");
            this.btnSave.setOnAction(ev ->
            {
                T newObj = this.onSave();
                if (newObj != null) {
                    this.getRecordItem().setChanged(false);
                    this.attemptLoadRecord(newObj);
                    this.refBoxKey.setRecord(newObj);
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
                    if (dialogRes.isPresent() && ButtonType.OK.equals(dialogRes.get())) {
                        boolean succeeded = this.onDelete();
                        if (succeeded) {
                            this.attemptLoadRecord(null);
                            this.refBoxKey.setRecord(null);
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

    /**
     * Discard all current changes, by restoring the original item.
     */
    private void discard() {
        this.attemptLoadRecord(this.original);
    }

    /**
     * Returns the current record.
     * @return The current record.
     */
    public ObjectProperty<T> recordItem() {
        return recordItem;
    }

    /**
     * Indicates, whether the current record has any changes.
     * @return ReadOnlyBooleanProperty to listen to the first change.
     */
    public ReadOnlyBooleanProperty isChanged() {
        return this.changedWrapper.getReadOnlyProperty();
    }

    /**
     * Attempts to load the given record.
     * @param record The record to load.
     */
    protected void attemptLoadRecord(T record) {
        Callback apply = () ->
        {
            this.setRecord(record);
            if (this.getRecordItem() != null)
                this.bind();
        };

        if (this.getRecordItem() != null && this.getRecordItem().isChanged()) {
            DecisionDialog dialog = new DecisionDialog(this, "Discard changes?", "Discard current changes?");
            Optional<ButtonType> dialogRes = dialog.showAndWait();
            if (ButtonType.OK.equals(dialogRes.get()))
                apply.call();
            else
                this.refBoxKey.setRecord(this.getRecordItem());
        } else
            apply.call();
    }

    /**
     * Called, when a new record was created.
     */
    protected abstract void onNew();

    /**
     * Called, when the current record should be saved.
     * @return The saved record.
     */
    protected abstract T onSave();

    /**
     * Sets, whether saving is enabled.
     * @param storeable True, if saving is enabled; False otherwise.
     */
    protected void setStoreable(boolean storeable) {
        this.storeable.set(storeable);
    }

    /**
     * Called, when the current record should be deleted.
     * @return Success indicator
     */
    protected abstract boolean onDelete();

    /**
     * Sets, whether the deleting is enabled.
     * @param deletable True, if deleting is enabled; False otherwise.
     */
    protected void setDeletable(boolean deletable) {
        this.deletable.set(deletable);
    }

    /**
     * Sets the current item, without checking for current changes to save.
     * @param record The record to set.
     */
    protected abstract void setRecord(T record);

    /**
     * @return The current record item.
     */
    public T getRecordItem() {
        return recordItem.get();
    }

    /**
     * Sets the current record item.
     * Should only called by implementations of setRecord()!
     * @param record The record to set.
     */
    protected void setRecordItem(T record) {
        this.recordItem.set(record);
        if (this.changedWrapper.isBound())
            this.changedWrapper.unbind();
        this.changedWrapper.bind(record.changedProperty());
    }

    /**
     * Binding UI elements to the (new) record.
     */
    protected abstract void bind();

    /**
     * Initializes the provider.
     * @return The initialized provider.
     */
    protected abstract Provider<T> initProvider();

    /**
     * Returns the provider.
     * @return The provider.
     */
    protected Provider<T> getProvider() {
        return this.provider;
    }
}
