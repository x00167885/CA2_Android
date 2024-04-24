package com.example.eventplanner;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.eventplanner.API.RetrofitClient;
import com.example.eventplanner.Models.Person;

import java.util.ArrayList;

public class UpdatePerson extends AppCompatActivity {
    private EditText editTextName, editTextAge;
    private Button buttonUpdate, buttonBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_update_person);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize your EditText fields
        editTextName = findViewById(R.id.update_person_name);// Replace with actual ID from your layout

        editTextAge = findViewById(R.id.update_person_age);

        buttonBack = findViewById(R.id.back_to_person_details); // Initialize the back button
        buttonBack.setText(R.string.back_button);
        buttonBack.setOnClickListener(v -> {
            // This will just close the current activity and go back to the previous one in the activity stack
            finish();
        });

        // Update Person button. Only buttons get setText method
        buttonUpdate = findViewById(R.id.update_person_button);
        buttonUpdate.setText(R.string.update_person_button);
        buttonUpdate.setOnClickListener(v -> {
            // Get the values from the EditTexts
            String name = editTextName.getText().toString();
            String age = editTextAge.getText().toString();
            // Check if the user has actually entered the data
            if (name.isEmpty() || age.isEmpty()) {
                Toast.makeText(UpdatePerson.this, R.string.fields_request, Toast.LENGTH_SHORT).show();
                return;
            }
            // Getting the person we passed in from the details page.
            Person personToUpdate = (Person) getIntent().getSerializableExtra("personDetails");
            // Now you can use these values to make an API call
            personToUpdate.setName(name);
            personToUpdate.setAge(Integer.parseInt(age));
            // use the updatePerson method
            updatePerson(personToUpdate);
        });
    }

    private void updatePerson(Person person) {
        // Create a new Event object with the details from the original event
        Person personForUpdate = new Person();
        personForUpdate.setId(person.getId());
        personForUpdate.setName(person.getName());
        personForUpdate.setAge(person.getAge());
        personForUpdate.setEvents(new ArrayList<>());
        // Make the API call to update the event with the new information
        RetrofitClient.updatePersonHelper(getApplicationContext(), person, personForUpdate, updatedPerson -> {
            // Pass the updated event back to the previous activity, and set the result of the activity to OK!
            Intent resultIntent = new Intent();
            resultIntent.putExtra("updatedPerson", updatedPerson); // Consume the updatedEvent from the API service and pass it back to the details page.
            setResult(RESULT_OK, resultIntent);
            // Finish the activity and return to the previous one
            finish();
        });
    }
}