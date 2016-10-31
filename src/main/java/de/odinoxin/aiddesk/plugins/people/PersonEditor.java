package de.odinoxin.aiddesk.plugins.people;

import de.odinoxin.aidcloud.provider.PersonProvider;
import de.odinoxin.aidcloud.provider.Provider;
import de.odinoxin.aiddesk.Login;
import de.odinoxin.aiddesk.controls.refbox.RefBox;
import de.odinoxin.aiddesk.controls.translateable.Button;
import de.odinoxin.aiddesk.dialogs.MsgDialog;
import de.odinoxin.aiddesk.plugins.Plugin;
import de.odinoxin.aiddesk.plugins.RecordEditor;
import de.odinoxin.aiddesk.plugins.addresses.Address;
import de.odinoxin.aiddesk.plugins.addresses.AddressEditor;
import de.odinoxin.aiddesk.plugins.languages.Language;
import de.odinoxin.aiddesk.plugins.languages.LanguageEditor;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

public class PersonEditor extends RecordEditor<Person> {

    private TextField txfForename;
    private TextField txfName;
    private TextField txfCode;
    private Button btnPwd;
    private RefBox<Language> refBoxLanguage;
    private RefBox<Address> refBoxAddress;

    private String currentPwdw;

    public PersonEditor() {
        this(null);
    }

    public PersonEditor(Person person) {
        super("/plugins/personeditor.fxml", "People");

        this.txfName = (TextField) this.root.lookup("#txfName");
        this.txfForename = (TextField) this.root.lookup("#txfForename");
        this.txfCode = (TextField) this.root.lookup("#txfCode");
        this.btnPwd = (Button) this.root.lookup("#btnPwd");
        this.btnPwd.setOnAction(ev -> new PwdEditor(this));
        Plugin.setButtonEnter(this.btnPwd);
        this.refBoxLanguage = (RefBox<Language>) this.root.lookup("#refBoxLanguage");
        this.refBoxLanguage.setOnNewAction(ev -> {
            LanguageEditor editor = new LanguageEditor();
            editor.recordItem().addListener((observable, oldValue, newValue) -> this.refBoxLanguage.setObj(newValue));
            editor.isChanged().addListener((observable, oldValue, newValue) -> this.refBoxLanguage.update());
        });
        this.refBoxLanguage.setOnEditAction(ev -> {
            LanguageEditor editor = new LanguageEditor(this.refBoxLanguage.getObj());
            editor.recordItem().addListener((observable, oldValue, newValue) -> this.refBoxLanguage.setObj(newValue));
            editor.isChanged().addListener((observable, oldValue, newValue) -> this.refBoxLanguage.update());
        });
        this.refBoxAddress = (RefBox<Address>) this.root.lookup("#refBoxAddress");
        this.refBoxAddress.setOnNewAction(ev -> {
            AddressEditor addressEditor = new AddressEditor();
            addressEditor.recordItem().addListener((observable, oldValue, newValue) -> this.refBoxAddress.setObj(newValue));
            addressEditor.isChanged().addListener((observable, oldValue, newValue) -> this.refBoxAddress.update());
        });
        this.refBoxAddress.setOnEditAction(ev -> {
            AddressEditor addressEditor = new AddressEditor(this.refBoxAddress.getObj());
            addressEditor.recordItem().addListener((observable, oldValue, newValue) -> this.refBoxAddress.setObj(newValue));
            addressEditor.isChanged().addListener((observable, oldValue, newValue) -> this.refBoxAddress.update());
        });

        this.loadRecord(person);
        if (person == null)
            this.onNew();
    }

    @Override
    protected void onNew() {
        this.txfForename.requestFocus();
    }

    @Override
    protected Person onSave() {
        if (this.currentPwdw != null && this.getRecordItem().getPwd() != null)
            if (!PersonProvider.changePwd(this.getRecordItem().getId(), this.currentPwdw, this.getRecordItem().getPwd()))
                new MsgDialog(this, Alert.AlertType.ERROR, "Fehlgeschlagen!", "Passwort konnte nicht geändert werden.").showAndWait();
        return PersonProvider.save(this.getRecordItem());
    }

    @Override
    protected boolean onDelete() {
        return PersonProvider.delete(this.getRecordItem().getId());
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
        this.refBoxLanguage.objProperty().bindBidirectional(this.getRecordItem().languageProperty());
        this.refBoxAddress.objProperty().bindBidirectional(this.getRecordItem().addressProperty());
        this.getRecordItem().setChanged(false);
    }

    @Override
    protected Provider<Person> getProvider() {
        return new PersonProvider();
    }

    public void changePwd(String oldPwd, String newPwd) {
        this.currentPwdw = oldPwd;
        this.getRecordItem().setPwd(newPwd);
    }
}
