package de.odinoxin.aiddesk.plugins.addresses;

import de.odinoxin.aiddesk.plugins.RecordItem;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Address extends RecordItem {

    private StringProperty street = new SimpleStringProperty();
    private StringProperty hsNo = new SimpleStringProperty();
    private StringProperty zip = new SimpleStringProperty();
    private StringProperty city = new SimpleStringProperty();
    private IntegerProperty country = new SimpleIntegerProperty();

    public Address() {
        super();
        this.street.addListener((observable, oldValue, newValue) -> setChanged(true));
        this.hsNo.addListener((observable, oldValue, newValue) -> setChanged(true));
        this.zip.addListener((observable, oldValue, newValue) -> setChanged(true));
        this.city.addListener((observable, oldValue, newValue) -> setChanged(true));
        this.country.addListener((observable, oldValue, newValue) ->
        {
            if ((int) newValue != 0)
                setChanged(true);
        });
        this.setChanged(false);
    }

    public Address(int id) {
        this();
        this.setId(id);
        this.setChanged(false);
    }

    public Address(int id, String street, String hsNo, String zip, String city, int country) {
        this(id);
        this.setStreet(street);
        this.setHsNo(hsNo);
        this.setZip(zip);
        this.setCity(city);
        this.setCountry(country);
        this.setChanged(true);
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

    public int getCountry() {
        return country.get();
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

    public void setCountry(int country) {
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

    public IntegerProperty countryProperty() {
        return country;
    }
}
