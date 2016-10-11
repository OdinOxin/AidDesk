package de.odinoxin.aiddesk.controls.refbox;

import de.odinoxin.aiddesk.Database;
import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.effect.Glow;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;

public class RefBox extends VBox {

    @FXML
    private TextField txfText;
    @FXML
    private HBox hbxButtons;
    @FXML
    private Button btnNew;
    @FXML
    private Button btnEdit;
    @FXML
    private Button btnSearch;
    @FXML
    private TextArea txfDetails;
    private RefBoxList refBoxList;

    private IntegerProperty ref = new SimpleIntegerProperty(this, "ref", 0);
    private StringProperty view = new SimpleStringProperty(this, "view");
    private BooleanProperty showNewButton = new SimpleBooleanProperty(this, "showNewButton", false);
    private BooleanProperty showEditButton = new SimpleBooleanProperty(this, "showEditButton", false);
    private BooleanProperty showDetails = new SimpleBooleanProperty(this, "showDetails", false);
    private IntegerProperty detailsRows = new SimpleIntegerProperty(this, "detailsRows", 2);
    private ObjectProperty<State> state = new SimpleObjectProperty<>();

    private boolean ignoreTextChange;
    private boolean keepText;

    public RefBox() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/controls/refbox.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        this.txfText.setOnKeyPressed(ev ->
        {
            if (ev.getCode() == KeyCode.DOWN)
                this.search();
        });
        this.txfText.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) ->
        {
            if (this.ignoreTextChange || view.get() == null)
                return;
            this.keepText = true;
            this.setRef(0);
            this.keepText = false;
            this.search();
        });
        this.ref.addListener((observable, oldValue, newValue) ->
        {
            try {
                PreparedStatement stmt = Database.DB.prepareStatement("SELECT * FROM " + this.view.get() + " WHERE ID = ?");
                stmt.setInt(1, (int) newValue);
                ResultSet dbRes = stmt.executeQuery();
                this.ignoreTextChange = true;
                if (dbRes.next()) {
                    this.setText(dbRes.getString("Text"));
                    this.state.set(State.LOGGED_IN);
                    this.txfDetails.setText(dbRes.getString("SubText"));
                } else {
                    this.state.set(State.SEARCHING);
                    if (!this.keepText)
                        this.txfText.setText("");
                    this.txfDetails.setText("");
                }
                this.ignoreTextChange = false;
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });
        this.state.addListener((observable, oldValue, newValue) ->
        {
            switch (newValue) {
                case LOGGED_IN:
                    this.txfText.setStyle("-fx-text-fill: black");
                    break;
                case NO_RESULTS:
                    this.txfText.setStyle("-fx-text-fill: red; -fx-font-weight: bold");
                    break;
                case SEARCHING:
                    this.txfText.setStyle("-fx-text-fill: orange; -fx-font-weight: bold");
                    break;
            }
        });

        this.hbxButtons.widthProperty().addListener((observable, oldValue, newValue) -> this.txfText.setPadding(new Insets(5, (double) newValue, 5, 5)));
        this.showNewButton.addListener((observable, oldValue, newValue) ->
        {
            this.btnNew.setVisible(newValue);
            this.btnNew.setManaged(newValue);
        });
        this.btnNew.minHeightProperty().bind(this.txfText.heightProperty());
        this.btnNew.maxHeightProperty().bind(this.txfText.heightProperty());
        this.btnNew.setOnKeyPressed(ev ->
        {
            switch (ev.getCode()) {
                case ENTER:
                    this.btnNew.fire();
                    ev.consume();
                    break;
                case DOWN:
                    this.search();
                    ev.consume();
                    break;
            }
        });
        this.btnNew.setVisible(this.isShowNewButton());
        this.btnNew.setManaged(this.isShowNewButton());
        this.btnNew.focusedProperty().addListener(this.getBtnHighlighter(this.btnNew));
        this.showEditButton.addListener((observable, oldValue, newValue) ->
        {
            this.btnEdit.setVisible(newValue);
            this.btnEdit.setManaged(newValue);
        });
        this.btnEdit.minHeightProperty().bind(this.txfText.heightProperty());
        this.btnEdit.maxHeightProperty().bind(this.txfText.heightProperty());
        this.btnEdit.setOnKeyPressed(ev ->
        {
            switch (ev.getCode()) {
                case ENTER:
                    this.btnNew.fire();
                    ev.consume();
                    break;
                case DOWN:
                    this.search();
                    ev.consume();
                    break;
            }
        });
        this.btnEdit.setVisible(this.isShowEditButton());
        this.btnEdit.setManaged(this.isShowEditButton());
        this.btnEdit.focusedProperty().addListener(this.getBtnHighlighter(this.btnEdit));
        this.btnSearch.minHeightProperty().bind(this.txfText.heightProperty());
        this.btnSearch.maxHeightProperty().bind(this.txfText.heightProperty());
        this.btnSearch.setOnKeyPressed(ev ->
        {
            switch (ev.getCode()) {
                case ENTER:
                case DOWN:
                    this.search();
                    ev.consume();
                    break;
            }
        });
        this.btnSearch.setOnAction(ev -> this.search());
        this.btnSearch.focusedProperty().addListener(this.getBtnHighlighter(this.btnSearch));

        this.showDetails.addListener((observable, oldValue, newValue) ->
        {
            this.txfDetails.setVisible(newValue);
            this.txfDetails.setManaged(newValue);
        });
        this.txfDetails.setVisible(this.isShowDetails());
        this.txfDetails.setManaged(this.isShowDetails());
        this.detailsRows.addListener((observable, oldValue, newValue) -> this.txfDetails.setPrefHeight((int) newValue * 20 + 15));
        this.txfDetails.setPrefHeight(this.getDetailsRows() * 20 + 15);

        this.setRef(0);
    }

    public String getText() {
        return this.txfText.getText();
    }

    public void setText(String text) {
        this.txfText.setText(text);
    }

    public StringProperty textProperty() {
        return this.txfText.textProperty();
    }

    public int getRef() {
        return this.ref.get();
    }

    public void setRef(int ref) {
        this.ref.set(ref);
        if (this.refBoxList != null)
            this.refBoxList.hide();
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

    public boolean isShowNewButton() {
        return showNewButton.get();
    }

    public void setShowNewButton(boolean showNewButton) {
        this.showNewButton.set(showNewButton);
    }

    public BooleanProperty showNewButton() {
        return showNewButton;
    }

    public void setOnNewAction(EventHandler<ActionEvent> eventHandler) {
        this.btnNew.setOnAction(eventHandler);
    }

    public boolean isShowEditButton() {
        return showEditButton.get();
    }

    public void setShowEditButton(boolean showEditButton) {
        this.showEditButton.set(showEditButton);
    }

    public BooleanProperty showEditButton() {
        return showEditButton;
    }

    public void setOnEditAction(EventHandler<ActionEvent> eventHandler) {
        this.btnEdit.setOnAction(eventHandler);
    }

    public boolean isShowDetails() {
        return showDetails.get();
    }

    public void setShowDetails(boolean showDetails) {
        this.showDetails.set(showDetails);
    }

    public BooleanProperty showDetails() {
        return this.showDetails;
    }

    public int getDetailsRows() {
        return detailsRows.get();
    }

    public void setDetailsRows(int detailsRows) {
        this.detailsRows.set(detailsRows);
    }

    public IntegerProperty detailsRowsProperty() {
        return detailsRows;
    }

    public final void setOnAction(EventHandler<ActionEvent> value) {
        this.txfText.onActionProperty().set(value);
    }

    private void search() {
        this.txfText.requestFocus();
        try {
            if (this.refBoxList != null)
                this.refBoxList.hide();
            this.refBoxList = new RefBoxList(this.localToScreen(0, this.txfText.getHeight()));
            this.refBoxList.setPrefWidth(this.getWidth());
            this.refBoxList.getSuggestionsList().setCellFactory(param -> new RefBoxListItemCell());

            String[] highlight = this.txfText.getText() == null || this.txfText.getText().isEmpty() ? null : this.txfText.getText().split(" ");
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
            while (dbRes.next()) {
                RefBoxListItem item = new RefBoxListItem(dbRes.getInt("ID"), dbRes.getString("Text"), dbRes.getString("SubText"), highlight);
                this.refBoxList.getSuggestionsList().getItems().add(item);
            }
            if (this.refBoxList.getSuggestionsList().getItems().size() > 0) {
                if (state.get() == State.NO_RESULTS)
                    this.state.set(State.SEARCHING);
                this.refBoxList.getSuggestionsList().setOnKeyPressed(ev ->
                {
                    switch (ev.getCode()) {
                        case TAB:
                            this.btnSearch.requestFocus();
                        case ENTER:
                            RefBoxListItem item = this.refBoxList.getSuggestionsList().getSelectionModel().getSelectedItem();
                            this.setRef(item == null ? 0 : item.getId());
                            break;
                        case ESCAPE:
                            this.refBoxList.hide();
                            break;
                    }
                });
                this.refBoxList.getSuggestionsList().setOnMouseClicked(ev ->
                {
                    if (ev.getButton() == MouseButton.PRIMARY && ev.getClickCount() == 2) {
                        RefBoxListItem item = this.refBoxList.getSuggestionsList().getSelectionModel().getSelectedItem();
                        this.setRef(item == null ? 0 : item.getId());
                    }
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
            } else {
                this.state.set(State.NO_RESULTS);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private ChangeListener<Boolean> getBtnHighlighter(Button btn) {
        return (ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (newValue) {
                Glow glow = new Glow();
                glow.setLevel(1d / 3d);
                SVGPath svg = (SVGPath) btn.getGraphic();
                if (svg != null)
                    svg.setFill(Color.web("#039ED3"));
                btn.setTextFill(Color.web("#039ED3"));
                btn.setEffect(glow);
            } else {
                btn.setTextFill(Color.BLACK);
                SVGPath svg = (SVGPath) btn.getGraphic();
                if (svg != null)
                    svg.setFill(Color.BLACK);
                btn.setEffect(null);
            }
        };
    }

    private enum State {
        NO_RESULTS,
        SEARCHING,
        LOGGED_IN,
    }
}
