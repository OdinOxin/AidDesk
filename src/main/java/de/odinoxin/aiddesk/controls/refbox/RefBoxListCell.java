package de.odinoxin.aiddesk.controls.refbox;

import de.odinoxin.aidcloud.provider.Provider;
import de.odinoxin.aiddesk.plugins.RecordItem;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;

import java.util.List;

public class RefBoxListCell<T extends RecordItem> extends ListCell<T> {

    private Provider<T> provider;
    private Button btnRemove;
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
                HBox box = FXMLLoader.load(RefBoxListCell.class.getResource("/controls/refboxlistcell.fxml"));
                box.prefWidthProperty().bind(this.prefWidthProperty());

                this.btnRemove = (Button) box.lookup("#btnRemove");
                this.btnRemove.setOnAction(e -> {
                    this.list.remove(this.getIndex() - 1);
                    this.getListView().getItems().remove(this.getIndex());
                });

                this.refBox = (RefBox<T>) box.lookup("#refBox");
                this.refBox.setProvider(this.provider);
                this.refBox.setShowEditButton(true);
                this.refBox.setObj(item);
                this.refBox.objProperty().addListener((observable, oldValue, newValue) -> {
                    if (this.getIndex() > 0) {
                        this.list.set(this.getIndex() - 1, newValue);
                        this.getListView().getItems().set(this.getIndex(), newValue);
                    } else if (newValue != null) {
                        this.list.add(0, newValue);
                        this.getListView().getItems().add(1, newValue);
                    }
                });

                if (this.getIndex() == 0) {
                    this.btnRemove.setVisible(false);
                    this.btnRemove.setDisable(true);
                    this.refBox.setShowNewButton(true);
                }
                this.setGraphic(box);
                return;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        this.setText(null);
        this.setGraphic(null);
    }
}
