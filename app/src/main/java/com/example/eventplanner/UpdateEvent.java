package com.example.eventplanner;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.eventplanner.API.APIService;
import com.example.eventplanner.API.RetrofitClient;
import com.example.eventplanner.Models.Event;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateEvent extends AppCompatActivity {

    private EditText editTextTitle, editTextDate, editTextDescription;
    private Button buttonUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_update_event);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize your EditText fields
        editTextTitle = findViewById(R.id.update_event_name); // Replace with actual ID from your layout
        editTextDate = findViewById(R.id.update_event_date);   // Replace with actual ID from your layout
        editTextDescription = findViewById(R.id.update_event_description); // Replace with actual ID from your layout
        // Adding a date picker:
        editTextDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
            }
        });

        // Initialize your Button
        buttonUpdate = findViewById(R.id.update_event_button);

        // Update button to update the event.
        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
            }
        });

        // Update button to update the event.
        Button buttonCancel = findViewById(R.id.back_to_event_details);
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
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

        // Get the API service instance from your Retrofit client
        APIService apiService = RetrofitClient.getApiService();

        // Make the API call to update the event with the new information
        apiService.updateEvent(event.getId(), eventForUpdate).enqueue(new Callback<Event>() {
            @Override
            public void onResponse(Call<Event> call, Response<Event> response) {
                if (response.isSuccessful()) {

                    // Modifying the current event, and sending it back as the result, because we don't get anything back in response.
                    event.setTitle(eventForUpdate.getTitle());
                    event.setId(eventForUpdate.getId());
                    event.setPeople(event.getEventsPeople());
                    event.setDate(eventForUpdate.getDate());
                    event.setDescription(eventForUpdate.getDescription());

                    // Pass the updated event back to the previous activity
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("updatedEvent", event); // Make sure Event class implements Serializable or Parcelable
                    setResult(RESULT_OK, resultIntent);

                    // Notify the user of the successful update
                    Toast.makeText(UpdateEvent.this, "Event updated successfully!", Toast.LENGTH_SHORT).show();

                    // Finish the activity and return to the previous one
                    finish();
                } else {
                    // Notify the user of the successful update
                    Toast.makeText(UpdateEvent.this, "Event updated failed." + response.errorBody(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Event> call, Throwable t) {
                // The call failed to execute. Handle the failure, typically an IOException.
                Toast.makeText(UpdateEvent.this, "Update failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}