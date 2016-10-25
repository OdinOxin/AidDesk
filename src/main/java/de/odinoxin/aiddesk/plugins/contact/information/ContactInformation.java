package de.odinoxin.aiddesk.plugins.contact.information;

import de.odinoxin.aidcloud.service.ContactInformationEntity;
import de.odinoxin.aiddesk.plugins.RecordItem;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ContactInformation extends RecordItem<ContactInformationEntity> {

    private IntegerProperty contactType = new SimpleIntegerProperty();
    private StringProperty information = new SimpleStringProperty();

    public ContactInformation() {
        super();
        contactType.addListener((observable, oldValue, newValue) -> this.setChanged(true));
        information.addListener((observable, oldValue, newValue) -> this.setChanged(true));
        this.setChanged(false);
    }

    public ContactInformation(int id) {
        this();
        this.setId(id);
        this.setChanged(false);
    }

    public ContactInformation(int id, int contactType, String information) {
        this(id);
        this.setContactType(contactType);
        this.setInformation(information);
        this.setChanged(false);
    }

    public int getContactType() {
        return contactType.get();
    }

    public String getInformation() {
        return information.get();
    }

    public void setContactType(int contactType) {
        this.contactType.set(contactType);
    }

    public void setInformation(String information) {
        this.information.set(information);
    }

    public IntegerProperty contactTypeProperty() {
        return contactType;
    }

    public StringProperty informationProperty() {
        return information;
    }

    @Override
    public ContactInformationEntity toService() {
        ContactInformationEntity entity = new ContactInformationEntity();
        entity.setId(this.getId());
        entity.setContactType(this.getContactType());
        entity.setInformation(this.getInformation());
        return entity;
    }
}
