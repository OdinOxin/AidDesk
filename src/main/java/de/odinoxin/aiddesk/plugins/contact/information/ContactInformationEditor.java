package de.odinoxin.aiddesk.plugins.contact.information;

import de.odinoxin.aidcloud.provider.ContactInformationProvider;
import de.odinoxin.aidcloud.provider.Provider;
import de.odinoxin.aiddesk.controls.refbox.RefBox;
import de.odinoxin.aiddesk.plugins.RecordEditor;
import javafx.scene.control.TextField;

public class ContactInformationEditor extends RecordEditor<ContactInformation> {

    private RefBox refBoxContactType;
    private TextField txfInformation;

    public ContactInformationEditor() {
        this(null);
    }

    public ContactInformationEditor(ContactInformation contactInformation) {
        super("/plugins/contactinformationeditor.fxml", "Contact information");

        this.refBoxContactType = (RefBox) this.root.lookup("#refBoxContactType");
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
    protected ContactInformation onSave() {
        return ContactInformationProvider.save(this.getRecordItem());
    }

    @Override
    protected boolean onDelete() {
        return ContactInformationProvider.delete(this.getRecordItem().getId());
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
    protected Provider<ContactInformation> getProvider() {
        return new ContactInformationProvider();
    }
}
