package de.odinoxin.aiddesk.plugins.languages;

import de.odinoxin.aidcloud.mapper.LanguagesMapper;
import de.odinoxin.aiddesk.plugins.RecordEditor;
import javafx.application.Platform;
import javafx.scene.control.TextField;

public class LanguageEditor extends RecordEditor<Language> {

    private TextField txfName;
    private TextField txfCode;

    public LanguageEditor() {
        this(0);
    }

    public LanguageEditor(int id) {
        super("/plugins/languageeditor.fxml", "Languages");

        this.txfName = (TextField) this.root.lookup("#txfName");
        this.txfCode = (TextField) this.root.lookup("#txfCode");

        this.loadRecord(id);
        if (id == 0)
            this.onNew();
    }

    @Override
    protected void onNew() {
        Platform.runLater(() -> this.txfName.requestFocus());
    }

    @Override
    protected int onSave() {
        return LanguagesMapper.save(this.getRecordItem());
    }

    @Override
    protected boolean onDelete() {
        return LanguagesMapper.delete(this.getRecordItem().getId());
    }

    @Override
    protected boolean setRecord(int id) {
        if (id == 0) {
            this.setRecordItem(new Language());
            return true;
        } else {
            Language item = LanguagesMapper.get(id);
            if (item != null) {
                this.setRecordItem(item);
                return true;
            }
        }
        return false;
    }

    @Override
    protected void bind() {
        this.txfName.textProperty().bindBidirectional(this.getRecordItem().nameProperty());
        this.txfCode.textProperty().bindBidirectional(this.getRecordItem().codeProperty());
        this.getRecordItem().setChanged(false);
    }

    @Override
    protected String getRefBoxName() {
        return "Languages";
    }
}
