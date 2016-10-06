package de.odinoxin.aiddesk.controls;

import com.sun.deploy.util.StringUtils;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListCell;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RefBoxListItemCell extends ListCell<RefBoxListItem> {

    private GridPane grdItem;

    @Override
    protected void updateItem(RefBoxListItem item, boolean empty) {
        super.updateItem(item, empty);

        if (item != null) {
            try {
                this.grdItem = FXMLLoader.load(RefBox.class.getResource("/controls/refboxlistitem.fxml"));
                this.hightlight("ID", String.valueOf(item.getId()), item.getHighlight());
                this.hightlight("Text", item.getText(), item.getHighlight());
                this.setGraphic(this.grdItem);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } else {
            this.setGraphic(null);
            this.setText(null);
        }
    }

    private void hightlight(String controlId, String value, String[] highlight)
    {
        TextFlow tfl = (TextFlow) grdItem.lookup("#" + controlId);
        if (tfl != null) {
            if (highlight != null && highlight.length > 0) {
                Pattern pattern = Pattern.compile(String.join("|", highlight), Pattern.CASE_INSENSITIVE);
                Matcher matcher = pattern.matcher(value);

                int prevEnd = 0;
                while (matcher.find()) {
                    if (prevEnd < matcher.start())
                        tfl.getChildren().add(new Text(value.substring(prevEnd, matcher.start())));
                    Text bold = new Text(value.substring(matcher.start(), matcher.end()));
                    bold.setStyle("-fx-font-weight: bold");
                    tfl.getChildren().add(bold);
                    prevEnd = matcher.end();
                }
                if (prevEnd < value.length())
                    tfl.getChildren().add(new Text(value.substring(prevEnd)));
            } else
                tfl.getChildren().add(new Text(value));
        }
    }
}
