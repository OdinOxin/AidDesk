package de.odinoxin.aiddesk.plugins.people;

import de.odinoxin.aidcloud.service.PersonEntity;
import de.odinoxin.aiddesk.plugins.RecordItem;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Person extends RecordItem {

    private StringProperty name = new SimpleStringProperty();
    private StringProperty forename = new SimpleStringProperty();
    private StringProperty code = new SimpleStringProperty();
    private StringProperty pwd = new SimpleStringProperty();
    private IntegerProperty language = new SimpleIntegerProperty();
    private IntegerProperty addressId = new SimpleIntegerProperty();

    public Person() {
        super();
        this.name.addListener((observable, oldValue, newValue) -> setChanged(true));
        this.forename.addListener((observable, oldValue, newValue) -> setChanged(true));
        this.code.addListener((observable, oldValue, newValue) -> setChanged(true));
        this.pwd.addListener((observable, oldValue, newValue) -> setChanged(true));
        this.language.addListener((observable, oldValue, newValue) -> setChanged(true));
        this.addressId.addListener((observable, oldValue, newValue) -> setChanged(true));
        this.setChanged(false);
    }

    public Person(int id) {
        this();
        this.setId(id);
        this.setChanged(false);
    }

    public Person(int id, String name, String forename, String code, int language, int addressId) {
        this(id);
        this.setName(name);
        this.setForename(forename);
        this.setCode(code);
        this.setLanguage(language);
        this.setAddressId(addressId);
        this.setChanged(false);
    }

    @Override
    protected Object clone() {
        return new Person(this.getId(), this.getName(), this.getForename(), this.getCode(), this.getLanguage(), this.getAddressId());
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

    public int getLanguage() {
        return language.get();
    }

    public int getAddressId() {
        return addressId.get();
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

    public void setLanguage(int language) {
        this.language.set(language);
    }

    public void setAddressId(int addressId) {
        this.addressId.set(addressId);
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

    public IntegerProperty languageProperty() {
        return language;
    }

    public IntegerProperty addressIdProperty() {
        return addressId;
    }

    public PersonEntity toService() {
        PersonEntity p = new PersonEntity();
        p.setId(this.getId());
        p.setName(this.getName());
        p.setForename(this.getForename());
        p.setCode(this.getCode());
        p.setLanguage(this.getLanguage());
        p.setAddressId(this.getAddressId());
        return p;
    }
}
