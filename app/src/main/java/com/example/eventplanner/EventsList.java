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

        // Listing out the events.
        eventsList = findViewById(R.id.eventsList);

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

        // Adding a swipe to refresh layout to the list, allowing the user to refresh the event content from the API.
        SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.swiperefresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                updateEvents();
                updatePeople();
                Toast.makeText(getApplicationContext(), "Refreshing Events and People.", Toast.LENGTH_LONG).show();
                swipeRefreshLayout.setRefreshing(false);
            }
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

        // API CALLS AFTER VIEWS HAVE ALREADY BEEN CREATED.

        // Checking if we already have events, and pulling them down if we don't have any.
        if(RetrofitClient.retrievedEvents.isEmpty()){
            // Calling API to retrieve all events:
            updateEvents();
        } else {
            updateEventListView(RetrofitClient.retrievedEvents);
        }

        if(RetrofitClient.retrievedPeople.isEmpty()){
            // Calling API to retrieve all people, so they can be rendered appropriately by sub-pages:
            updatePeople();
        } else {
            RetrievedPeople = (ArrayList<Person>) RetrofitClient.retrievedPeople;
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

    private void updateEvents(){
        RetrofitClient.getEventsHelper(retrievedEvents -> {
            List<Event> eventsData = retrievedEvents;
            // Updating our list, so we have the most up-date-content showing.
            updateEventListView(eventsData);
            // Storing the retrieved events so as to not make needless requests.
            RetrofitClient.retrievedEvents = eventsData;
        });
    }

    private void updatePeople(){
        // Calling API to retrieve all people, so they can be rendered appropriately by sub-pages:
        RetrofitClient.getPeopleHelper(retrievedPeople ->{
            RetrievedPeople = retrievedPeople;
            // Storing the retrieved people so as to not make needless requests.
            RetrofitClient.retrievedPeople = retrievedPeople;
        });
    }
}