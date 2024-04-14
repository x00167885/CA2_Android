package com.example.eventplanner.Models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Person implements Serializable {
    @SerializedName("personId")
    private int personId;
    @SerializedName("name")
    private String name;
    @SerializedName("age")
    private int age;

    // Getters
    public int getPersonId() {
        return personId;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

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

    @Override // Overriding the ToString output for a nicer list on events list page.
    public String toString() {
        return getName();
    }
}