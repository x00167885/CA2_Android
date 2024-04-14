package com.example.eventplanner;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.eventplanner.Models.Event;
import com.example.eventplanner.Models.Person;


public class EventDetails extends AppCompatActivity {
    private ListView AttendeeList;
    private ArrayAdapter<Person> arrayAdapter;
    private int UPDATE_REQUEST_CODE = 1;
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
        TextView eventDateView = findViewById(R.id.event_date);
        eventDateView.setText(selectedEvent.getDate());

        // Getting the description of the Event
        TextView eventDescriptionView = findViewById(R.id.event_description);
        eventDescriptionView.setText(selectedEvent.getDescription());

        // Listing people who are going to the event.
        AttendeeList = findViewById(R.id.attendee_list);
        arrayAdapter = new ArrayAdapter<Person>(this, R.layout.list_item, selectedEvent.getEventsPeople());
        AttendeeList.setAdapter(arrayAdapter);

        Button buttonEditEvent = findViewById(R.id.goto_update_event);
        buttonEditEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an intent to start the UpdateEvent activity
                Intent intent = new Intent(EventDetails.this, UpdateEvent.class);
                // You can also pass the entire event object if the Event class implements Serializable
                intent.putExtra("eventDetails", selectedEvent);
                // Start the activity and expect a result back
                startActivityForResult(intent, UPDATE_REQUEST_CODE);
            }
        });

        // Button to go to the events list.
        Button eventsListLink = findViewById(R.id.back_to_event_list);
        eventsListLink.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == UPDATE_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            Event updatedEvent = (Event) data.getSerializableExtra("updatedEvent");
            if (updatedEvent != null) {
                updateUIWithEventDetails(updatedEvent);
            } else {
                Toast.makeText(this, "No updated event data received.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Define a method to update your UI components with the updated event details
    private void updateUIWithEventDetails(Event event) {
        // Assuming you have TextViews for the event title, date, and description
        TextView eventNameTextView = findViewById(R.id.event_name_text_view);
        eventNameTextView.setText(event.getTitle());

        TextView eventDateView = findViewById(R.id.event_date);
        eventDateView.setText(event.getDate());

        TextView eventDescriptionView = findViewById(R.id.event_description);
        eventDescriptionView.setText(event.getDescription());
    }
}