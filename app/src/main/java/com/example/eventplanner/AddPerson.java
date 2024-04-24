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

public class AddPerson extends AppCompatActivity {
    private EditText editTextName, editTextAge;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_person);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.add_person), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        // Getting the name of the person
        editTextName = findViewById(R.id.add_person_name);
        // Getting the age of the person
        editTextAge = findViewById(R.id.add_person_age);

        // Add Person button.
        Button buttonAdd = findViewById(R.id.add_person_button);
        buttonAdd.setText(R.string.add_person_button);


        buttonAdd.setOnClickListener(v -> {
            // Get the values from the EditTexts
            String name = editTextName.getText().toString();
            String age = editTextAge.getText().toString();
            // Check if the user has actually entered the data
            if(name.isEmpty() || age.isEmpty()) {
                Toast.makeText(AddPerson.this, R.string.fields_request, Toast.LENGTH_SHORT).show();
                return;
            };
            Person newPerson = new Person(name, Integer.parseInt(age));
            addPerson(newPerson);
        });
        // Button to go back to the events list.
        Button eventsListLink = findViewById(R.id.back_to_event_list);
        eventsListLink.setText(R.string.back_button);
        eventsListLink.setOnClickListener(v -> {
            finish();
        });
    }
    private void addPerson(Person person) {
        // Make the API call to update the event with the new information
        RetrofitClient.addPersonHelper(getApplicationContext(), person);
        // Pass the updated event back to the previous activity, and set the result of the activity to OK!
        Intent resultIntent = new Intent();
        resultIntent.putExtra("addedPerson", person); // Consume the updatedEvent from the API service and pass it back to the details page.
        setResult(RESULT_OK, resultIntent);
        // Finish the activity and return to the previous one
        finish();
    }
}