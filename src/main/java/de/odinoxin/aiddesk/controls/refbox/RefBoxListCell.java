package de.odinoxin.aiddesk.controls.refbox;

import de.odinoxin.aidcloud.provider.Provider;
import de.odinoxin.aiddesk.plugins.RecordItem;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListCell;

import java.io.IOException;
import java.util.List;

public class RefBoxListCell<T extends RecordItem> extends ListCell<T> {

    private Provider<T> provider;
    private RefBox<T> refBox;
    private List<T> list;

    public RefBoxListCell(Provider<T> provider, List<T> list) {
        this.provider = provider;
        this.list = list;
    }

    @Override
    protected void updateItem(T item, boolean empty) {
        super.updateItem(item, empty);

        if (!empty) {
            try {
                this.refBox = FXMLLoader.load(RefBoxListCell.class.getResource("/controls/refboxlistcell.fxml"));
                this.refBox.setProvider(this.provider);
                this.refBox.setShowEditButton(true);
                this.refBox.setObj(item);
                if (this.getIndex() == this.list.size())
                    this.refBox.setShowNewButton(true);
                this.refBox.objProperty().addListener((observable, oldValue, newValue) -> {
                    if (this.getIndex() < this.list.size())
                        this.list.set(this.getIndex(), newValue);
                    else if (newValue != null)
                        this.list.add(newValue);
                });
                this.setGraphic(this.refBox);
                return;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        this.setText(null);
        this.setGraphic(null);
    }
}
