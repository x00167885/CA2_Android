package com.example.eventplanner.Models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Person implements Serializable {
    @SerializedName("personId")
    private int personId;
    @SerializedName("name")
    private String name;
    @SerializedName("age")
    private int age;
    @SerializedName("eventsPeople")
    private List<Event> eventsPeople;

    // Constructor
    public Person(String name_in, int age_in){} // NEED THIS JUST FOR TESTING

    // Getters
    public int getId() {
        return personId;
    }
    public String getName() {
        return name;
    }
    public int getAge() {
        return age;
    }
    public List<Event> getEventsPeople() { return eventsPeople; }

    // Setters
    public void setPersonId(int personId) {
        this.personId = personId;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setAge(int age) {
        this.age = age;
    }
    public void setEvents(List<Event> people) {
        this.eventsPeople = people;
    }

    @Override // Overriding the ToString output for a nicer list on events list page.
    public String toString() {
        return getName();
    }
}