package de.odinoxin.aiddesk.plugins;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;

import java.lang.reflect.ParameterizedType;

/**
 * Base class for RecordItems.
 * @param <T> Class of the related service entity class.
 */
public abstract class RecordItem<T> implements Cloneable {

    /**
     * ID of the RecordItem.
     */
    private IntegerProperty id = new SimpleIntegerProperty(this, "id", 0);
    /**
     * Property to indicate, whether any property changed.
     */
    private BooleanProperty changed = new SimpleBooleanProperty(this, "changed", false);

    public RecordItem() {
        idProperty().addListener((observable, oldValue, newValue) -> setChanged(true));
    }

    /**
     * Clones the RecordItem
     * @return a clone of the RecordItem
     */
    @Override
    protected abstract Object clone();

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

    /**
     * Convertes the RecordItem into the related service entity class.
     * @return the converted entity
     */
    public abstract T toEntity();
}
