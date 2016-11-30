package de.odinoxin.aiddesk.plugins.contact.types;

import de.odinoxin.aidcloud.provider.ContactTypeProvider;
import de.odinoxin.aidcloud.provider.Provider;
import de.odinoxin.aidcloud.service.ConcurrentFault_Exception;
import de.odinoxin.aiddesk.plugins.RecordEditor;
import javafx.application.Platform;
import javafx.scene.control.TextField;

public class ContactTypeEditor extends RecordEditor<ContactType> {

    private TextField txfName;
    private TextField txfCode;
    private TextField txfRegex;

    public ContactTypeEditor(ContactType contactType) {
        super("/plugins/contacttypeeditor.fxml", "Contact type");

        this.txfName = (TextField) this.root.lookup("#txfName");
        this.txfCode = (TextField) this.root.lookup("#txfCode");
        this.txfRegex = (TextField) this.root.lookup("#txfRegex");

        this.loadRecord(contactType);
        if (contactType == null)
            this.onNew();
    }

    @Override
    protected void onNew() {
        Platform.runLater(() -> this.txfName.requestFocus());
    }

    @Override
    protected ContactType onSave() throws ConcurrentFault_Exception {
        return this.getProvider().save(this.getRecordItem(), this.getOriginalItem());
    }

    @Override
    protected boolean onDelete() {
        return this.getProvider().delete(this.getRecordItem().getId());
    }

    @Override
    protected void setRecord(ContactType contactType) {
        if (contactType == null)
            this.setRecordItem(new ContactType());
        else
            this.setRecordItem(contactType);
    }

    @Override
    protected void bind() {
        this.txfName.textProperty().bindBidirectional(this.getRecordItem().nameProperty());
        this.txfCode.textProperty().bindBidirectional(this.getRecordItem().codeProperty());
        this.txfRegex.textProperty().bindBidirectional(this.getRecordItem().regexProperty());
        this.getRecordItem().setChanged(false);
    }

    @Override
    protected Provider<ContactType> initProvider() {
        return new ContactTypeProvider();
    }
}
