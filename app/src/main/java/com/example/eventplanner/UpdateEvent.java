package com.example.eventplanner;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.eventplanner.API.RetrofitClient;
import com.example.eventplanner.Models.Event;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class UpdateEvent extends AppCompatActivity {
    private EditText editTextTitle, editTextDate, editTextDescription;
    private Button buttonUpdate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_update_event);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.update_event), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        // Initialize your EditText fields
        editTextTitle = findViewById(R.id.update_event_name); // Replace with actual ID from your layout
        editTextDate = findViewById(R.id.update_event_date);   // Replace with actual ID from your layout
        editTextDescription = findViewById(R.id.update_event_description); // Replace with actual ID from your layout
        // Adding a date picker:
        editTextDate.setOnClickListener(v -> {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog datePickerDialog = new DatePickerDialog(UpdateEvent.this,
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            // The month value is 0-based, so add 1
                            String selectedDate = String.format(Locale.getDefault(), "%d-%02d-%02d", year, monthOfYear + 1, dayOfMonth);
                            editTextDate.setText(selectedDate);
                        }
                    }, year, month, day);
            datePickerDialog.show();
        });

        // Update Event button.
        buttonUpdate = findViewById(R.id.update_event_button);
        buttonUpdate.setOnClickListener(v -> {
            // Get the values from the EditTexts
            String title = editTextTitle.getText().toString();
            String date = editTextDate.getText().toString();
            String description = editTextDescription.getText().toString();
            // Check if the user has actually entered the data
            if(title.isEmpty() || date.isEmpty() || description.isEmpty()) {
                Toast.makeText(UpdateEvent.this, "Please fill in all fields.", Toast.LENGTH_SHORT).show();
                return;
            }
            // Getting the event we passed in from the details page.
            Event eventToUpdate = (Event) getIntent().getSerializableExtra("eventDetails");
            // Now you can use these values to make an API call
            eventToUpdate.setTitle(title);
            eventToUpdate.setDate(date);
            eventToUpdate.setDescription(description);
            // Assuming you have a method to update the event
            updateEvent(eventToUpdate);
        });

        // Update button to update the event.
        Button buttonCancel = findViewById(R.id.back_to_event_details);
        buttonCancel.setOnClickListener(v -> {
            finish();
        });
    }

    // Function to call the API to update an event
    private void updateEvent(Event event) {
        // Create a new Event object with the details from the original event
        Event eventForUpdate = new Event();
        eventForUpdate.setId(event.getId());
        eventForUpdate.setTitle(event.getTitle());
        eventForUpdate.setDate(event.getDate());
        eventForUpdate.setDescription(event.getDescription());
        // Set an empty people's list to not confuse Entity Framework. (WE ARE ONLY UPDATING THE EVENTS DETAILS NOT THE PEOPLE PART OF THE EVENT)
        eventForUpdate.setPeople(new ArrayList<>());

        // Make the API call to update the event with the new information
        RetrofitClient.updateEventHelper(getApplicationContext(), event, eventForUpdate, updatedEvent -> {
            // Pass the updated event back to the previous activity, and set the result of the activity to OK!
            Intent resultIntent = new Intent();
            resultIntent.putExtra("updatedEvent", updatedEvent); // Consume the updatedEvent from the API service and pass it back to the details page.
            setResult(RESULT_OK, resultIntent);
            // Finish the activity and return to the previous one
            finish();
        });
    }
}