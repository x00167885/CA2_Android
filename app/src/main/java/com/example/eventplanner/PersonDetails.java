package com.example.eventplanner;

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

public class PersonDetails extends AppCompatActivity {
    private ListView EventList;
    private ArrayAdapter<Event> arrayAdapter;
    private int UPDATE_REQUEST_CODE = 1;
    private boolean isUserInteracted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_person_details);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.person_details), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Retrieve the data for the people
        Person selectedPerson = (Person) getIntent().getSerializableExtra("selectedPerson");
        System.out.println(selectedPerson);
//
//        // Getting the name of the person
        TextView personNameTextView = findViewById(R.id.person_name_text_view);
        personNameTextView.setText(selectedPerson.getName());
//
//        // Getting the age of the person
        TextView personAgeTextView = findViewById(R.id.person_age_text_view);
        personAgeTextView.setText(Integer.toString(selectedPerson.getAge()));

        // Spinner for adding people to event list, adapt to add the attended events for each person
//        Spinner personSpinner = findViewById(R.id.event_spinner);
//        ArrayAdapter<Person> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, retrievedPeople);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        personSpinner.setAdapter(adapter);

        // NEED TO LIST ALL THE EVENTS THIS PERSON IS GOING TO.
//        // Listing people who are going to the event.
//        AttendeeList = findViewById(R.id.attendee_list);
//        arrayAdapter = new ArrayAdapter<>(this, R.layout.list_item, selectedEvent.getEventsPeople());
//        AttendeeList.setAdapter(arrayAdapter);
//
//        // So we can click on an person from the list.
//        AttendeeList.setOnItemClickListener((parent, view, position, id) -> {
//            Person selectedPerson = arrayAdapter.getItem(position);
//            // Going to go to the selected events details page from here, below:
//            Intent intent = new Intent(getApplicationContext(), PersonDetails.class);
//            // Passing in the selected event to the event details page:
//            intent.putExtra("selectedPerson", selectedPerson);
//            // Passing in the list of people within our database, so we can select to add in the details page.            // Start the activity and expect a result back if an event has been updated.
//            startActivityForResult(intent, UPDATE_REQUEST_CODE);
//        });

        // Button to go to edit event page.
//        Button buttonEditEvent = findViewById(R.id.goto_update_person);
//        buttonEditEvent.setOnClickListener(v -> {
////            // Create an intent to start the UpdateEvent activity
////            Intent intent = new Intent(PersonDetails.this, UpdatePerson.class);
////            // You can also pass the entire event object if the Event class implements Serializable
////            intent.putExtra("eventDetails", selectedEvent);
////            // Start the activity and expect a result back.
////            startActivityForResult(intent, UPDATE_REQUEST_CODE);
//        });

        // Button to go to delete a person.
//        Button buttonDeleteEvent = findViewById(R.id.delete_event_button);
//        buttonDeleteEvent.setOnClickListener(v -> {
//            showDeleteConfirmationDialog(selectedEvent);
//        });

        // Button to go back to the events details.
        Button eventsDetailsLink = findViewById(R.id.back_to_event_details);
        eventsDetailsLink.setOnClickListener(v -> {
            finish();
        });
    }

    // Editing our existing event details on activity return.
//    private void updatePersonDetailsInUI(Event event) {
//        TextView eventNameTextView = findViewById(R.id.event_name_text_view);
//        eventNameTextView.setText(event.getTitle());
//
//        TextView eventDateView = findViewById(R.id.event_date);
//        eventDateView.setText(event.getDate());
//
//        TextView eventDescriptionView = findViewById(R.id.event_description);
//        eventDescriptionView.setText(event.getDescription());
//    }

    // Showing a delete confirmation dialog for when the user chooses to delete a person.
//    private void showDeleteConfirmationDialog(Person person) {
//        new AlertDialog.Builder(this)
//                .setTitle("Confirm Delete") // Optional: set a title for the dialog
//                .setMessage("Are you sure you want to delete this item?")
//                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int button_yes) {
//                        RetrofitClient.deletePersonHelper(getApplicationContext(), person.getId(), deletionConfirmation -> {
//                            // Setting the result code for this activity, because we just deleted the event for this page.
//                            Intent resultIntent = new Intent();
//                            resultIntent.putExtra("deletedPerson", "Person Deleted");
//                            setResult(RESULT_OK, resultIntent);
//                            // Finished with this event, go back to event list and refresh.
//                            finish();
//                        });
//                    }
//                })
//                .setNegativeButton("No", null) // null listener means just dismiss the dialog
//                .show();
//    }
}