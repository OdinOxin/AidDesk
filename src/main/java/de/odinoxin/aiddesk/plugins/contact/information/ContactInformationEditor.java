package de.odinoxin.aiddesk.plugins.contact.information;

import de.odinoxin.aidcloud.mapper.ContactInformationMapper;
import de.odinoxin.aiddesk.controls.refbox.RefBox;
import de.odinoxin.aiddesk.plugins.RecordEditor;
import javafx.scene.control.TextField;

public class ContactInformationEditor extends RecordEditor<ContactInformation> {

    private RefBox refBoxContactType;
    private TextField txfInformation;

    public ContactInformationEditor() {
        this(0);
    }

    public ContactInformationEditor(int id) {
        super("/plugins/contactinformationeditor.fxml", "Contact information");

        this.refBoxContactType = (RefBox) this.root.lookup("#refBoxContactType");
        this.txfInformation = (TextField) this.root.lookup("#txfInformation");

        this.loadRecord(id);
        if (id == 0)
            this.onNew();
    }

    @Override
    protected void onNew() {
        this.refBoxContactType.requestFocus();
    }

    @Override
    protected int onSave() {
        return ContactInformationMapper.save(this.getRecordItem());
    }

    @Override
    protected boolean onDelete() {
        return ContactInformationMapper.delete(this.getRecordItem().getId());
    }

    @Override
    protected boolean setRecord(int id) {
        if (id == 0) {
            this.setRecordItem(new ContactInformation());
            return true;
        } else {
            ContactInformation item = ContactInformationMapper.get(id);
            if (item != null) {
                this.setRecordItem(item);
                return true;
            }
        }
        return false;
    }

    @Override
    protected void bind() {
        this.refBoxContactType.refProperty().bindBidirectional(this.getRecordItem().contactTypeProperty());
        this.txfInformation.textProperty().bindBidirectional(this.getRecordItem().informationProperty());
        this.getRecordItem().setChanged(false);
    }

    @Override
    protected String getRefBoxName() {
        return "ContactInformation";
    }
}
