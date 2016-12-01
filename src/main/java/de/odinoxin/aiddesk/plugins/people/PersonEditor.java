package de.odinoxin.aiddesk.plugins.people;

import de.odinoxin.aidcloud.provider.*;
import de.odinoxin.aidcloud.service.ConcurrentFault_Exception;
import de.odinoxin.aiddesk.Login;
import de.odinoxin.aiddesk.controls.refbox.RefBox;
import de.odinoxin.aiddesk.controls.reflist.RefList;
import de.odinoxin.aiddesk.controls.translateable.Button;
import de.odinoxin.aiddesk.dialogs.MsgDialog;
import de.odinoxin.aiddesk.plugins.Plugin;
import de.odinoxin.aiddesk.plugins.RecordEditor;
import de.odinoxin.aiddesk.plugins.addresses.Address;
import de.odinoxin.aiddesk.plugins.contact.information.ContactInformation;
import de.odinoxin.aiddesk.plugins.languages.Language;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

public class PersonEditor extends RecordEditor<Person> {

    private TextField txfForename;
    private TextField txfName;
    private TextField txfCode;
    private Button btnPwd;
    private RefBox<Language> refBoxLanguage;
    private RefBox<Address> refBoxAddress;
    private RefList<ContactInformation> refListContactInformation;

    private String currentPwdw;

    public PersonEditor(Person person) {
        super("/plugins/personeditor.fxml", "People");

        this.txfName = (TextField) this.root.lookup("#txfName");
        this.txfForename = (TextField) this.root.lookup("#txfForename");
        this.txfCode = (TextField) this.root.lookup("#txfCode");
        this.btnPwd = (Button) this.root.lookup("#btnPwd");
        this.btnPwd.setOnAction(ev -> new PwdEditor(this));
        Plugin.setButtonEnter(this.btnPwd);
        this.refBoxLanguage = (RefBox<Language>) this.root.lookup("#refBoxLanguage");
        this.refBoxLanguage.setProvider(new LanguageProvider());
        this.refBoxAddress = (RefBox<Address>) this.root.lookup("#refBoxAddress");
        this.refBoxAddress.setProvider(new AddressProvider());
        this.refListContactInformation = (RefList<ContactInformation>) this.root.lookup("#refListContactInformation");
        this.refListContactInformation.setProvider(new ContactInformationProvider());
        this.attemptLoadRecord(person);
        if (person == null)
            this.onNew();
    }

    @Override
    protected void onNew() {
        this.txfForename.requestFocus();
    }

    @Override
    protected Person onSave() throws ConcurrentFault_Exception {
        if (this.currentPwdw != null && this.getRecordItem().getPwd() != null)
            if (!PersonProvider.changePwd(this.getRecordItem().getId(), this.currentPwdw, this.getRecordItem().getPwd()))
                new MsgDialog(this, Alert.AlertType.ERROR, "Fehlgeschlagen!", "Passwort konnte nicht geändert werden.").showAndWait();
        return this.getProvider().save(this.getRecordItem(), this.getOriginalItem());
    }

    @Override
    protected boolean onDelete() {
        return this.getProvider().delete(this.getRecordItem().getId());
    }

    @Override
    protected void setRecord(Person person) {
        if (person == null)
            this.setRecordItem(new Person());
        else {
            this.setDeletable(person.getId() != Login.getPerson().getId());
            this.setRecordItem(person);
        }
    }

    @Override
    protected void bind() {
        this.txfForename.textProperty().bindBidirectional(this.getRecordItem().forenameProperty());
        this.txfName.textProperty().bindBidirectional(this.getRecordItem().nameProperty());
        this.txfCode.textProperty().bindBidirectional(this.getRecordItem().codeProperty());
        this.btnPwd.disableProperty().bind(this.getRecordItem().idProperty().isEqualTo(0));
        this.refBoxLanguage.recordProperty().bindBidirectional(this.getRecordItem().languageProperty());
        this.refBoxAddress.recordProperty().bindBidirectional(this.getRecordItem().addressProperty());
        this.refListContactInformation.bindBidirectional(this.getRecordItem().contactInformationProperty());
        this.getRecordItem().setChanged(false);
    }

    @Override
    protected Provider<Person> initProvider() {
        return new PersonProvider();
    }

    void changePwd(String currentPwd, String newPwd) {
        this.currentPwdw = currentPwd;
        this.getRecordItem().setPwd(newPwd);
    }
}
