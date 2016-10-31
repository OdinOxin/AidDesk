package de.odinoxin.aiddesk.plugins;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;

public abstract class RecordItem<T> implements Cloneable {

    private IntegerProperty id = new SimpleIntegerProperty(this, "id", 0);
    private BooleanProperty changed = new SimpleBooleanProperty(this, "changed", false);

    public RecordItem() {
        idProperty().addListener((observable, oldValue, newValue) -> setChanged(true));
    }

    @Override
    protected Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public int getId() {
        return id.get();
    }

    public boolean isChanged() {
        return changed.get();
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public void setChanged(boolean changed) {
        this.changed.set(changed);
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public BooleanProperty changedProperty() {
        return changed;
    }

    public abstract T toEntity();
}
