package de.odinoxin.aiddesk.controls.translateable;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.Node;

public class Label extends javafx.scene.control.Label {

    private IntegerProperty textId = new SimpleIntegerProperty(this, "textId", 0);

    public Label() {
        super();
        this.init();
    }

    public Label(String text) {
        super(text);
        this.init();
    }

    public Label(String text, Node graphic) {
        super(text, graphic);
        this.init();
    }

    private void init() {
        this.textIdProperty().addListener((observable, oldValue, newValue) -> loadTranslation());
        this.textProperty().addListener((observable, oldValue, newValue) -> loadTranslation());
    }

    private void loadTranslation() {
        if (this.getTextId() != 0) {
            String translation = Translator.getTranslation(this.getTextId());
            if (translation != null)
                this.setText(translation);
        }
    }

    public int getTextId() {
        return textId.get();
    }

    public void setTextId(int textId) {
        this.textId.set(textId);
    }

    public IntegerProperty textIdProperty() {
        return textId;
    }
}
