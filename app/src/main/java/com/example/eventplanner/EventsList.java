package com.example.eventplanner;

// Default Imports:

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
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
            updateListView(eventsData);
        });

        // Calling API to retrieve all people, so they can be rendered appropriately by sub-pages:
        RetrofitClient.getPeopleHelper(retrievedPeople ->{
            RetrievedPeople = retrievedPeople;
        });

        // Listing out the events.
        eventsList = findViewById(R.id.eventsList);
        // Setting the listview to empty by default (before giving it a populated adapter).
        arrayAdapter = new ArrayAdapter<Event>(this, R.layout.list_item, new ArrayList<Event>());
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
            startActivity(intent);
        });

        // Back button Welcome page. (MAIN)
        Button eventsListLink = findViewById(R.id.goto_update_event);
        eventsListLink.setOnClickListener(v -> {
            finish();
        });
    }
    private void updateListView(List<Event> events) {
        // Create a list of strings to display in the ListView
        List<Event> eventObjects = new ArrayList<>();
        for (Event event : events) {
            eventObjects.add(event);
        }

        // Creating a new adapter with the new data, and attaching it to the already created ListView (eventslist).
        arrayAdapter = new ArrayAdapter<>(this, R.layout.list_item, eventObjects);
        eventsList.setAdapter(arrayAdapter);
    }
}