package de.odinoxin.aidcloud.provider;

import de.odinoxin.aiddesk.controls.refbox.RefBoxListItem;
import de.odinoxin.aiddesk.plugins.RecordItem;

import java.util.List;

public interface Provider<T extends RecordItem> {
    public abstract List<RefBoxListItem<T>> search(String[] expr);
}
