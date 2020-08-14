package com.example.todolisttest;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Notes Table")
public class Note {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String Title;
    private String Description;
    private String Date;

    public Note(String Title, String Description, String Date) {
        this.Title = Title;
        this.Description = Description;
        this.Date = Date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return Title;
    }


    public String getDescription() {
        return Description;
    }


    public String getDate() {
        return Date;
    }

}
