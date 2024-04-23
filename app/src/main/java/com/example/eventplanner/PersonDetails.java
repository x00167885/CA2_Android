package com.example.eventplanner;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
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
        // Retrieving event id for updating person.
        int eventId = getIntent().getIntExtra("eventId", -1);

        // Getting the name of the person
        TextView personNameTextView = findViewById(R.id.person_name_text_view);
        personNameTextView.setText(selectedPerson.getName());

        // Getting the age of the person
        TextView personAgeTextView = findViewById(R.id.person_age_text_view);
        personAgeTextView.setText(Integer.toString(selectedPerson.getAge()));

        // Spinner for adding people to event list, adapt to add the attended events for each person
//        Spinner personSpinner = findViewById(R.id.event_spinner);
//        ArrayAdapter<Person> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, retrievedPeople);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        personSpinner.setAdapter(adapter);
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
        Button buttonEditEvent = findViewById(R.id.goto_update_person);
        buttonEditEvent.setOnClickListener(v -> {
            // Create an intent to start the UpdateEvent activity
            Intent intent = new Intent(PersonDetails.this, UpdatePerson.class);
            // You can also pass the entire event object if the Event class implements Serializable
            intent.putExtra("personDetails", selectedPerson);
            // Passing in the event id for updating person object.
            intent.putExtra("eventId", eventId);
            // Start the activity and expect a result back.
            startActivityForResult(intent, UPDATE_REQUEST_CODE);
        });

        Button buttonDeletePerson = findViewById(R.id.delete_person_button);
        buttonDeletePerson.setOnClickListener(v -> {
            showDeleteConfirmationDialog(selectedPerson, eventId);
        });


        // Button to go back to the events details.
        Button eventsDetailsLink = findViewById(R.id.back_to_event_details);
        eventsDetailsLink.setOnClickListener(v -> {
            finish();
        });

        // Button to go to delete a person.
//        Button buttonDeleteEvent = findViewById(R.id.delete_event_button);
//        buttonDeleteEvent.setOnClickListener(v -> {
//            showDeleteConfirmationDialog(selectedEvent);
//        });

    }

    //     Showing a delete confirmation dialog for when the user chooses to delete a person.
    private void showDeleteConfirmationDialog(Person person, int eventId) {
        new AlertDialog.Builder(this)
                .setTitle("Confirm Delete") // Optional: set a title for the dialog
                .setMessage("Are you sure you want to delete this person?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int button_yes) {
                        RetrofitClient.deletePersonHelper(getApplicationContext(), eventId, person.getId(), deletionConfirmation -> {
                            // Setting the result code for this activity, because we just deleted the event for this page.
                            Intent resultIntent = new Intent();
                            resultIntent.putExtra("deletedPerson", "Person Deleted");
                            setResult(RESULT_OK, resultIntent);
                            // Finished with this event, go back to event list and refresh.
                            finish();
                        });
                    }
                })
                .setNegativeButton("No", null) // null listener means just dismiss the dialog
                .show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == UPDATE_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            Person updatedPerson = (Person) data.getSerializableExtra("updatedPerson");
            // Making sure we are getting an event object back from the event update page. (Otherwise display a toast error.)
            if (updatedPerson != null) {
                // Updating UI with the updated event details
                updatePersonDetailsInUI(updatedPerson);
                // Setting the result code for this activity, so we can notify our list activity to update.
                Intent resultIntent = new Intent();
                resultIntent.putExtra("updatedPerson", updatedPerson);
                setResult(RESULT_OK, resultIntent);
            } else {
                Toast.makeText(this, "No updated person data received.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    //Editing our existing event details on activity return.
    private void updatePersonDetailsInUI(Person person) {
        TextView personNameTextView = findViewById(R.id.person_name_text_view);
        personNameTextView.setText(person.getName());

        TextView personAgeTextView = findViewById(R.id.person_age_text_view);
        personAgeTextView.setText(Integer.toString(person.getAge()));
    }
}

//    private void showDeleteConfirmationDialog(Person person) {
//        new AlertDialog.Builder(this)
//                .setTitle("Confirm Delete") // Optional: set a title for the dialog
//                .setMessage("Are you sure you want to delete this person?")
//                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int button_yes) {
//                        RetrofitClient.deleteEventHelper(getApplicationContext(), person.getId(), deletionConfirmation -> {
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
//}