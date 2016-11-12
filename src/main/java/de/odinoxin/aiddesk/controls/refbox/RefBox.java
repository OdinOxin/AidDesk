package de.odinoxin.aiddesk.controls.refbox;

import de.odinoxin.aidcloud.provider.Provider;
import de.odinoxin.aiddesk.plugins.RecordEditor;
import de.odinoxin.aiddesk.plugins.RecordItem;
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
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class RefBox<T extends RecordItem> extends VBox {

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
    private RefBoxList<T> refBoxList;

    private ObjectProperty<T> obj = new SimpleObjectProperty<>(this, "obj", null);
    private BooleanProperty showNewButton = new SimpleBooleanProperty(this, "showNewButton", false);
    private BooleanProperty showEditButton = new SimpleBooleanProperty(this, "showEditButton", false);
    private BooleanProperty showDetails = new SimpleBooleanProperty(this, "showDetails", false);
    private BooleanProperty translate = new SimpleBooleanProperty(this, "translate", false);
    private IntegerProperty detailsRows = new SimpleIntegerProperty(this, "detailsRows", 2);
    private ObjectProperty<State> state = new SimpleObjectProperty<>();

    private boolean ignoreTextChange;
    private boolean keepText;

    private Provider<T> provider;

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
            if (this.ignoreTextChange)
                return;
            this.keepText = true;
            this.setObj(null);
            this.keepText = false;
            this.search();
        });
        this.obj.addListener((observable, oldValue, newValue) -> this.update());
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
        this.btnNew.setOnAction(ev -> {
            if (this.getProvider() != null) {
                RecordEditor<T> editor = this.getProvider().openEditor(null);
                if (editor != null)
                    editor.recordItem().addListener((observable, oldValue, newValue) -> this.setObj(newValue));
            }
        });
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
        this.showEditButton.addListener((observable, oldValue, newValue) -> this.update());
        this.btnEdit.minHeightProperty().bind(this.txfText.heightProperty());
        this.btnEdit.maxHeightProperty().bind(this.txfText.heightProperty());
        this.btnEdit.setOnAction(ev -> {
            if (this.getProvider() != null) {
                RecordEditor<T> editor = this.getProvider().openEditor(this.getObj());
                if (editor != null)
                    editor.recordItem().addListener((observable, oldValue, newValue) -> this.setObj(newValue));
            }
        });
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

        this.setObj(null);
        this.update();
    }

    public String getText() {
        return this.txfText.getText();
    }

    public void setText(String text) {
        this.txfText.setText(text);
    }

    public T getObj() {
        return obj.get();
    }

    public void setObj(T obj) {
        this.obj.set(obj);
        if (this.refBoxList != null)
            this.refBoxList.hide();
    }

    public boolean isShowNewButton() {
        return showNewButton.get();
    }

    public void setShowNewButton(boolean showNewButton) {
        this.showNewButton.set(showNewButton);
    }

    public boolean isShowEditButton() {
        return showEditButton.get();
    }

    public void setShowEditButton(boolean showEditButton) {
        this.showEditButton.set(showEditButton);
    }

    public boolean isShowDetails() {
        return showDetails.get();
    }

    public void setShowDetails(boolean showDetails) {
        this.showDetails.set(showDetails);
    }

    public boolean isTranslate() {
        return translate.get();
    }

    public void setTranslate(boolean translate) {
        this.translate.set(translate);
    }

    public int getDetailsRows() {
        return detailsRows.get();
    }

    public void setDetailsRows(int detailsRows) {
        this.detailsRows.set(detailsRows);
    }

    public Provider<T> getProvider() {
        return provider;
    }

    public void setProvider(Provider<T> provider) {
        this.provider = provider;
        this.update();
    }

    public StringProperty textProperty() {
        return this.txfText.textProperty();
    }

    public ObjectProperty<T> objProperty() {
        return obj;
    }

    public BooleanProperty showNewButton() {
        return showNewButton;
    }

    public BooleanProperty showEditButton() {
        return showEditButton;
    }

    public BooleanProperty showDetails() {
        return this.showDetails;
    }

    public IntegerProperty detailsRowsProperty() {
        return detailsRows;
    }

    public void setOnNewAction(EventHandler<ActionEvent> eventHandler) {
        this.btnNew.setOnAction(eventHandler);
    }

    public void setOnEditAction(EventHandler<ActionEvent> eventHandler) {
        this.btnEdit.setOnAction(eventHandler);
    }

    public final void setOnAction(EventHandler<ActionEvent> value) {
        this.txfText.onActionProperty().set(value);
    }

    public void update() {
        this.ignoreTextChange = true;
        RefBoxListItem<T> item;
        if (this.provider != null) {
            if (this.getObj() != null) {
                item = this.provider.getRefBoxItem(this.getObj());
                this.setText(item.getText());
                this.txfDetails.setText(item.getSubText());
                this.state.set(State.LOGGED_IN);
            } else {
                this.state.set(State.SEARCHING);
                if (!this.keepText)
                    this.txfText.setText("");
                this.txfDetails.setText("");
            }
            boolean showEditBtn = this.getObj() != null && this.isShowEditButton();
            this.btnEdit.setVisible(showEditBtn);
            this.btnEdit.setManaged(showEditBtn);
        } else if (!this.isDisabled()) {
            item = new RefBoxListItem<T>(null, "Provider not set!", "", new String[]{"Provider", "not", "set!"});
            this.setText(item.getText());
            this.txfDetails.setText(item.getSubText());
            this.state.set(State.NO_RESULTS);
        }
        this.ignoreTextChange = false;
    }

    private void search() {
        this.txfText.requestFocus();
        if (this.refBoxList != null)
            this.refBoxList.hide();
        this.refBoxList = new RefBoxList<>(this.localToScreen(0, this.txfText.getHeight()));
        this.refBoxList.setPrefWidth(this.getWidth());
        this.refBoxList.getSuggestionsList().setCellFactory(param -> new RefBoxListItemCell());

        String[] highlight = this.txfText.getText() == null || this.txfText.getText().isEmpty() ? null : this.txfText.getText().split(" ");
        if (this.provider != null) {
            List<RefBoxListItem<T>> result = this.provider.search(highlight == null ? null : Arrays.asList(highlight));
            if (result != null) {
                for (RefBoxListItem<T> item : result)
                    item.setHighlight(highlight);
                this.refBoxList.getSuggestionsList().getItems().addAll(result);
                if (result.size() > 0) {
                    if (this.state.get() == State.NO_RESULTS)
                        this.state.set(State.SEARCHING);
                    this.refBoxList.getSuggestionsList().setOnKeyPressed(ev ->
                    {
                        switch (ev.getCode()) {
                            case TAB:
                                this.btnSearch.requestFocus();
                            case ENTER:
                                RefBoxListItem<T> item = this.refBoxList.getSuggestionsList().getSelectionModel().getSelectedItem();
                                this.setObj(item == null ? null : item.getRecord());
                                break;
                            case A:
                                if (ev.isControlDown())
                                    this.txfText.selectAll();
                                break;
                            case ESCAPE:
                                this.refBoxList.hide();
                                break;
                        }
                    });
                    this.refBoxList.getSuggestionsList().setOnMouseClicked(ev ->
                    {
                        if (ev.getButton() == MouseButton.PRIMARY && ev.getClickCount() == 2) {
                            RefBoxListItem<T> item = this.refBoxList.getSuggestionsList().getSelectionModel().getSelectedItem();
                            this.setObj(item == null ? null : item.getRecord());
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
                    if (this.getScene() != null) {
                        this.refBoxList.show(this.getScene().getWindow());
                        this.refBoxList.getSuggestionsList().getSelectionModel().selectFirst();
                    }
                } else
                    this.state.set(State.NO_RESULTS);
            }
        } else {
            RefBoxListItem<T> item = new RefBoxListItem<>(null, "Provider not set!", "", new String[]{"Provider", "not", "set!"});
            this.setText(item.getText());
            this.txfDetails.setText(item.getSubText());
            this.state.set(State.NO_RESULTS);
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

    @Override
    public void requestFocus() {
        super.requestFocus();
        this.txfText.requestFocus();
    }
}
