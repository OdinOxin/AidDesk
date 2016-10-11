package de.odinoxin.aiddesk.controls.translateable;

import javafx.beans.property.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.HBox;

import java.io.IOException;

public class Label extends HBox {

    private StringProperty text = new SimpleStringProperty(this, "text", null);
    private BooleanProperty required = new SimpleBooleanProperty(this, "required", false);

    @FXML
    private javafx.scene.control.Label lblText;
    @FXML
    private javafx.scene.control.Label lblRequired;

    public Label() {
        super();
        this.init();
    }

    public Label(String text) {
        this();
        this.setText(text);
    }

    private void init() {

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/controls/label.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        this.text.addListener((observable, oldValue, newValue) ->
        {
            if (newValue != null) {
                String translation = Translator.getTranslation(newValue);
                if (translation != null)
                    this.lblText.setText(translation);
                else
                    this.lblText.setText(newValue);
            }
        });
        this.required.addListener((observable, oldValue, newValue) ->
        {
            this.lblRequired.setVisible(newValue);
            this.lblRequired.setManaged(newValue);
        });
        this.lblRequired.setVisible(this.isRequired());
        this.lblRequired.setManaged(this.isRequired());
    }

    public String getText() {
        return text.get();
    }

    public boolean isRequired() {
        return required.get();
    }

    public void setText(String text) {
        this.text.set(text);
    }

    public void setRequired(boolean required) {
        this.required.set(required);
    }

    public StringProperty textProperty() {
        return text;
    }

    public BooleanProperty requiredProperty() {
        return required;
    }
}
