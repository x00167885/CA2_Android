package com.example.eventplanner;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

//import com.example.eventplanner.Models.Event;
import com.example.eventplanner.Models.Person;

public class UpdatePerson extends AppCompatActivity {

    private EditText editTextName, editTextAge;
    private Button buttonUpdate;
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
        editTextName = findViewById(R.id.update_person_name); // Replace with actual ID from your layout
        editTextAge = findViewById(R.id.update_person_age);   // Replace with actual ID from xml


        // Update Person button.
        buttonUpdate = findViewById(R.id.update_person_button);
        buttonUpdate.setOnClickListener(v -> {
            // Get the values from the EditTexts
            String name = editTextName.getText().toString();
            String age = editTextAge.getText().toString();
            // Check if the user has actually entered the data
            if(name.isEmpty() || age.isEmpty() ) {
                Toast.makeText(UpdatePerson.this, "Please fill in all fields.", Toast.LENGTH_SHORT).show();
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
}