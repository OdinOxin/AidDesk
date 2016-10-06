package de.odinoxin.aiddesk.controls;

import de.odinoxin.aiddesk.Database;
import javafx.beans.property.*;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RefBox extends HBox {

    @FXML
    private TextField txfRefBox;
    @FXML
    private Button btnRefBox;
    private RefBoxList refBoxList;

    private IntegerProperty refProperty = new SimpleIntegerProperty(this, "ref");
    private StringProperty viewProperty = new SimpleStringProperty(this, "view");

    private boolean ignoreTextChange;

    public RefBox() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/controls/refbox.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        this.txfRefBox.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) ->
        {
            if (this.ignoreTextChange || viewProperty.get() == null)
                return;

            this.txfRefBox.setStyle("-fx-text-fill: black");

            try {
                String[] highlight = this.txfRefBox.getText().isEmpty() ? null : this.txfRefBox.getText().split(" ");
                String dbViewSelect = "SELECT * FROM " + this.viewProperty.get();
                if (highlight != null && highlight.length > 0) {
                    dbViewSelect += " WHERE";
                    for (int i = 0; i < highlight.length; i++) {
                        dbViewSelect += " ID LIKE ? OR Text LIKE ?";

                        if (i < highlight.length - 1)
                            dbViewSelect += " OR";
                    }
                }
                PreparedStatement stmt = Database.DB.prepareStatement(dbViewSelect);
                if (highlight != null)
                    for (int i = 0; i < highlight.length; i++) {
                        stmt.setString(i * 2 + 1, "%" + highlight[i] + "%");
                        stmt.setString(i * 2 + 2, "%" + highlight[i] + "%");
                    }
                ResultSet dbRes = stmt.executeQuery();
                if (this.refBoxList != null)
                    this.refBoxList.hide();
                this.refBoxList = new RefBoxList(this.localToScreen(0, this.getHeight()));
                this.refBoxList.setPrefWidth(this.getWidth());
                this.refBoxList.getSuggestionsList().setCellFactory(param -> new RefBoxListItemCell());

                while (dbRes.next()) {
                    RefBoxListItem item = new RefBoxListItem(dbRes.getInt("ID"), dbRes.getString("Text"), highlight);
                    this.refBoxList.getSuggestionsList().getItems().add(item);
                }
                if (this.refBoxList.getSuggestionsList().getItems().size() > 0) {
                    this.refBoxList.getSuggestionsList().setOnKeyPressed(ev ->
                    {
                        if (ev.getCode() == KeyCode.ENTER) {
                            RefBoxListItem item = RefBox.this.refBoxList.getSuggestionsList().getSelectionModel().getSelectedItem();
                            RefBox.this.setSelected(item);
                        }
                        else if(ev.getCode() == KeyCode.ESCAPE)
                            this.refBoxList.hide();
                    });
                    this.refBoxList.getSuggestionsList().setOnMouseClicked(ev ->
                    {
                        if (ev.getButton() == MouseButton.PRIMARY && ev.getClickCount() == 2) {
                            RefBoxListItem item = RefBox.this.refBoxList.getSuggestionsList().getSelectionModel().getSelectedItem();
                            RefBox.this.setSelected(item);
                        }
                    });
                    this.refBoxList.getSuggestionsList().getSelectionModel().selectFirst();
                    this.refBoxList.show(this.getScene().getWindow());
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });
    }

    public String getText() {
        return this.txfRefBox.getText();
    }

    public void setText(String text) {
        this.txfRefBox.setText(text);
    }

    public StringProperty textProperty() {
        return this.txfRefBox.textProperty();
    }

    public int getRef() {
        return this.refProperty.get();
    }

    public void setRef(int ref) {
        this.refProperty.set(ref);
    }

    public IntegerProperty refProperty() {
        return this.refProperty;
    }

    public String getView() {
        return this.viewProperty.get();
    }

    public void setView(String view) {
        this.viewProperty.set(view);
    }

    public StringProperty viewProperty() {
        return this.viewProperty;
    }

    public final void setOnAction(EventHandler<ActionEvent> value) {
        this.txfRefBox.onActionProperty().set(value);
    }

    public void setSelected(RefBoxListItem item) {
        RefBox.this.ignoreTextChange = true;
        if (item != null) {
            this.setRef(item.getId());
            this.setText(String.format("%d - %s", item.getId(), item.getText()));
            this.txfRefBox.setStyle("-fx-text-fill: green");
        } else {
            this.setRef(0);
            this.setText(null);
            this.txfRefBox.setStyle("-fx-text-fill: black");
        }
        this.refBoxList.hide();
        RefBox.this.ignoreTextChange = false;
    }
}
