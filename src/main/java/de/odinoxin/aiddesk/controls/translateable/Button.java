package de.odinoxin.aiddesk.controls.translateable;

import de.odinoxin.aidcloud.provider.TranslatorProvider;
import javafx.scene.Node;

/**
 * Button, translating itself by users selected language.
 */
public class Button extends javafx.scene.control.Button {

    public Button() {
        super();
        this.init();
    }

    public Button(String text) {
        super(text);
        this.init();
    }

    public Button(String text, Node graphic) {
        super(text, graphic);
        this.init();
    }

    private void init() {
        this.textProperty().addListener((observable, oldValue, newValue) -> loadTranslation());
    }

    private void loadTranslation() {
        this.setText(TranslatorProvider.getTranslation(this.getText()));
    }
}
