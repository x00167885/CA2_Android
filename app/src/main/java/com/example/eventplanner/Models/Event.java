package com.example.eventplanner.Models;

import java.io.Serializable;
import java.util.List;

public class Event implements Serializable { // Need to make this serializable in order to render the event dat passed into details.
    private int eventId;
    private String title;
    private String date;
    private List<Person> people;

    // Getters
    public int getEventId() {
        return eventId;
    }

    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }

    public List<Person> getPeople() { return people; }

    // Setters

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override // Overriding the ToString output for a nicer list on events list page.
    public String toString() {
        return getTitle();
    }
}