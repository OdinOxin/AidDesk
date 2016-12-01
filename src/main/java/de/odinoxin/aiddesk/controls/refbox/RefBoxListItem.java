package de.odinoxin.aiddesk.controls.refbox;

import de.odinoxin.aiddesk.plugins.RecordItem;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

/**
 * Datamodel for {@link RefBoxList} items, representing a {@link RecordItem}.
 * @param <T> The type of the record.
 */
public class RefBoxListItem<T extends RecordItem> {

    /**
     * The record to represent.
     */
    private T record;
    /**
     * The main text.
     */
    private String text;
    /**
     * The sub text.
     */
    private String subText;
    /**
     * Text occurrences to highlight.
     */
    private String[] highlight;
    /**
     * Match percentage.
     */
    private DoubleProperty matchProperty = new SimpleDoubleProperty(this, "match");

    public RefBoxListItem(T record, String text, String subText) {
        this.record = record;
        this.text = text;
        this.subText = subText;
    }

    public RefBoxListItem(T record, String text, String subText, String[] highlight) {
        this(record, text, subText);
        this.highlight = highlight;
    }

    public T getRecord() {
        return record;
    }

    public String getText() {
        return text;
    }

    public String getSubText() {
        return subText;
    }

    public String[] getHighlight() {
        return highlight;
    }

    public double getMatch() {
        return this.matchProperty.get();
    }

    public void setRecord(T record) {
        this.record = record;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setSubText(String subText) {
        this.subText = subText;
    }

    public void setHighlight(String[] highlight) {
        this.highlight = highlight;
    }

    public void setMatch(double match) {
        this.matchProperty.set(match);
    }

    public DoubleProperty matchProperty() {
        return this.matchProperty;
    }
}
