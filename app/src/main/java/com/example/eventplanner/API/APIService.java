package com.example.eventplanner.API;

import com.example.eventplanner.Models.Event;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface APIService {
    @GET("api/Events")
    Call<List<Event>> getEvents();

    @PUT("api/Events/{id}")
    Call<Event> updateEvent(@Path("id") int id, @Body Event event);

    // NEED TO DO LATER:

//    @POST("api/events")
//    Call<Event> createEvent(@Body Event event);
//
//    @DELETE("api/events/{id}")
//    Call<Void> deleteEvent(@Path("id") int id);
}