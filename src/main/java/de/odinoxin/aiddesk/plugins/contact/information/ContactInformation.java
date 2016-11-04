package de.odinoxin.aiddesk.plugins.contact.information;

import de.odinoxin.aidcloud.service.ContactInformationEntity;
import de.odinoxin.aiddesk.plugins.RecordItem;
import de.odinoxin.aiddesk.plugins.contact.types.ContactType;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ContactInformation extends RecordItem<ContactInformationEntity> {

    private ObjectProperty<ContactType> contactType = new SimpleObjectProperty<>();
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

    public ContactInformation(int id, ContactType contactType, String information) {
        this(id);
        this.setContactType(contactType);
        this.setInformation(information);
        this.setChanged(false);
    }

    public ContactInformation(ContactInformationEntity entity) {
        this(entity.getId(), entity.getContactType() == null ? null : new ContactType(entity.getContactType()), entity.getInformation());
    }

    @Override
    protected Object clone() {
        return new ContactInformation(this.getId(), this.getContactType(), this.getInformation());
    }

    public ContactType getContactType() {
        return contactType.get();
    }

    public String getInformation() {
        return information.get();
    }

    public void setContactType(ContactType contactType) {
        this.contactType.set(contactType);
    }

    public void setInformation(String information) {
        this.information.set(information);
    }

    public ObjectProperty<ContactType> contactTypeProperty() {
        return contactType;
    }

    public StringProperty informationProperty() {
        return information;
    }

    @Override
    public ContactInformationEntity toEntity() {
        ContactInformationEntity entity = new ContactInformationEntity();
        entity.setId(this.getId());
        entity.setContactType(this.getContactType() == null ? null : this.getContactType().toEntity());
        entity.setInformation(this.getInformation());
        return entity;
    }
}
