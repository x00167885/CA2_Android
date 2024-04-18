package com.example.eventplanner;

// Default Imports:

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.eventplanner.API.RetrofitClient;
import com.example.eventplanner.Models.Event;
import com.example.eventplanner.Models.Person;

import java.util.ArrayList;
import java.util.List;

public class EventsList extends AppCompatActivity {
    private ListView eventsList;
    private ArrayAdapter<Event> arrayAdapter;
    private ArrayList<Person> RetrievedPeople;
    private int UPDATE_REQUEST_CODE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // BOILERPLATE:
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_events_list);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Calling API to retrieve all events:
        RetrofitClient.getEventsHelper(retrievedEvents -> {
            List<Event> eventsData = retrievedEvents;
            // Updating our list, so we have the most up-date-content showing.
            updateEventListView(eventsData);
        });

        // Calling API to retrieve all people, so they can be rendered appropriately by sub-pages:
        RetrofitClient.getPeopleHelper(retrievedPeople ->{
            RetrievedPeople = retrievedPeople;
        });

        // Listing out the events.
        eventsList = findViewById(R.id.eventsList);
        // Setting the listview to empty by default (before giving it a populated adapter).
        arrayAdapter = new ArrayAdapter<>(this, R.layout.list_item, new ArrayList<>());
        eventsList.setAdapter(arrayAdapter);

        // So we can click on an event from the list.
        eventsList.setOnItemClickListener((parent, view, position, id) -> {
            Event selectedEvent = arrayAdapter.getItem(position);
            // Going to go to the selected events details page from here, below:
            Intent intent = new Intent(getApplicationContext(), EventDetails.class);
            // Passing in the selected event to the event details page:
            intent.putExtra("selectedEvent", selectedEvent);
            // Passing in the list of people within our database, so we can select to add in the details page.
            intent.putExtra("retrievedPeople", RetrievedPeople);
            // Start the activity and expect a result back if an event has been updated.
            startActivityForResult(intent, UPDATE_REQUEST_CODE);
        });

        // Back button Welcome page. (MAIN)
        Button eventsListLink = findViewById(R.id.back_to_welcome_page);
        eventsListLink.setOnClickListener(v -> {
            finish();
        });

        // Create Event button.
        Button addEventLink = findViewById(R.id.add_event_button);
        addEventLink.setOnClickListener(v -> {
            // Going to go to the selected events details page from here, below:
            Intent intent = new Intent(getApplicationContext(), AddEvent.class);
            // Start the activity and expect a result back if an event has been updated.
            startActivityForResult(intent, UPDATE_REQUEST_CODE);
        });
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == UPDATE_REQUEST_CODE && resultCode == RESULT_OK) {
            if(data != null && data.hasExtra("updatedEvent") || data.hasExtra("addedEvent")){
                RetrofitClient.getEventsHelper(retrievedEvents -> {
                    List<Event> eventsData = retrievedEvents;
                    // Updating our list, so we have the most up-date-content after updating an event.
                    updateEventListView(eventsData);
                });
            }else{
                System.out.println("No Event was added or updated so no need to refresh.");
            }
        }
    }
}