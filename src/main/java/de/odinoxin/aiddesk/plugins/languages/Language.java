package de.odinoxin.aiddesk.plugins.languages;

import de.odinoxin.aidcloud.service.LanguageEntity;
import de.odinoxin.aiddesk.plugins.RecordItem;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Language extends RecordItem<LanguageEntity> {

    private StringProperty name = new SimpleStringProperty();
    private StringProperty code = new SimpleStringProperty();

    public Language() {
        super();
        this.name.addListener((observable, oldValue, newValue) -> this.setChanged(true));
        this.code.addListener((observable, oldValue, newValue) -> this.setChanged(true));
        this.setChanged(false);
    }

    public Language(int id) {
        this();
        this.setId(id);
        this.setChanged(false);
    }

    public Language(int id, String name, String code) {
        this(id);
        this.setName(name);
        this.setCode(code);
        this.setChanged(false);
    }

    public String getName() {
        return name.get();
    }

    public String getCode() {
        return code.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public void setCode(String code) {
        this.code.set(code);
    }

    public StringProperty nameProperty() {
        return name;
    }

    public StringProperty codeProperty() {
        return code;
    }

    @Override
    public LanguageEntity toService() {
        LanguageEntity entity = new LanguageEntity();
        entity.setId(this.getId());
        entity.setName(this.getName());
        entity.setCode(this.getCode());
        return entity;
    }
}
