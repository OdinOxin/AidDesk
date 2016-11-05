package de.odinoxin.aiddesk.controls.refbox;

import de.odinoxin.aiddesk.plugins.RecordItem;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListCell;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class RefBoxListItemCell<T extends RecordItem> extends ListCell<RefBoxListItem<T>> {

    private GridPane grdItem;
    private int matched;

    @Override
    protected void updateItem(RefBoxListItem<T> item, boolean empty) {
        super.updateItem(item, empty);

        if (item != null) {
            try {
                this.matched = 0;
                String idText = String.valueOf(item.getRecord().getId());
                this.grdItem = FXMLLoader.load(RefBox.class.getResource("/controls/refboxlistitem.fxml"));
                this.markup("ID", idText, item.getHighlight());
                this.markup("Text", item.getText(), item.getHighlight());
                this.markup("SubText", item.getSubText(), item.getHighlight());

                if (item.getHighlight() != null)
                    for (String hightlight : item.getHighlight())
                        if (idText.equals(hightlight)) {
                            item.setMatch(Double.MAX_VALUE);
                            this.matched = -1;
                        }
                if (matched >= 0) {
                    int length = idText.length();
                    if (item.getText() != null)
                        length += item.getText().length();
                    if (item.getSubText() != null)
                        length += item.getSubText().length();
                    item.setMatch((double) matched / length);
                }
                this.setGraphic(this.grdItem);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } else {
            this.setGraphic(null);
            this.setText(null);
        }
    }

    private void markup(String controlId, String value, String[] highlight) {
        if (value == null)
            value = "";

        TextFlow tfl;
        if (!controlId.equals("SubText")) {
            tfl = (TextFlow) this.grdItem.lookup("#" + controlId);
            hightlight(tfl, false, value, highlight);
        } else {
            VBox vbox = (VBox) this.grdItem.lookup("#" + controlId);
            String[] lines = value.split("\\r?\\n");
            for (String line : lines) {
                tfl = new TextFlow();
                hightlight(tfl, true, line, highlight);
                vbox.getChildren().add(tfl);
            }
        }
    }

    private void hightlight(TextFlow tfl, boolean subText, String value, String[] highlight) {
        if (tfl == null)
            return;

        if (highlight != null && highlight.length > 0) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < highlight.length; i++)
                if (highlight[i] != null && !highlight[i].isEmpty()) {
                    sb.append(highlight[i]);
                    if (i < highlight.length - 1)
                        sb.append('|');
                }
            Pattern pattern = Pattern.compile(sb.toString(), Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(value);

            int prevEnd = 0;
            while (matcher.find()) {
                if (prevEnd < matcher.start())
                    this.addText(tfl, subText, value.substring(prevEnd, matcher.start()));
                Text bold = new Text(value.substring(matcher.start(), matcher.end()));
                if (subText)
                    bold.setStyle("-fx-font-size: 10pt; -fx-fill: darkslategray; -fx-font-weight: bold");
                else
                    bold.setStyle("-fx-font-weight: bold");
                tfl.getChildren().add(bold);
                prevEnd = matcher.end();
                this.matched += matcher.end() - matcher.start();
            }
            if (prevEnd < value.length())
                this.addText(tfl, subText, value.substring(prevEnd));
        } else
            this.addText(tfl, subText, value);
    }

    private void addText(TextFlow tfl, boolean subText, String text) {
        Text txt = new Text(text);
        if (subText)
            txt.setStyle("-fx-font-size: 10pt; -fx-fill: darkslategray");
        tfl.getChildren().add(txt);
    }
}
