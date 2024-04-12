package com.example.eventplanner.Models;

import java.util.List;

public class Event {
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
}