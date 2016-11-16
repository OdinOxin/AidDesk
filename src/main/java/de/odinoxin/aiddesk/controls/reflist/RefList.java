package de.odinoxin.aiddesk.controls.reflist;

import de.odinoxin.aidcloud.provider.Provider;
import de.odinoxin.aiddesk.plugins.RecordItem;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.layout.VBox;

public class RefList<T extends RecordItem> extends VBox {

    private ObservableList<T> items;
    private Provider<T> provider;

    public RefList() {
        this.setSpacing(5);
        this.bindBidirectional(FXCollections.observableArrayList());
    }

    public void setProvider(Provider<T> provider) {
        this.provider = provider;
        for (int i = 0; i < this.getChildren().size(); i++)
            ((RefListCell<T>) this.getChildren().get(i)).setProvider(provider);
    }

    public T get(int index) {
        if (index < 0 || index >= this.items.size())
            return null;
        return this.items.get(index);
    }

    public void add(T item) {
        this.items.add(item);
    }

    public void set(int index, T item) {
        this.items.set(index, item);
    }

    public void bindBidirectional(ObservableList<T> items) {
        this.items = items;
        this.items.addListener((ListChangeListener.Change<? extends T> c) -> {
            if (c.next()) {
                if (!c.wasReplaced() && c.wasAdded()) {
                    this.getChildren().add(this.getChildren().size() - 1, new RefListCell<T>(this.provider, this.items, this.items.size() - 1));
                    ((RefListCell<T>) this.getChildren().get(this.getChildren().size() - 1)).update(this.items.size());
                } else if (!c.wasReplaced() && c.wasRemoved()) {
                    this.getChildren().remove(c.getFrom(), c.getFrom() + c.getRemovedSize());
                    for (int i = 0; i < this.getChildren().size(); i++)
                        ((RefListCell<T>) this.getChildren().get(i)).update(i);
                }
            }
        });
        this.getChildren().clear();
        for (int i = 0; i < this.items.size(); i++)
            this.getChildren().add(new RefListCell<T>(this.provider, this.items, i));
        this.getChildren().add(new RefListCell<T>(this.provider, this.items, this.items.size()));
    }
}
