package com.example.eventplanner.API;

import com.example.eventplanner.Models.Event;
import retrofit2.Call;
import retrofit2.http.GET;
import java.util.List;

public interface APIService {
    @GET("api/events")
    Call<List<Event>> getEvents();

    // NEED TO DO LATER:

//    @POST("api/events")
//    Call<Event> createEvent(@Body Event event);
//
//    @PUT("api/events/{id}")
//    Call<Event> updateEvent(@Path("id") int id, @Body Event event);
//
//    @DELETE("api/events/{id}")
//    Call<Void> deleteEvent(@Path("id") int id);
}