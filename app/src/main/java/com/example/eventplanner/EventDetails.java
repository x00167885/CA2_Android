package com.example.eventplanner;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.eventplanner.API.RetrofitClient;
import com.example.eventplanner.Models.Event;
import com.example.eventplanner.Models.Person;

import java.util.ArrayList;

public class EventDetails extends AppCompatActivity {
    private ListView AttendeeList;
    private ArrayAdapter<Person> arrayAdapter;
    private int UPDATE_REQUEST_CODE = 1;
    private boolean isUserInteracted = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_event_details);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.event_details), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Retrieve the data passed from the event list activity.
        Event selectedEvent = (Event) getIntent().getSerializableExtra("selectedEvent");
        ArrayList<Person> retrievedPeople = (ArrayList<Person>) getIntent().getSerializableExtra("retrievedPeople");

        // Getting the name of the Event
        TextView eventNameTextView = findViewById(R.id.event_name_text_view);
        eventNameTextView.setText(selectedEvent.getTitle());

        // Getting the date of the Event
        TextView eventDateView = findViewById(R.id.event_date);
        eventDateView.setText(selectedEvent.getDate());

        // Getting the date of the Event
        TextView eventPriceView = findViewById(R.id.event_price_text_view);
        eventPriceView.setText(Float.toString(selectedEvent.getPrice()));

        // Getting the description of the Event
        TextView eventDescriptionView = findViewById(R.id.event_description);
        eventDescriptionView.setText(selectedEvent.getDescription());

        // Getting image associated with type of Event:
        ImageView imageView = findViewById(R.id.event_image);
        showRelevantImage(imageView, selectedEvent.getType());

        // Spinner for adding people to the event list.
        Spinner personSpinner = findViewById(R.id.people_spinner);
        ArrayAdapter<Person> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, retrievedPeople);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        personSpinner.setAdapter(adapter);

        // Selecting a person from the dropdown will add them to this event.
        personSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (isUserInteracted) {
                    Person selectedPerson = (Person) parent.getItemAtPosition(position);
                    // Using our RetrofitClient helper function to make the request given the activity context.
                    RetrofitClient.addPersonToEventHelper(getApplicationContext(), selectedEvent.getId(), selectedPerson.getId(), personIsAddedSoDoThisNext -> {
                        // Refreshing the current events details. USING CONSUMABLE TO CHAIN THE ASYNCHRONOUS CALLS.
                        RetrofitClient.getEventByIdHelper(selectedEvent.getId(), retrievedEvent -> {
                            updateEventAttendees(retrievedEvent);
                        });
                    });
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Probably don't need this....
            }
        });
        // Preventing the item selector from being called on just entering the page.
        personSpinner.setOnTouchListener((view, event) -> {
            isUserInteracted = true;
            return false;
        });

        // Listing people who are going to the event.
        AttendeeList = findViewById(R.id.attendee_list);
        arrayAdapter = new ArrayAdapter<>(this, R.layout.list_item, selectedEvent.getEventsPeople());
        AttendeeList.setAdapter(arrayAdapter);

        // So we can click on an person from the list.
        AttendeeList.setOnItemClickListener((parent, view, position, id) -> {
            Person selectedPerson = arrayAdapter.getItem(position);
            // Going to go to the selected events details page from here, below:
            Intent intent = new Intent(getApplicationContext(), PersonDetails.class);
            // Passing in the selected event to the event details page:
            intent.putExtra("selectedPerson", selectedPerson);
            // Passing in the event id for updating person object.
            intent.putExtra("eventId", selectedEvent.getId());
            // Passing in the list of people within our database, so we can select to add in the details page.            // Start the activity and expect a result back if an event has been updated.
            startActivityForResult(intent, UPDATE_REQUEST_CODE);
        });

        // Button to go to edit event page.
        Button buttonEditEvent = findViewById(R.id.goto_update_event);
        buttonEditEvent.setOnClickListener(v -> {
            // Create an intent to start the UpdateEvent activity
            Intent intent = new Intent(EventDetails.this, UpdateEvent.class);
            // You can also pass the entire event object if the Event class implements Serializable
            intent.putExtra("eventDetails", selectedEvent);
            // Start the activity and expect a result back.
            startActivityForResult(intent, UPDATE_REQUEST_CODE);
        });

        // Button to go to delete an event.
        Button buttonDeleteEvent = findViewById(R.id.delete_event_button);
        buttonDeleteEvent.setOnClickListener(v -> {
            showDeleteConfirmationDialog(selectedEvent);
        });

        // Button to go to the events list.
        Button eventsListLink = findViewById(R.id.back_to_event_list);
        eventsListLink.setOnClickListener(v -> {
            finish();
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == UPDATE_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            Event updatedEvent = (Event) data.getSerializableExtra("updatedEvent");
            // Making sure we are getting an event object back from the event update page. (Otherwise display a toast error.)
            if (updatedEvent != null) {
                // Updating UI with the updated event details
                updateEventDetailsInUI(updatedEvent);
                // Setting the result code for this activity, so we can notify our list activity to update.
                Intent resultIntent = new Intent();
                resultIntent.putExtra("updatedEvent", updatedEvent);
                setResult(RESULT_OK, resultIntent);
            } else {
                Toast.makeText(this, "No updated event data received.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Editing our existing event details on activity return.
    private void updateEventDetailsInUI(Event event) {
        TextView eventNameTextView = findViewById(R.id.event_name_text_view);
        eventNameTextView.setText(event.getTitle());

        TextView eventDateView = findViewById(R.id.event_date);
        eventDateView.setText(event.getDate());

        TextView eventDescriptionView = findViewById(R.id.event_description);
        eventDescriptionView.setText(event.getDescription());

        TextView eventPriceView = findViewById(R.id.event_price_text_view);
        eventPriceView.setText(Float.toString(event.getPrice()));

        ImageView imageView = findViewById(R.id.event_image);
        showRelevantImage(imageView, event.getType());
    }

    // Updating event attendees list when we add a person to the event.
    private void updateEventAttendees(Event event) {
        // Updating the list of people when adding a person to an event.
        AttendeeList = findViewById(R.id.attendee_list);
        arrayAdapter = new ArrayAdapter<>(this, R.layout.list_item, event.getEventsPeople());
        AttendeeList.setAdapter(arrayAdapter);
    }

    // Showing a delete confirmation dialog for when the user chooses to delete the event.
    private void showDeleteConfirmationDialog(Event event) {
        new AlertDialog.Builder(this)
                .setTitle("Confirm Delete") // Optional: set a title for the dialog
                .setMessage("Are you sure you want to delete this item?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int button_yes) {
                        RetrofitClient.deleteEventHelper(getApplicationContext(), event.getId(), deletionConfirmation -> {
                            // Setting the result code for this activity, because we just deleted the event for this page.
                            Intent resultIntent = new Intent();
                            resultIntent.putExtra("deletedEvent", "Event Deleted");
                            setResult(RESULT_OK, resultIntent);
                            // Finished with this event, go back to event list and refresh.
                            finish();
                        });
                    }
                })
                .setNegativeButton("No", null) // null listener means just dismiss the dialog
                .show();
    }

    private void showRelevantImage(ImageView imageView, Event.EventType eventType) {
        switch (eventType) {
            case FOODFESTIVAL:
                imageView.setBackgroundResource(R.drawable.food_festival);
                break;
            case MUSICCONCERT:
                imageView.setBackgroundResource(R.drawable.music_concert);
                break;
            case TECHCONFERENCE:
                imageView.setBackgroundResource(R.drawable.tech_conference);
                break;
            case SPORTSMATCH:
                imageView.setBackgroundResource(R.drawable.sports_match);
                break;
            case ARTEXHIBITION:
                imageView.setBackgroundResource(R.drawable.art_exhibition);
                break;
            default:
                imageView.setBackgroundResource(R.drawable.art_exhibition);
                break;
        }
    }
}