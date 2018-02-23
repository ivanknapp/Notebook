package interfaces;

import objects.Note;

public interface NotesBook {
    void add(Note note);

    void update(Note note);

    void delete(Note note);
}
