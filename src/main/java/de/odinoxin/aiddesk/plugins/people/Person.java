package de.odinoxin.aiddesk.plugins.people;

import de.odinoxin.aiddesk.plugins.RecordItem;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Person extends RecordItem {

    private IntegerProperty id = new SimpleIntegerProperty();
    private StringProperty name = new SimpleStringProperty();
    private StringProperty forename = new SimpleStringProperty();
    private StringProperty code = new SimpleStringProperty();
    private StringProperty languageCode = new SimpleStringProperty();
    private IntegerProperty addressId = new SimpleIntegerProperty();

    public Person() {
        idProperty().addListener((observable, oldValue, newValue) -> setChanged(true));
        nameProperty().addListener((observable, oldValue, newValue) -> setChanged(true));
        forenameProperty().addListener((observable, oldValue, newValue) -> setChanged(true));
        codeProperty().addListener((observable, oldValue, newValue) -> setChanged(true));
        languageCodeProperty().addListener((observable, oldValue, newValue) -> setChanged(true));
        addressIdProperty().addListener((observable, oldValue, newValue) ->
        {
            if ((int) newValue != 0)
                setChanged(true);
        });
    }

    public Person(int id) {
        this();
        this.setId(id);
        this.setChanged(false);
    }

    public Person(int id, String name, String forename, String code, String languageCode, int addressId) {
        this(id);
        this.setName(name);
        this.setForename(forename);
        this.setCode(code);
        this.setLanguageCode(languageCode);
        this.setAddressId(addressId);
        this.setChanged(false);
    }

    @Override
    protected Object clone() {
        return new Person(this.getId(), this.getName(), this.getForename(), this.getCode(), this.getLanguageCode(), this.getAddressId());
    }

    public int getId() {
        return id.get();
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

    public String getLanguageCode() {
        return languageCode.get();
    }

    public int getAddressId() {
        return addressId.get();
    }

    public void setId(int id) {
        this.id.set(id);
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

    public void setLanguageCode(String languageCode) {
        this.languageCode.set(languageCode);
    }

    public void setAddressId(int addressId) {
        this.addressId.set(addressId);
    }

    public IntegerProperty idProperty() {
        return id;
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

    public StringProperty languageCodeProperty() {
        return languageCode;
    }

    public IntegerProperty addressIdProperty() {
        return addressId;
    }
}
