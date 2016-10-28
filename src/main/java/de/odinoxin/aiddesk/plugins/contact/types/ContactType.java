package de.odinoxin.aiddesk.plugins.contact.types;

import de.odinoxin.aidcloud.service.ContactTypeEntity;
import de.odinoxin.aiddesk.plugins.RecordItem;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ContactType extends RecordItem<ContactTypeEntity> {

    private StringProperty name = new SimpleStringProperty();
    private StringProperty code = new SimpleStringProperty();
    private StringProperty regex = new SimpleStringProperty();

    public ContactType() {
        super();
        this.name.addListener((observable, oldValue, newValue) -> this.setChanged(true));
        this.code.addListener((observable, oldValue, newValue) -> this.setChanged(true));
        this.regex.addListener((observable, oldValue, newValue) -> this.setChanged(true));
        this.setChanged(false);
    }

    public ContactType(int id) {
        this();
        this.setId(id);
        this.setChanged(false);
    }

    public ContactType(int id, String name, String code, String regex) {
        this(id);
        this.setName(name);
        this.setCode(code);
        this.setRegex(regex);
        this.setChanged(false);
    }

    public ContactType(ContactTypeEntity entity)
    {
        this(entity.getId(), entity.getName(), entity.getCode(), entity.getRegex());
    }

    public String getName() {
        return name.get();
    }

    public String getCode() {
        return code.get();
    }

    public String getRegex() {
        return regex.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public void setCode(String code) {
        this.code.set(code);
    }

    public void setRegex(String regex) {
        this.regex.set(regex);
    }

    public StringProperty nameProperty() {
        return name;
    }

    public StringProperty codeProperty() {
        return code;
    }

    public StringProperty regexProperty() {
        return regex;
    }

    @Override
    public ContactTypeEntity toEntity() {
        ContactTypeEntity entity = new ContactTypeEntity();
        entity.setId(this.getId());
        entity.setName(this.getName());
        entity.setCode(this.getCode());
        entity.setRegex(this.getRegex());
        return entity;
    }
}
