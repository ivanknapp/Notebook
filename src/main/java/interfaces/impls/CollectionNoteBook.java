package interfaces.impls;

import interfaces.NotesBook;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import objects.Note;


public class CollectionNoteBook implements NotesBook {

    private ObservableList<Note> notes = FXCollections.observableArrayList();

    @Override
    public void add(Note note) {
        notes.add(note);
    }

    @Override
    public void update(Note note) {

    }

    @Override
    public void delete(Note note) {
        notes.remove(note);
    }

    public ObservableList<Note> getNotes() {
        return notes;
    }
}
