package com.example.eventplanner.Models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Event implements Serializable { // Need to make this serializable in order to render the event data passed into details.
    @SerializedName("eventId")
    private int eventId;
    @SerializedName("title")
    private String title;
    @SerializedName("date")
    private String date;
    @SerializedName("description")
    private String description;
    @SerializedName("eventsPeople")
    private List<Person> eventsPeople;

    // Getters
    public int getId() {
        return eventId;
    }
    public String getTitle() {
        return title;
    }
    public String getDate() { return date; }
    public String getDescription() { return description; }
    public List<Person> getEventsPeople() { return eventsPeople; }

    // Setters
    public void setId(int eventId) {
        this.eventId = eventId;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public void setPeople(List<Person> people) {
        this.eventsPeople = people;
    }

    @Override // Overriding the ToString output for a nicer list on events list page.
    public String toString() {
        return getTitle();
    }
}