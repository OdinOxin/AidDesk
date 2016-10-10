package de.odinoxin.aiddesk.plugins;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class RecordItem implements Cloneable {

    private BooleanProperty changed = new SimpleBooleanProperty(this, "changed", false);

    @Override
    protected Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public boolean isChanged() {
        return changed.get();
    }

    public void setChanged(boolean changed) {
        this.changed.set(changed);
    }

    public BooleanProperty changedProperty() {
        return changed;
    }
}
