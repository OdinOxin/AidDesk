package de.odinoxin.aiddesk.controls;

import de.odinoxin.aiddesk.Database;
import javafx.beans.property.*;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;

public class RefBox extends StackPane {

    @FXML
    private TextField txfRefBox;
    @FXML
    private Button btnRefBox;
    @FXML
    private Label lblDetails;

    private RefBoxList refBoxList;

    private IntegerProperty ref = new SimpleIntegerProperty(this, "ref", 0);
    private StringProperty view = new SimpleStringProperty(this, "view");
    private BooleanProperty detailsVisible = new SimpleBooleanProperty(this, "detailsVisible", false);

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

        this.txfRefBox.setOnKeyPressed(ev ->
        {
            if (ev.getCode() == KeyCode.DOWN)
                this.search();
        });
        this.txfRefBox.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) ->
        {
            if (this.ignoreTextChange || view.get() == null)
                return;
            this.txfRefBox.setStyle("-fx-text-fill: black");
            this.search();
        });
        this.detailsVisible.addListener((observable, oldValue, newValue) ->
        {
            this.lblDetails.setVisible(newValue);
            this.lblDetails.setManaged(newValue);
        });
        this.lblDetails.setVisible(this.isDetailsVisible());
        this.lblDetails.setManaged(this.isDetailsVisible());
        this.setSelected();
        this.btnRefBox.setOnAction(ev ->
        {
            this.txfRefBox.requestFocus();
            this.search();
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
        return this.ref.get();
    }

    public void setRef(int ref) {
        this.ref.set(ref);
    }

    public IntegerProperty refProperty() {
        return this.ref;
    }

    public String getView() {
        return this.view.get();
    }

    public void setView(String view) {
        this.view.set(view);
    }

    public StringProperty viewProperty() {
        return this.view;
    }

    public boolean isDetailsVisible() {
        return detailsVisible.get();
    }

    public void setDetailsVisible(boolean detailsVisible) {
        this.detailsVisible.set(detailsVisible);
    }

    public BooleanProperty detailsVisible() {
        return this.detailsVisible;
    }

    public final void setOnAction(EventHandler<ActionEvent> value) {
        this.txfRefBox.onActionProperty().set(value);
    }

    public void setSelected() {
        RefBox.this.ignoreTextChange = true;
        RefBoxListItem item = null;
        if (this.refBoxList != null)
            item = RefBox.this.refBoxList.getSuggestionsList().getSelectionModel().getSelectedItem();
        if (item != null) {
            this.setRef(item.getId());
            this.setText(String.format("%d - %s", item.getId(), item.getText()));
            this.txfRefBox.setStyle("-fx-text-fill: green");
            this.lblDetails.setText(item.getSubText());
        } else {
            this.setRef(0);
            this.setText(null);
            this.txfRefBox.setStyle("-fx-text-fill: black");
            this.lblDetails.setText("");
        }
        if (this.refBoxList != null)
            this.refBoxList.hide();
        RefBox.this.ignoreTextChange = false;
    }

    private void search() {
        try {
            String[] highlight = this.txfRefBox.getText().isEmpty() ? null : this.txfRefBox.getText().split(" ");
            String dbViewSelect = "SELECT * FROM " + this.view.get();
            if (highlight != null && highlight.length > 0) {
                dbViewSelect += " WHERE";
                for (int i = 0; i < highlight.length; i++) {
                    dbViewSelect += " ID LIKE ? OR Text LIKE ? OR SubText LIKE ?";

                    if (i < highlight.length - 1)
                        dbViewSelect += " OR";
                }
            }
            PreparedStatement stmt = Database.DB.prepareStatement(dbViewSelect);
            if (highlight != null)
                for (int i = 0; i < highlight.length; i++) {
                    stmt.setString(i * 3 + 1, "%" + highlight[i] + "%");
                    stmt.setString(i * 3 + 2, "%" + highlight[i] + "%");
                    stmt.setString(i * 3 + 3, "%" + highlight[i] + "%");
                }
            ResultSet dbRes = stmt.executeQuery();
            if (this.refBoxList != null)
                this.refBoxList.hide();
            this.refBoxList = new RefBoxList(this.localToScreen(0, this.txfRefBox.getHeight()));
            this.refBoxList.setPrefWidth(this.getWidth());
            this.refBoxList.getSuggestionsList().setCellFactory(param -> new RefBoxListItemCell());


            while (dbRes.next()) {
                RefBoxListItem item = new RefBoxListItem(dbRes.getInt("ID"), dbRes.getString("Text"), dbRes.getString("SubText"), highlight);
                this.refBoxList.getSuggestionsList().getItems().add(item);
            }
            if (this.refBoxList.getSuggestionsList().getItems().size() > 0) {
                this.refBoxList.getSuggestionsList().setOnKeyPressed(ev ->
                {
                    switch (ev.getCode()) {
                        case TAB:
                            this.btnRefBox.requestFocus();
                        case ENTER:
                            RefBox.this.setSelected();
                            break;
                        case ESCAPE:
                            this.refBoxList.hide();
                            break;
                    }
                });
                this.refBoxList.getSuggestionsList().setOnMouseClicked(ev ->
                {
                    if (ev.getButton() == MouseButton.PRIMARY && ev.getClickCount() == 2)
                        RefBox.this.setSelected();
                });
                for (RefBoxListItem item : this.refBoxList.getSuggestionsList().getItems()) {
                    item.matchProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) ->
                    {
                        Collections.sort(RefBox.this.refBoxList.getSuggestionsList().getItems(), (RefBoxListItem item1, RefBoxListItem item2) ->
                        {
                            if (item1.getMatch() < item2.getMatch())
                                return 1;
                            else if (item1.getMatch() == item2.getMatch())
                                return 0;
                            return -1;
                        });
                        RefBox.this.refBoxList.getSuggestionsList().getSelectionModel().selectFirst();
                    });
                }

                this.refBoxList.show(this.getScene().getWindow());
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
