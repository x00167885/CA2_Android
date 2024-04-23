package com.example.eventplanner;

// Default Imports:

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.eventplanner.API.RetrofitClient;
import com.example.eventplanner.Models.Event;
import com.example.eventplanner.Models.Person;

import java.util.ArrayList;
import java.util.List;

public class EventsList extends AppCompatActivity {
    private ListView eventsList;
    private ArrayAdapter<Event> arrayAdapter;
    private int UPDATE_REQUEST_CODE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // BOILERPLATE:
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_events_list);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.events_list), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Listing out the events.
        eventsList = findViewById(R.id.eventsList);

        // So we can click on an event from the list.
        eventsList.setOnItemClickListener((parent, view, position, id) -> {
            Event selectedEvent = arrayAdapter.getItem(position);
            // Going to go to the selected events details page from here, below:
            Intent intent = new Intent(getApplicationContext(), EventDetails.class);
            // Passing in the selected event to the event details page:
            intent.putExtra("selectedEvent", selectedEvent);
            // Start the activity and expect a result back if an event has been updated.
            startActivityForResult(intent, UPDATE_REQUEST_CODE);
        });

        // Adding a swipe to refresh layout to the list, allowing the user to refresh the event content from the API.
        SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.swiperefresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                updateEvents(null);
                updatePeople(null);
                Toast.makeText(getApplicationContext(), "Refreshing Events and People.", Toast.LENGTH_LONG).show();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        // Back button Welcome page. (MAIN)
        Button eventsListLink = findViewById(R.id.back_to_welcome_page);
        eventsListLink.setText(R.string.back_button);
        eventsListLink.setOnClickListener(v -> {
            finish();
        });

        // Create Event button.
        Button addEventLink = findViewById(R.id.add_event_button_link);
        addEventLink.setText(R.string.add_event_button);
        addEventLink.setOnClickListener(v -> {
            // Going to go to the selected events details page from here, below:
            Intent intent = new Intent(getApplicationContext(), AddEvent.class);
            // Start the activity and expect a result back if an event has been updated.
            startActivityForResult(intent, UPDATE_REQUEST_CODE);
        });

        // Create Person button.
        Button addPersonLink = findViewById(R.id.add_person_button_link);
        addPersonLink.setText(R.string.add_person_button);
        addPersonLink.setOnClickListener(v -> {
            // Going to go to the selected events details page from here, below:
            Intent intent = new Intent(getApplicationContext(), AddPerson.class);
            // Start the activity and expect a result back if an event has been updated.
            startActivityForResult(intent, UPDATE_REQUEST_CODE);
        });

        // API CALLS AFTER VIEWS HAVE ALREADY BEEN CREATED.

        // Checking if we already have events, and pulling them down if we don't have any.
        if (RetrofitClient.retrievedEvents.isEmpty()) {
            // Calling API to retrieve all events:
            updateEvents(null);
        } else {
            updateEventListView(RetrofitClient.retrievedEvents);
        }

        if (RetrofitClient.retrievedPeople.isEmpty()) {
            // Calling API to retrieve all people, so they can be rendered appropriately by sub-pages:
            updatePeople(null);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == UPDATE_REQUEST_CODE && resultCode == RESULT_OK) {
            if (data != null) {
                if (data.hasExtra("updatedEvent") || data.hasExtra("addedEvent") || data.hasExtra("deletedEvent") || data.hasExtra("addedPerson") || data.hasExtra("personAddedToEvent")) {
                    updateEvents(null);
                    updatePeople(null);
                } else if (data.hasExtra("updatedEvents") && data.hasExtra("updatedPeople")) {
                    // If a person has been updated, they need to be reflected through the app.
                    // Accessing the refreshed data passed back from the Event details page:.
                    List<Event> updatedEvents = (List<Event>) data.getSerializableExtra("updatedEvents");
                    List<Person> updatedPeople = (List<Person>) data.getSerializableExtra("updatedPeople");
                    // Then updating the stored events and people accordingly, preventing needless requests.
                    updateEvents(updatedEvents);
                    updatePeople(updatedPeople);
                }
            } else {
                System.out.println("No Event was added, updated, or deleted so no need to refresh.");
            }
        }
    }

    private void updateEventListView(List<Event> events) {
        // Create a list of strings to display in the ListView
        List<Event> eventObjects = new ArrayList<>();
        for (Event event : events) {
            eventObjects.add(event);
        }
        // Creating a new adapter with the new data, and attaching it to the already created ListView (eventslist).
        arrayAdapter = new ArrayAdapter<>(this, R.layout.list_item, eventObjects);
        eventsList.setAdapter(arrayAdapter);
    }

    private void updateEvents(@Nullable List updatedEvents) {
        if(updatedEvents == null || updatedEvents.isEmpty()){
            RetrofitClient.getEventsHelper(retrievedEvents -> {
                // Updating our list, so we have the most up-date-content showing.
                updateEventListView(retrievedEvents);
                // Storing the retrieved events so as to not make needless requests.
                RetrofitClient.retrievedEvents = retrievedEvents;
            });
        }else{
            // We store the events we got back...
            RetrofitClient.retrievedEvents = updatedEvents;
            // Also need to update the events list view to properly propogate changes.
            updateEventListView(RetrofitClient.retrievedEvents);
        }
    }

    private void updatePeople(@Nullable List updatedPeople) {
        if(updatedPeople == null || updatedPeople.isEmpty()) {
            // Calling API to retrieve all people, so they can be rendered appropriately by sub-pages:
            RetrofitClient.getPeopleHelper(retrievedPeople -> {
                // Storing the retrieved people so as to not make needless requests.
                RetrofitClient.retrievedPeople = retrievedPeople;
            });
        }else{
            RetrofitClient.retrievedPeople = updatedPeople;
        }
    }
}