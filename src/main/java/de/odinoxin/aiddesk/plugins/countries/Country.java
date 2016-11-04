package de.odinoxin.aiddesk.plugins.countries;

import de.odinoxin.aidcloud.service.CountryEntity;
import de.odinoxin.aiddesk.plugins.RecordItem;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Country extends RecordItem<CountryEntity> {
    private StringProperty alpha2 = new SimpleStringProperty();
    private StringProperty alpha3 = new SimpleStringProperty();
    private StringProperty name = new SimpleStringProperty();
    private StringProperty areaCode = new SimpleStringProperty();

    public Country() {
        super();
        this.alpha2.addListener((observable, oldValue, newValue) -> this.setChanged(true));
        this.alpha3.addListener((observable, oldValue, newValue) -> this.setChanged(true));
        this.name.addListener((observable, oldValue, newValue) -> this.setChanged(true));
        this.areaCode.addListener((observable, oldValue, newValue) -> this.setChanged(true));
        this.setChanged(false);
    }

    public Country(int id) {
        this();
        this.setId(id);
        this.setChanged(false);
    }

    public Country(int id, String alpha2, String alpha3, String name, String areaCode) {
        this(id);
        this.setAlpha2(alpha2);
        this.setAlpha3(alpha3);
        this.setName(name);
        this.setAreaCode(areaCode);
        this.setChanged(false);
    }

    public Country(CountryEntity entity) {
        this(entity.getId(), entity.getAlpha2(), entity.getAlpha3(), entity.getName(), entity.getAreaCode());
    }

    @Override
    protected Object clone() {
        return new Country(this.getId(), this.getAlpha2(), this.getAlpha3(), this.getName(), this.getAreaCode());
    }

    public String getAlpha2() {
        return alpha2.get();
    }

    public String getAlpha3() {
        return alpha3.get();
    }

    public String getName() {
        return name.get();
    }

    public String getAreaCode() {
        return areaCode.get();
    }

    public void setAlpha2(String alpha2) {
        this.alpha2.set(alpha2);
    }

    public void setAlpha3(String alpha3) {
        this.alpha3.set(alpha3);
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public void setAreaCode(String areaCode) {
        this.areaCode.set(areaCode);
    }

    public StringProperty alpha2Property() {
        return alpha2;
    }

    public StringProperty alpha3Property() {
        return alpha3;
    }

    public StringProperty nameProperty() {
        return name;
    }

    public StringProperty areaCodeProperty() {
        return areaCode;
    }

    @Override
    public CountryEntity toEntity() {
        CountryEntity countryEntity = new CountryEntity();
        countryEntity.setId(this.getId());
        countryEntity.setAlpha2(this.getAlpha2());
        countryEntity.setAlpha3(this.getAlpha3());
        countryEntity.setName(this.getName());
        countryEntity.setAreaCode(this.getAreaCode());
        return countryEntity;
    }
}
