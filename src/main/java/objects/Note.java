package objects;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Note {
    private final StringProperty date = new SimpleStringProperty("");
    private final StringProperty text = new SimpleStringProperty("");


    public Note() {
    }

    public Note(String date, String text) {
        this.date.set(date);
        this.text.set(text);
    }

    public String getDate() {
        return date.get();
    }

    public StringProperty dateProperty() {
        return date;
    }

    public void setDate(String date) {
        this.date.set(date);
    }

    public String getText() {
        return text.get();
    }

    public StringProperty textProperty() {
        return text;
    }

    public void setText(String text) {
        this.text.set(text);
    }


}
