package com.example.eventplanner;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.eventplanner.API.RetrofitClient;
import com.example.eventplanner.Models.Event;

import java.util.Calendar;
import java.util.Locale;

public class AddEvent extends AppCompatActivity {
    private EditText editTextTitle, editTextDate, editTextDescription, editTextPrice;
    private Event.EventType selectedEventType;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_event);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.add_event), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize your EditText fields
        editTextTitle = findViewById(R.id.add_event_name); // Replace with actual ID from your layout
        editTextDate = findViewById(R.id.add_event_date);   // Replace with actual ID from your layout
        editTextDescription = findViewById(R.id.add_event_description); // Replace with actual ID from your layout
        editTextPrice = findViewById(R.id.add_event_price);
        // Adding a date picker:
        editTextDate.setOnClickListener(v -> {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog datePickerDialog = new DatePickerDialog(AddEvent.this,
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

        // Selecting Event Type Spinner
        Spinner eventTypeSpinner = findViewById(R.id.add_event_type_spinner);
        ArrayAdapter<Event.EventType> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, Event.EventType.values());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        eventTypeSpinner.setAdapter(adapter);
        eventTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedEventType = (Event.EventType) parent.getItemAtPosition(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        // Add Event button.
        Button buttonAdd = findViewById(R.id.add_event_button);
        buttonAdd.setOnClickListener(v -> {
            // Get the values from the EditTexts
            String title = editTextTitle.getText().toString();
            String date = editTextDate.getText().toString();
            String description = editTextDescription.getText().toString();
            String price = editTextPrice.getText().toString();
            String eventType = selectedEventType.toString();
            // Check if the user has actually entered the data
            if(title.isEmpty() || date.isEmpty() || description.isEmpty() || price.isEmpty() || eventType.isEmpty()) {
                Toast.makeText(AddEvent.this, "Please fill in all fields.", Toast.LENGTH_SHORT).show();
                return;
            };
            Event newEvent = new Event(title, date, description, Float.parseFloat(price), selectedEventType);
            addEvent(newEvent);
        });

        // Cancel button.
        Button buttonCancel = findViewById(R.id.back_to_event_list);
        buttonCancel.setOnClickListener(v -> {
            finish();
        });
    }

    private void addEvent(Event event) {
        // Make the API call to update the event with the new information
        RetrofitClient.addEventHelper(getApplicationContext(), event);
        // Pass the updated event back to the previous activity, and set the result of the activity to OK!
        Intent resultIntent = new Intent();
        resultIntent.putExtra("addedEvent", event); // Consume the updatedEvent from the API service and pass it back to the details page.
        setResult(RESULT_OK, resultIntent);
        // Finish the activity and return to the previous one
        finish();
    }
}

