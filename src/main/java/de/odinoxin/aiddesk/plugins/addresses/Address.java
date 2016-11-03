package de.odinoxin.aiddesk.plugins.addresses;

import de.odinoxin.aidcloud.service.AddressEntity;
import de.odinoxin.aiddesk.plugins.RecordItem;
import de.odinoxin.aiddesk.plugins.countries.Country;
import javafx.beans.property.*;

public class Address extends RecordItem<AddressEntity> {

    private StringProperty street = new SimpleStringProperty();
    private StringProperty hsNo = new SimpleStringProperty();
    private StringProperty zip = new SimpleStringProperty();
    private StringProperty city = new SimpleStringProperty();
    private ObjectProperty<Country> country = new SimpleObjectProperty<>();
    private ReadOnlyIntegerWrapper countryId = new ReadOnlyIntegerWrapper();

    public Address() {
        super();
        this.street.addListener((observable, oldValue, newValue) -> setChanged(true));
        this.hsNo.addListener((observable, oldValue, newValue) -> setChanged(true));
        this.zip.addListener((observable, oldValue, newValue) -> setChanged(true));
        this.city.addListener((observable, oldValue, newValue) -> setChanged(true));
        this.country.addListener((observable, oldValue, newValue) -> {
            setChanged(true);
            if (this.countryId.isBound())
                this.countryId.unbind();
            this.countryId.bind(this.getCountry().idProperty());
        });
        this.setChanged(false);
    }

    public Address(int id) {
        this();
        this.setId(id);
        this.setChanged(false);
    }

    public Address(int id, String street, String hsNo, String zip, String city, Country country) {
        this(id);
        this.setStreet(street);
        this.setHsNo(hsNo);
        this.setZip(zip);
        this.setCity(city);
        this.setCountry(country);
        this.setChanged(true);
    }

    public Address(AddressEntity entity) {
        this(entity.getId(), entity.getStreet(), entity.getHsNo(), entity.getZip(), entity.getCity(), new Country(entity.getCountry()));
    }

    @Override
    protected Object clone() {
        return new Address(this.getId(), this.getStreet(), this.getHsNo(), this.getZip(), this.getCity(), this.getCountry());
    }

    public String getStreet() {
        return street.get();
    }

    public String getHsNo() {
        return hsNo.get();
    }

    public String getZip() {
        return zip.get();
    }

    public String getCity() {
        return city.get();
    }

    public Country getCountry() {
        return country.get();
    }

    public ReadOnlyIntegerProperty countryIdProperty() {
        return this.countryId.getReadOnlyProperty();
    }

    public void setStreet(String street) {
        this.street.set(street);
    }

    public void setHsNo(String hsNo) {
        this.hsNo.set(hsNo);
    }

    public void setZip(String zip) {
        this.zip.set(zip);
    }

    public void setCity(String city) {
        this.city.set(city);
    }

    public void setCountry(Country country) {
        this.country.set(country);
    }

    public StringProperty streetProperty() {
        return street;
    }

    public StringProperty hsNoProperty() {
        return hsNo;
    }

    public StringProperty zipProperty() {
        return zip;
    }

    public StringProperty cityProperty() {
        return city;
    }

    public ObjectProperty<Country> countryProperty() {
        return country;
    }

    @Override
    public AddressEntity toEntity() {
        AddressEntity entity = new AddressEntity();
        entity.setId(this.getId());
        entity.setStreet(this.getStreet());
        entity.setHsNo(this.getHsNo());
        entity.setZip(this.getZip());
        entity.setCity(this.getCity());
        entity.setCountry(this.getCountry() == null ? null : this.getCountry().toEntity());
        return entity;
    }
}
