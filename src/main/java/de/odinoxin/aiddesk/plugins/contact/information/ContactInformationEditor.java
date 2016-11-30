package de.odinoxin.aiddesk.plugins.contact.information;

import de.odinoxin.aidcloud.provider.ContactInformationProvider;
import de.odinoxin.aidcloud.provider.ContactTypeProvider;
import de.odinoxin.aidcloud.provider.Provider;
import de.odinoxin.aidcloud.service.ConcurrentFault_Exception;
import de.odinoxin.aiddesk.controls.refbox.RefBox;
import de.odinoxin.aiddesk.plugins.RecordEditor;
import de.odinoxin.aiddesk.plugins.contact.types.ContactType;
import javafx.scene.control.TextField;

public class ContactInformationEditor extends RecordEditor<ContactInformation> {

    private RefBox<ContactType> refBoxContactType;
    private TextField txfInformation;

    public ContactInformationEditor(ContactInformation contactInformation) {
        super("/plugins/contactinformationeditor.fxml", "Contact information");

        this.refBoxContactType = (RefBox<ContactType>) this.root.lookup("#refBoxContactType");
        this.refBoxContactType.setProvider(new ContactTypeProvider());
        this.txfInformation = (TextField) this.root.lookup("#txfInformation");

        this.loadRecord(contactInformation);
        if (contactInformation == null)
            this.onNew();
    }

    @Override
    protected void onNew() {
        this.refBoxContactType.requestFocus();
    }

    @Override
    protected ContactInformation onSave() throws ConcurrentFault_Exception {
        return this.getProvider().save(this.getRecordItem(), this.getOriginalItem());
    }

    @Override
    protected boolean onDelete() {
        return this.getProvider().delete(this.getRecordItem().getId());
    }

    @Override
    protected void setRecord(ContactInformation contactInformation) {
        if (contactInformation == null)
            this.setRecordItem(new ContactInformation());
        else
            this.setRecordItem(contactInformation);
    }

    @Override
    protected void bind() {
        this.refBoxContactType.objProperty().bindBidirectional(this.getRecordItem().contactTypeProperty());
        this.txfInformation.textProperty().bindBidirectional(this.getRecordItem().informationProperty());
        this.getRecordItem().setChanged(false);
    }

    @Override
    protected Provider<ContactInformation> initProvider() {
        return new ContactInformationProvider();
    }
}
