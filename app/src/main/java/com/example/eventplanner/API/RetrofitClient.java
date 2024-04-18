package com.example.eventplanner.API;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.eventplanner.Models.Event;
import com.example.eventplanner.Models.Person;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static Retrofit retrofit = null;
    private static final String BASE_URL = "https://ead2-ca2-api.azurewebsites.net/"; // Our API hosted on Azure.

    // WE ARE USING OUR API SERVICE TO KEEP TRACK OF ALL REQUESTED OBJECTS, PREVENTING REDUNDANT REQUESTS:
    private static ArrayList eventsList = null;

    public static APIService getApiService() {
        // If retrofit is not yet initialised to be used within our application, we must do so.
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create()) // Using GSON to parse response.
                    .build();
        }
        return retrofit.create(APIService.class);
    }

    // Person Specific endpoint helpers:

    // Getting people to populate event detail pages, endpoint:
    public static void getPeopleHelper(Consumer<ArrayList> onPeopleRequestSuccess){
        getApiService().getPeople().enqueue(new Callback<List<Person>>() {
            @Override
            public void onResponse(Call<List<Person>> call, Response<List<Person>> response) {
                if (response.isSuccessful()) {
                    // Telling our Activity that it needs to accept our array list of retrieved people, as input.
                    onPeopleRequestSuccess.accept((ArrayList) response.body());
                }
            }
            @Override
            public void onFailure(Call<List<Person>> call, Throwable t) {
                Log.e("PeopleList", "Error fetching people" + t);
            }
        });
    };

    // Adding person to event, endpoint.
    public static void addPersonToEventHelper(Context context, int eventId, int personId, Consumer<Response> onSuccess){
        getApiService().addPersonToEvent(eventId, personId).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    // Displaying Confirmation message that the user has been successfully added to the event.
                    Toast.makeText(context, "Person added to event successfully", Toast.LENGTH_LONG).show();
                    // Just passing back the success response. (Even though we aren't going to be using it, it's just for chaining the async calls)
                    onSuccess.accept(response);
                } else {
                    // Handling the case where the server responds with an error
                    Toast.makeText(context, "Person not added to event.", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                // Handle failure such as a network error
                Log.e("EventsList", "Error adding person to event", t);
            }
        });
    }

    // Event Specific endpoint helpers:

    // Getting all events, endpoint.
    public static void getEventsHelper(Consumer<List> onEventsRequestSuccess){
        getApiService().getEvents().enqueue(new Callback<List<Event>>() {
            @Override
            public void onResponse(Call<List<Event>> call, Response<List<Event>> response) {
                if (response.isSuccessful()) {
                    onEventsRequestSuccess.accept(response.body());
                }
            }
            @Override
            public void onFailure(Call<List<Event>> call, Throwable t) {
                Log.e("EventsList", "Error fetching events" + t);
            }
        });
    }

    // Get event by it's id, endpoint.
    public static void getEventByIdHelper(int eventId, Consumer<Event> onEventRequestSuccess){
        getApiService().getEventById(eventId).enqueue(new Callback<Event>() {
            @Override
            public void onResponse(Call<Event> call, Response<Event> response) {
                if (response.isSuccessful()) {
                    onEventRequestSuccess.accept(response.body());
                }
            }
            @Override
            public void onFailure(Call<Event> call, Throwable t) {
                Log.e("Event", "Error fetching event" + t);
            }
        });
    }

    // Adding an Event endpoint.
    public static void addEventHelper(Context context, Event event){
        getApiService().addEvent(event).enqueue(new Callback<Event>() {
            @Override
            public void onResponse(Call<Event> call, Response<Event> response) {
                if (response.isSuccessful()) {
                    // Notify the user of successfully adding an event.
                    Toast.makeText(context, "Event updated successfully!", Toast.LENGTH_SHORT).show();
                } else {
                    // Notify the user that there was a problem.
                    Toast.makeText(context, "Update failed." + response.errorBody(), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<Event> call, Throwable t) {
                // The call failed to execute. Handle the failure, typically an IOException.
                Toast.makeText(context, "Update failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Updating the event details endpoint.
    public static void updateEventHelper(Context context, Event originalEvent, Event eventForUpdate, Consumer<Event> onEventUpdateSuccess){
        getApiService().updateEvent(originalEvent.getId(), eventForUpdate).enqueue(new Callback<Event>() {
            @Override
            public void onResponse(Call<Event> call, Response<Event> response) {
                if (response.isSuccessful()) {
                    // Modifying the current event, and sending it back as the result, because we don't get anything back in response.
                    originalEvent.setTitle(eventForUpdate.getTitle());
                    originalEvent.setId(eventForUpdate.getId());
                    originalEvent.setPeople(originalEvent.getEventsPeople());
                    originalEvent.setDate(eventForUpdate.getDate());
                    originalEvent.setDescription(eventForUpdate.getDescription());
                    // Telling our Activity that it needs to accept our updated event as an input.
                    onEventUpdateSuccess.accept(originalEvent);
                    // Notify the user of the successful update.
                    Toast.makeText(context, "Event updated successfully!", Toast.LENGTH_SHORT).show();
                } else {
                    // Notify the user that there was a problem.
                    Toast.makeText(context, "Update failed." + response.errorBody(), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<Event> call, Throwable t) {
                // The call failed to execute. Handle the failure, typically an IOException.
                Toast.makeText(context, "Update failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}