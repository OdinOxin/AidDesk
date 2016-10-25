package de.odinoxin.aiddesk.plugins.contact.types;

import de.odinoxin.aidcloud.mapper.ContactTypesMapper;
import de.odinoxin.aiddesk.plugins.RecordEditor;
import javafx.application.Platform;
import javafx.scene.control.TextField;

public class ContactTypeEditor extends RecordEditor<ContactType> {

    private TextField txfName;
    private TextField txfCode;
    private TextField txfRegex;

    public ContactTypeEditor() {
        this(0);
    }

    public ContactTypeEditor(int id) {
        super("/plugins/contacttypeeditor.fxml", "Contact type");

        this.txfName = (TextField) this.root.lookup("#txfName");
        this.txfCode = (TextField) this.root.lookup("#txfCode");
        this.txfRegex = (TextField) this.root.lookup("#txfRegex");

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
        return ContactTypesMapper.save(this.getRecordItem());
    }

    @Override
    protected boolean onDelete() {
        return ContactTypesMapper.delete(this.getRecordItem().getId());
    }

    @Override
    protected boolean setRecord(int id) {
        if (id == 0) {
            this.setRecordItem(new ContactType());
            return true;
        } else {
            ContactType item = ContactTypesMapper.get(id);
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
        this.txfRegex.textProperty().bindBidirectional(this.getRecordItem().regexProperty());
        this.getRecordItem().setChanged(false);
    }

    @Override
    protected String getRefBoxName() {
        return "ContactTypes";
    }
}
