package de.odinoxin.aiddesk.controls.reflist;

import de.odinoxin.aidcloud.provider.Provider;
import de.odinoxin.aiddesk.controls.refbox.RefBox;
import de.odinoxin.aiddesk.plugins.RecordItem;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.util.List;

class RefListCell<T extends RecordItem> extends HBox {

    private List<T> source;
    private int index;
    private RefBox<T> refBox;
    private Provider<T> provider;
    private Button btnRemove;

    RefListCell(Provider<T> provider, List<T> source, int index) {
        this.provider = provider;
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

        this.btnRemove = (Button) this.lookup("#btnRemove");
        this.btnRemove.setOnAction(e -> this.source.remove(this.index));
        this.refBox = (RefBox<T>) this.lookup("#refBox");
        this.refBox.setProvider(this.provider);
        this.refBox.setShowEditButton(true);

        this.update(this.index);

        this.refBox.objProperty().addListener((observable, oldValue, newValue) -> {
            if (this.index < this.source.size())
                this.source.set(this.index, newValue);
            else if (newValue != null)
                this.source.add(newValue);
        });
    }

    public void update(int index) {
        this.index = index;
        boolean pseudo = this.index == this.source.size();
        this.btnRemove.setVisible(!pseudo);
        this.btnRemove.setDisable(pseudo);
        if (!pseudo)
            this.refBox.setObj(this.source.get(this.index));
        else
            this.refBox.setObj(null);
        this.refBox.setShowNewButton(pseudo);
    }

    public void setProvider(Provider<T> provider) {
        this.provider = provider;
    }
}
