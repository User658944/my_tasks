package fi.heina.mytasks;

import java.util.Date;

public class ToDoModel {

    private int id;
    private String title, description, deadline, completed, noteicon;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public String getCompleted() {
        return completed;
    }

    public void setCompleted(String completed) {
        this.completed = completed;
    }

    public String getNoteicon() {
        return noteicon;
    }

    public void setNoteicon(String noteicon) {
        this.noteicon = noteicon;
    }
}