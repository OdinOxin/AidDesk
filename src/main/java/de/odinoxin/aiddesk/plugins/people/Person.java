package de.odinoxin.aiddesk.plugins.people;

import de.odinoxin.aidcloud.service.ContactInformationEntity;
import de.odinoxin.aidcloud.service.PersonEntity;
import de.odinoxin.aiddesk.plugins.RecordItem;
import de.odinoxin.aiddesk.plugins.addresses.Address;
import de.odinoxin.aiddesk.plugins.contact.information.ContactInformation;
import de.odinoxin.aiddesk.plugins.languages.Language;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;

public class Person extends RecordItem {

    private StringProperty name = new SimpleStringProperty();
    private StringProperty forename = new SimpleStringProperty();
    private StringProperty code = new SimpleStringProperty();
    private StringProperty pwd = new SimpleStringProperty();
    private ObjectProperty<Language> language = new SimpleObjectProperty<>();
    private ObjectProperty<Address> address = new SimpleObjectProperty<>();
    private ObservableList<ContactInformation> contactInformation = FXCollections.observableArrayList();

    public Person() {
        super();
        this.name.addListener((observable, oldValue, newValue) -> setChanged(true));
        this.forename.addListener((observable, oldValue, newValue) -> setChanged(true));
        this.code.addListener((observable, oldValue, newValue) -> setChanged(true));
        this.pwd.addListener((observable, oldValue, newValue) -> setChanged(true));
        this.language.addListener((observable, oldValue, newValue) -> setChanged(true));
        this.address.addListener((observable, oldValue, newValue) -> setChanged(true));
        this.contactInformation.addListener((ListChangeListener.Change<? extends ContactInformation> c) -> setChanged(true));
        this.setChanged(false);
    }

    public Person(int id) {
        this();
        this.setId(id);
        this.setChanged(false);
    }

    public Person(int id, String name, String forename, String code, Language language, Address address, List<ContactInformation> contactInformation) {
        this(id);
        this.setName(name);
        this.setForename(forename);
        this.setCode(code);
        this.setLanguage(language);
        this.setAddress(address);
        this.setContactInformation(contactInformation);
        this.setChanged(false);
    }

    public Person(PersonEntity entity) {
        this(entity.getId(), entity.getName(), entity.getForename(), entity.getCode(), entity.getLanguage() == null ? null : new Language(entity.getLanguage()), entity.getAddress() == null ? null : new Address(entity.getAddress()), null);
        if (entity.getContactInformation() != null) {
            List<ContactInformation> list = new ArrayList<>();
            for (ContactInformationEntity contactInformationEntity : entity.getContactInformation())
                list.add(new ContactInformation(contactInformationEntity));
            this.setContactInformation(list);
        }
        this.setChanged(false);
    }

    @Override
    protected Object clone() {
        return new Person(this.getId(), this.getName(), this.getForename(), this.getCode(), this.getLanguage(), this.getAddress(), this.getContactInformation());
    }

    public String getName() {
        return name.get();
    }

    public String getForename() {
        return forename.get();
    }

    public String getCode() {
        return code.get();
    }

    public String getPwd() {
        return pwd.get();
    }

    public Language getLanguage() {
        return language.get();
    }

    public Address getAddress() {
        return address.get();
    }

    public List<ContactInformation> getContactInformation() {
        return contactInformation;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public void setForename(String forename) {
        this.forename.set(forename);
    }

    public void setCode(String code) {
        this.code.set(code);
    }

    public void setPwd(String pwd) {
        this.pwd.set(pwd);
    }

    public void setLanguage(Language language) {
        this.language.set(language);
    }

    public void setAddress(Address address) {
        this.address.set(address);
    }

    public void setContactInformation(List<ContactInformation> contactInformation) {
        if (this.contactInformation != null) {
            this.contactInformation.clear();
            if (contactInformation != null)
                this.contactInformation.addAll(contactInformation);
        } else if (contactInformation != null)
            this.contactInformation = FXCollections.observableArrayList(contactInformation);
    }

    public StringProperty nameProperty() {
        return name;
    }

    public StringProperty forenameProperty() {
        return forename;
    }

    public StringProperty codeProperty() {
        return code;
    }

    public StringProperty pwdProperty() {
        return pwd;
    }

    public ObjectProperty<Language> languageProperty() {
        return language;
    }

    public ObjectProperty<Address> addressProperty() {
        return address;
    }

    public ObservableList<ContactInformation> contactInformationProperty() {
        return contactInformation;
    }

    public PersonEntity toEntity() {
        PersonEntity entity = new PersonEntity();
        entity.setId(this.getId());
        entity.setName(this.getName());
        entity.setForename(this.getForename());
        entity.setCode(this.getCode());
        entity.setLanguage(this.getLanguage() == null ? null : this.getLanguage().toEntity());
        entity.setAddress(this.getAddress() == null ? null : this.getAddress().toEntity());
        for (ContactInformation info : this.getContactInformation())
            if (info != null)
                entity.getContactInformation().add(info.toEntity());
        return entity;
    }
}
