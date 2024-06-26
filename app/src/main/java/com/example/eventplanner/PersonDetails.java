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
    private TextView personNameTextView, personAgeTextView, attendeeCostTextView;
    private ArrayAdapter<Event> arrayAdapter;
    private int UPDATE_REQUEST_CODE = 1;
    private Event chosenEvent;

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
        Event selectedEvent = (Event) getIntent().getSerializableExtra("selectedEvent");
        chosenEvent = selectedEvent;

        // Getting the name of the person
        personNameTextView = findViewById(R.id.person_name_text_view);
        personNameTextView.setText(selectedPerson.getName());

        // Getting the age of the person
        personAgeTextView = findViewById(R.id.person_age_text_view);
        personAgeTextView.setText(Integer.toString(selectedPerson.getAge()));

        // Setting Attendee cost:
        attendeeCostTextView = findViewById(R.id.attendee_cost);
        attendeeCostTextView.setText("€ " + Double.toString(calculateCostForPerson(selectedPerson.getAge(), selectedEvent.getPrice())));

        // Button to go to edit event page.
        Button buttonEditEvent = findViewById(R.id.goto_update_person);
        buttonEditEvent.setText(R.string.update_person_button);
        buttonEditEvent.setOnClickListener(v -> {
            // Create an intent to start the UpdateEvent activity
            Intent intent = new Intent(PersonDetails.this, UpdatePerson.class);
            // You can also pass the entire event object if the Event class implements Serializable
            intent.putExtra("personDetails", selectedPerson);
            // Start the activity and expect a result back.
            startActivityForResult(intent, UPDATE_REQUEST_CODE);
        });

        Button buttonDeletePerson = findViewById(R.id.delete_person_button);
        buttonDeletePerson.setText(R.string.delete_person_button);
        buttonDeletePerson.setOnClickListener(v -> {
            showDeleteConfirmationDialog(selectedPerson);
        });

        // Button to go back to the events details.
        Button eventsDetailsLink = findViewById(R.id.back_to_event_details);
        eventsDetailsLink.setText(R.string.back_button);
        eventsDetailsLink.setOnClickListener(v -> {
            finish();
        });
    }

    //     Showing a delete confirmation dialog for when the user chooses to delete a person.
    private void showDeleteConfirmationDialog(Person person) {
        new AlertDialog.Builder(this)
                .setTitle(R.string.delete_confirmation_title) // Optional: set a title for the dialog
                .setMessage(R.string.delete_confirmation_message)
                .setPositiveButton(R.string.yes_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int button_yes) {
                        RetrofitClient.deletePersonHelper(getApplicationContext(), person.getId(), deletionConfirmation -> {
                            // Setting the result code for this activity, because we just deleted the event for this page.
                            Intent resultIntent = new Intent();
                            resultIntent.putExtra("deletedPerson", "Person Deleted");
                            setResult(RESULT_OK, resultIntent);
                            // Finished with this event, go back to event list and refresh.
                            finish();
                        });
                    }
                })
                .setNegativeButton(R.string.no_button, null) // null listener means just dismiss the dialog
                .show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == UPDATE_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            Person updatedPerson = (Person) data.getSerializableExtra("updatedPerson");
            // Making sure we are getting a person object back from the person update page. (Otherwise display a toast error.)
            if (updatedPerson != null) {
                updatePersonDetailsInUI(updatedPerson);
                Intent resultIntent = new Intent();
                resultIntent.putExtra("updatedPerson", updatedPerson);
                setResult(RESULT_OK, resultIntent);
                // We need to update the cost displayed based on the person's new age if edited.
                attendeeCostTextView.setText("€ " + Double.toString(calculateCostForPerson(updatedPerson.getAge(), chosenEvent.getPrice())));
            } else {
                Toast.makeText(this, R.string.data_received, Toast.LENGTH_SHORT).show();
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

    private double calculateCostForPerson(int person_age, float event_cost){
        if (person_age <= 18) {
            return (event_cost * 0.75);
        } else if (person_age <= 60) {
            return event_cost;
        } else {
            return (event_cost * 0.25);
        }
    }
}