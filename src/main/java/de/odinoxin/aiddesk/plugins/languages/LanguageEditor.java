package de.odinoxin.aiddesk.plugins.languages;

import de.odinoxin.aidcloud.provider.LanguageProvider;
import de.odinoxin.aidcloud.provider.Provider;
import de.odinoxin.aiddesk.plugins.RecordEditor;
import javafx.application.Platform;
import javafx.scene.control.TextField;

public class LanguageEditor extends RecordEditor<Language> {

    private TextField txfName;
    private TextField txfCode;

    public LanguageEditor() {
        this(null);
    }

    public LanguageEditor(Language language) {
        super("/plugins/languageeditor.fxml", "Languages");

        this.txfName = (TextField) this.root.lookup("#txfName");
        this.txfCode = (TextField) this.root.lookup("#txfCode");

        this.loadRecord(language);
        if (language == null)
            this.onNew();
    }

    @Override
    protected void onNew() {
        Platform.runLater(() -> this.txfName.requestFocus());
    }

    @Override
    protected Language onSave() {
        return LanguageProvider.save(this.getRecordItem());
    }

    @Override
    protected boolean onDelete() {
        return LanguageProvider.delete(this.getRecordItem().getId());
    }

    @Override
    protected void setRecord(Language language) {
        if (language == null)
            this.setRecordItem(new Language());
        else
            this.setRecordItem(language);
    }

    @Override
    protected void bind() {
        this.txfName.textProperty().bindBidirectional(this.getRecordItem().nameProperty());
        this.txfCode.textProperty().bindBidirectional(this.getRecordItem().codeProperty());
        this.getRecordItem().setChanged(false);
    }

    @Override
    protected Provider<Language> getProvider() {
        return new LanguageProvider();
    }
}
