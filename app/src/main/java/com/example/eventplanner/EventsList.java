package com.example.eventplanner;

// Default Imports:
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

// My imports:
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.example.eventplanner.API.APIService;
import com.example.eventplanner.API.RetrofitClient;
import com.example.eventplanner.Models.Event;

import java.util.ArrayList;
import java.util.List;

// For retrofit response parsing:
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventsList extends AppCompatActivity {
    private ListView eventsList = findViewById(R.id.eventsList);
    private ArrayAdapter<String> arrayAdapter;
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

        // Setting the listview to empty by default (before giving it a populated adapter).
        arrayAdapter = new ArrayAdapter<String>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, new ArrayList<>());
        eventsList.setAdapter(arrayAdapter);

        // Fetch the events from the API
        APIService apiService = RetrofitClient.getApiService();
        apiService.getEvents().enqueue(new Callback<List<Event>>() {
            @Override
            public void onResponse(Call<List<Event>> call, Response<List<Event>> response) {
                if (response.isSuccessful()) {
                    List<Event> eventsData = response.body();
                    updateListView(eventsData);
                }
            }

            @Override
            public void onFailure(Call<List<Event>> call, Throwable t) {
                Log.e("EventsList", "Error fetching events" + t);
            }
        });

        // So we can click on an event from the list.
        eventsList.setOnItemClickListener((parent, view, position, id) -> {
            String selectedEvent = arrayAdapter.getItem(position);
            // Going to go to the selected events details page from here, below:
            //.... - List people part of the event, the time of the event etc....
        });

        // Back button Welcome page. (MAIN)
        Button eventsListLink = findViewById(R.id.button2);
        eventsListLink.setOnClickListener(v -> {
            Intent intent = new Intent( EventsList.this, MainActivity.class);
            startActivity(intent);
        });
    }
    private void updateListView(List<Event> events) {
        // Create a list of strings to display in the ListView
        List<String> eventTitles = new ArrayList<>();
        for (Event event : events) {
            eventTitles.add(event.getTitle());
        }

        // Creating a new adapter with the new data, and attaching it to the already created ListView (eventslist).
        arrayAdapter = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, eventTitles);
        eventsList.setAdapter(arrayAdapter);
    }
}