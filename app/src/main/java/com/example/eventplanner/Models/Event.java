package com.example.eventplanner.Models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Event implements Serializable { // Need to make this serializable in order to render the event data passed into details.

    public enum EventType {
        FOODFESTIVAL("FoodFestival"),
        MUSICCONCERT("MusicConcert"),
        TECHCONFERENCE("TechConference"),
        SPORTSMATCH("SportsMatch"),
        ARTEXHIBITION("ArtExhibition");
        private final String eventDescription;
        EventType(String description) {
            this.eventDescription = description;
        }
        @Override
        public String toString() {
            return this.eventDescription;
        }
    }

    @SerializedName("eventId")
    private int eventId;
    @SerializedName("title")
    private String title;
    @SerializedName("date")
    private String date;
    @SerializedName("description")
    private String description;
    @SerializedName("price")
    private float price;
    @SerializedName("type")
    private EventType type;
    @SerializedName("eventsPeople")
    private List<Person> eventsPeople;

    public Event(){} // We still need the default constructor here.

    public Event(String title_in, String date_in, String description_in, float price_in, EventType eventType_in){
        this.eventId = 0; // Not setting eventId, allowing entity framework to work that out.
        this.title = title_in;
        this.date = date_in;
        this.description = description_in;
        this.price = price_in;
        this.type = eventType_in;
        this.eventsPeople = new ArrayList<>(); // No people are going to be apart of the event at first.
    }

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
    public float getPrice() {
        return price;
    }
    public EventType getType() {
        return type;
    }

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
    public void setPrice(float price) {
        this.price = price;
    }
    public void setPeople(List<Person> people) {
        this.eventsPeople = people;
    }
    public void setType(EventType type) {
        this.type = type;
    }

    @Override // Overriding the ToString output for a nicer list on events list page.
    public String toString() {
        return getTitle();
    }
}