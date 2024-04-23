package com.example.eventplanner.API;

import com.example.eventplanner.Models.Event;
import com.example.eventplanner.Models.Person;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface APIService {

    // Event Related Calls:
    @GET("api/Events")
    Call<List<Event>> getEvents();

    @GET("api/Events/{id}")
    Call<Event> getEventById(@Path("id") int id);

    @POST("api/Events")
    Call<Event> addEvent(@Body Event event);

    @PUT("api/Events/{id}")
    Call<Event> updateEvent(@Path("id") int id, @Body Event event);

    @DELETE("api/Events/{id}")
    Call<Void> deleteEvent(@Path("id") int id);

    // People Related Calls:
    @GET("api/Events/People")
    Call<List<Person>> getPeople();

    @PUT("api/Events/{eventId}/Person/{personId}")
    Call<Person> updatePerson(@Path("eventId") int eventId, @Path("personId") int personId, @Body Person person);

    @POST("api/Events/{eventId}/Person/{personId}")
    Call<Void> addPersonToEvent(@Path("eventId") int eventId, @Path("personId") int personId);

    // NEED TO DO LATER:

//    @POST("api/events")
//    Call<Event> createEvent(@Body Event event);
//
//    @DELETE("api/events/{id}")
//    Call<Void> deleteEvent(@Path("id") int id);
}