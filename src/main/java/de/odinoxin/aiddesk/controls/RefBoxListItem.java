package de.odinoxin.aiddesk.controls;

public class RefBoxListItem {

    private int id;
    private String text;
    private String[] highlight;

    public RefBoxListItem(int id, String text)
    {
        this.id = id;
        this.text = text;
    }
    public RefBoxListItem(int id, String text, String[] highlight)
    {
        this(id, text);
        this.highlight = highlight;
    }

    public int getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public String[] getHighlight() {
        return highlight;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setHighlight(String[] highlight) {
        this.highlight = highlight;
    }
}
