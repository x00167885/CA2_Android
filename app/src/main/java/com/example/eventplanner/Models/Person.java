package com.example.eventplanner.Models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
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
    public Person(){}
    public Person(String name_in, int age_in){
        this.name = name_in;
        this.age = age_in;
        this.eventsPeople = new ArrayList<>();
    }

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
    public void setId(int personId) {
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