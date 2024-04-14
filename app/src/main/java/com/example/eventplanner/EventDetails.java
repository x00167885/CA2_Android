package com.example.eventplanner;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.eventplanner.Models.Event;
import com.example.eventplanner.Models.Person;

public class EventDetails extends AppCompatActivity {
    private ListView AttendeeList;
    private ArrayAdapter<Person> arrayAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_event_details);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Retrieve the data passed from the event list activity.
        Event selectedEvent = (Event) getIntent().getSerializableExtra("selectedEvent");

        // Getting the name of the Event
        TextView eventNameTextView = findViewById(R.id.event_name_text_view);
        eventNameTextView.setText(selectedEvent.getTitle());

        // Getting the date of the Event
        TextView eventDataView = findViewById(R.id.event_date);
        eventDataView.setText(selectedEvent.getDate());

        // Listing people who are going to the event.
        AttendeeList = findViewById(R.id.attendee_list);
        arrayAdapter = new ArrayAdapter<Person>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, selectedEvent.getPeople());
        AttendeeList.setAdapter(arrayAdapter);

        // Button to go to the events list.
        Button eventsListLink = findViewById(R.id.back_to_event_list);
        eventsListLink.setOnClickListener(v -> {
            Intent intent = new Intent(EventDetails.this, EventsList.class);
            startActivity(intent);
        });
    }
}