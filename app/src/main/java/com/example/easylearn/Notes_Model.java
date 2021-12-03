package com.example.easylearn;

public class Notes_Model {

    private String notesId, topicName, notes;

    public Notes_Model(String notesId, String topicName, String notes) {
        this.notesId = notesId;
        this.topicName = topicName;
        this.notes = notes;
    }

    public Notes_Model() {
    }

    public String getNotesId() {
        return notesId;
    }

    public void setNotesId(String notesId) {
        this.notesId = notesId;
    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
