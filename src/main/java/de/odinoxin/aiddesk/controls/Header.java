package de.odinoxin.aiddesk.controls;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

import java.io.IOException;

public class Header extends HBox {

    @FXML
    private Text txtHeader;

    private StringProperty txtProperty = new SimpleStringProperty(this, "text");

    public Header() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/controls/header.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
            this.txtProperty.addListener((observable, oldValue, newValue) -> this.txtHeader.setText(newValue));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public String getText() {
        return this.txtProperty.get();
    }

    public void setText(String text) {
        this.txtProperty.set(text);
    }

    public StringProperty txtProperty() {
        return this.txtProperty;
    }
}
