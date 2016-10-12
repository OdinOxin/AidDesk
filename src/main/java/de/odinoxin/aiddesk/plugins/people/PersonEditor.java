package de.odinoxin.aiddesk.plugins.people;

import java.sql.SQLException;

import de.odinoxin.aidcloud.PeopleService;
import javafx.scene.control.TextField;
import de.odinoxin.aiddesk.controls.refbox.RefBox;
import de.odinoxin.aiddesk.plugins.RecordEditor;
import de.odinoxin.aiddesk.plugins.addresses.AddressEditor;

import javax.xml.ws.WebServiceRef;

public class PersonEditor extends RecordEditor<Person> {

    @WebServiceRef(wsdlLocation = "http://localhost:15123/AidCloud/PeopleService?wsdl")
    private static PeopleService peopleService;

    private TextField txfForename;
    private TextField txfName;
    private TextField txfCode;
    private RefBox refBoxAddress;

    static {
        PersonEditor.peopleService = new PeopleService();
    }

    public PersonEditor() {
        super("/plugins/personeditor.fxml", "Personen");

        this.txfName = (TextField) this.root.lookup("#txfName");
        this.txfForename = (TextField) this.root.lookup("#txfForename");
        this.txfCode = (TextField) this.root.lookup("#txfCode");
        this.refBoxAddress = (RefBox) this.root.lookup("#refBoxAddress");
        this.refBoxAddress.setOnNewAction(ev -> new AddressEditor());
        this.refBoxAddress.setOnEditAction(ev -> new AddressEditor(this.refBoxAddress.getRef()).recordId().addListener((observable, oldValue, newValue) -> this.refBoxAddress.setRef((int) newValue)));
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
    protected int onSave() throws SQLException {
        return PersonEditor.peopleService.getPeoplePort().save(this.getRecordItem().toService());
    }

    @Override
    protected boolean onDelete() throws SQLException {
        return PersonEditor.peopleService.getPeoplePort().delete(this.getRecordItem().getId());
    }

    @Override
    protected boolean setRecord(int id) throws SQLException {
        if (id == 0) {
            this.setRecordItem(new Person());
            return true;
        } else {
            de.odinoxin.aidcloud.Person p = PersonEditor.peopleService.getPeoplePort().getPerson(id);
            if (p != null) {
                this.setRecordItem(new Person(p.getId(), p.getName(), p.getForename(), p.getCode(), p.getLanguage(), p.getAddressId()));
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
