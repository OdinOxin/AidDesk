package de.odinoxin.aiddesk.controls.reflist;

import de.odinoxin.aidcloud.provider.Provider;
import de.odinoxin.aiddesk.controls.refbox.RefBox;
import de.odinoxin.aiddesk.plugins.RecordItem;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.util.List;

/**
 * {@link ListCell} for {@link RefList}.
 *
 * @param <T> The type of the record.
 */
class RefListCell<T extends RecordItem> extends HBox {

    private List<T> source;
    private int index;
    @FXML
    private RefBox<T> refBox;
    @FXML
    private Button btnRemove;

    /**
     * Initializes the {@link RefListCell} and its behavior.
     *
     * @param provider The provider to use.
     * @param source   The source to display an item from.
     * @param index    The index of the item to display.
     */
    RefListCell(Provider<T> provider, List<T> source, int index) {
        this.source = source;
        this.index = index;

        FXMLLoader fxmlLoader = new FXMLLoader(RefListCell.class.getResource("/controls/reflistcell.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        this.btnRemove.setOnAction(e -> this.source.remove(this.index));
        this.refBox.setProvider(provider);
        this.refBox.setShowEditButton(true);

        this.update(this.index);

        this.refBox.recordProperty().addListener((observable, oldValue, newValue) -> {
            if (this.index < this.source.size())
                this.source.set(this.index, newValue);
            else if (newValue != null)
                this.source.add(newValue);
        });
    }

    /**
     * Updates the {@link RefListCell}.
     *
     * @param index The (maybe new) index to reference.
     */
    void update(int index) {
        this.index = index;
        boolean pseudo = this.index == this.source.size();
        this.btnRemove.setVisible(!pseudo);
        this.btnRemove.setDisable(pseudo);
        if (!pseudo)
            this.refBox.setRecord(this.source.get(this.index));
        else
            this.refBox.setRecord(null);
        this.refBox.setShowNewButton(pseudo);
    }

    /**
     * Sets the provider to use.
     *
     * @param provider The provider to use.
     */
    public void setProvider(Provider<T> provider) {
        this.refBox.setProvider(provider);
    }
}
