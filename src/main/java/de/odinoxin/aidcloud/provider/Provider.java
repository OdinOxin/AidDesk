package de.odinoxin.aidcloud.provider;

import de.odinoxin.aidcloud.service.ConcurrentFault_Exception;
import de.odinoxin.aiddesk.controls.refbox.RefBoxListItem;
import de.odinoxin.aiddesk.plugins.RecordEditor;
import de.odinoxin.aiddesk.plugins.RecordItem;

import java.util.List;

public interface Provider<T extends RecordItem> {
    public abstract T get(int id);

    public abstract T save(T item, T original) throws ConcurrentFault_Exception;

    public abstract boolean delete(int id);

    public abstract RefBoxListItem<T> getRefBoxItem(T item);

    public abstract List<RefBoxListItem<T>> search(List<String> expr, int max);

    public abstract RecordEditor<T> openEditor(T entity);
}
