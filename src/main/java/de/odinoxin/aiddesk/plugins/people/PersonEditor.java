package de.odinoxin.aiddesk.plugins.people;

import de.odinoxin.aidcloud.helper.PeopleHelper;
import javafx.scene.control.TextField;
import de.odinoxin.aiddesk.controls.refbox.RefBox;
import de.odinoxin.aiddesk.plugins.RecordEditor;
import de.odinoxin.aiddesk.plugins.addresses.AddressEditor;

public class PersonEditor extends RecordEditor<Person> {

    private TextField txfForename;
    private TextField txfName;
    private TextField txfCode;
    private RefBox refBoxAddress;

    public PersonEditor() {
        super("/plugins/personeditor.fxml", "Personen");

        this.txfName = (TextField) this.root.lookup("#txfName");
        this.txfForename = (TextField) this.root.lookup("#txfForename");
        this.txfCode = (TextField) this.root.lookup("#txfCode");
        this.refBoxAddress = (RefBox) this.root.lookup("#refBoxAddress");
        this.refBoxAddress.setOnNewAction(ev -> new AddressEditor());
        this.refBoxAddress.setOnEditAction(ev -> new AddressEditor(this.refBoxAddress.getRef()).recordId().addListener((observable, oldValue, newValue) -> this.refBoxAddress.setRef((int) newValue)));

        this.loadRecord(0);
    }

    public PersonEditor(int id) {
        this();
        this.loadRecord(id);
    }

    @Override
    protected void onNew() {
        this.txfForename.requestFocus();
    }

    @Override
    protected int onSave() {
        return PeopleHelper.save(this.getRecordItem());
    }

    @Override
    protected boolean onDelete() {
        return PeopleHelper.delete(this.getRecordItem().getId());
    }

    @Override
    protected boolean setRecord(int id) {
        if (id == 0) {
            this.setRecordItem(new Person());
            return true;
        } else {
            Person p = PeopleHelper.get(id);
            if (p != null) {
                this.setRecordItem(p);
                return true;
            }
        }
        return false;
    }

    @Override
    protected void bind() {
        this.txfName.textProperty().bindBidirectional(this.getRecordItem().nameProperty());
        this.txfForename.textProperty().bindBidirectional(this.getRecordItem().forenameProperty());
        this.txfCode.textProperty().bindBidirectional(this.getRecordItem().codeProperty());
        this.refBoxAddress.refProperty().bindBidirectional(this.getRecordItem().addressIdProperty());
        this.getRecordItem().setChanged(false);
    }

    @Override
    protected String getRefBoxKeyView() {
        return "V_People";
    }
}
