package de.odinoxin.aiddesk.controls.refbox;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

public class RefBoxListItem {

    private int id;
    private String text;
    private String subText;
    private String[] highlight;
    private DoubleProperty matchProperty = new SimpleDoubleProperty(this, "match");

    public RefBoxListItem(int id, String text, String subText) {
        this.id = id;
        this.text = text;
        this.subText = subText;
    }

    public RefBoxListItem(int id, String text, String subText, String[] highlight) {
        this(id, text, subText);
        this.highlight = highlight;
    }

    public int getId() {
        return id;
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

    public void setId(int id) {
        this.id = id;
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
