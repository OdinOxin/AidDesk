package de.odinoxin.aiddesk.plugins.people;

import de.odinoxin.aidcloud.mapper.PeopleMapper;
import de.odinoxin.aiddesk.Login;
import de.odinoxin.aiddesk.controls.refbox.RefBox;
import de.odinoxin.aiddesk.controls.translateable.Button;
import de.odinoxin.aiddesk.dialogs.MsgDialog;
import de.odinoxin.aiddesk.plugins.RecordEditor;
import de.odinoxin.aiddesk.plugins.addresses.AddressEditor;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

public class PersonEditor extends RecordEditor<Person> {

    private TextField txfForename;
    private TextField txfName;
    private TextField txfCode;
    private Button btnPwd;
    private RefBox refBoxAddress;

    private String currentPwdw;

    public PersonEditor() {
        this(0);
    }

    public PersonEditor(int id) {
        super("/plugins/personeditor.fxml", "Personen");

        this.txfName = (TextField) this.root.lookup("#txfName");
        this.txfForename = (TextField) this.root.lookup("#txfForename");
        this.txfCode = (TextField) this.root.lookup("#txfCode");
        this.btnPwd = (Button) this.root.lookup("#btnPwd");
        this.btnPwd.setOnAction(ev -> new PwdEditor(this));
        this.refBoxAddress = (RefBox) this.root.lookup("#refBoxAddress");
        this.refBoxAddress.setOnNewAction(ev -> {
            AddressEditor addressEditor = new AddressEditor();
            addressEditor.recordId().addListener((observable, oldValue, newValue) -> this.refBoxAddress.setRef((int) newValue));
            addressEditor.isChanged().addListener((observable, oldValue, newValue) -> this.refBoxAddress.update());
        });
        this.refBoxAddress.setOnEditAction(ev -> {
            AddressEditor addressEditor = new AddressEditor(this.refBoxAddress.getRef());
            addressEditor.recordId().addListener((observable, oldValue, newValue) -> this.refBoxAddress.setRef((int) newValue));
            addressEditor.isChanged().addListener((observable, oldValue, newValue) -> this.refBoxAddress.update());
        });

        this.loadRecord(id);
        if (id == 0)
            this.onNew();
    }

    @Override
    protected void onNew() {
        this.txfForename.requestFocus();
    }

    @Override
    protected int onSave() {
        if (this.currentPwdw != null && this.getRecordItem().getPwd() != null)
            if (!PeopleMapper.changePwd(this.getRecordItem().getId(), this.currentPwdw, this.getRecordItem().getPwd()))
                new MsgDialog(this, Alert.AlertType.ERROR, "Fehlgeschlagen!", "Passwort konnte nicht geändert werden.").showAndWait();
        return PeopleMapper.save(this.getRecordItem());
    }

    @Override
    protected boolean onDelete() {
        return PeopleMapper.delete(this.getRecordItem().getId());
    }

    @Override
    protected boolean setRecord(int id) {
        if (id == 0) {
            this.setRecordItem(new Person());
            return true;
        } else {
            Person p = PeopleMapper.get(id);
            if (p != null) {
                this.setDeletable(p.getId() != Login.getPerson().getId());
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
        this.btnPwd.disableProperty().bind(this.getRecordItem().idProperty().isEqualTo(0));
        this.refBoxAddress.refProperty().bindBidirectional(this.getRecordItem().addressIdProperty());
        this.getRecordItem().setChanged(false);
    }

    @Override
    protected String getRefBoxName() {
        return "People";
    }

    public void changePwd(String oldPwd, String newPwd) {
        this.currentPwdw = oldPwd;
        this.getRecordItem().setPwd(newPwd);
    }
}
