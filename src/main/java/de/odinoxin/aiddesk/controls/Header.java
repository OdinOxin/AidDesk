package de.odinoxin.aiddesk.controls;

import de.odinoxin.aiddesk.controls.translateable.Label;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.HBox;

import java.io.IOException;

public class Header extends HBox {

    @FXML
    private Label lblHeader;

    private StringProperty txtProperty = new SimpleStringProperty(this, "text");
    private IntegerProperty textId = new SimpleIntegerProperty(this, "textId", 0);

    public Header() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/controls/header.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
            this.txtProperty.addListener((observable, oldValue, newValue) -> this.lblHeader.setText(newValue));
            this.textId.addListener((observable, oldValue, newValue) -> this.lblHeader.setTextId((int) newValue));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public String getText() {
        return this.txtProperty.get();
    }

    public int getTextId() {
        return textId.get();
    }

    public void setText(String text) {
        this.txtProperty.set(text);
    }

    public void setTextId(int textId) {
        this.textId.set(textId);
    }

    public StringProperty txtProperty() {
        return this.txtProperty;
    }

    public IntegerProperty textIdProperty() {
        return textId;
    }
}
