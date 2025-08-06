package com.example.groupproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class StudentEntryActivity extends AppCompatActivity {
    EditText fullName, idNumber, gender, courseMajor;
    Button saveButton;
    // Declare dbRef here
    DatabaseReference dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_entry);

        fullName = findViewById(R.id.fullNameInput);
        idNumber = findViewById(R.id.idNumberInput); // Ensure this ID matches your layout file
        gender = findViewById(R.id.genderInput);
        courseMajor = findViewById(R.id.courseMajorInput);
        saveButton = findViewById(R.id.saveButton);

        // Initialize dbRef here
        dbRef = FirebaseDatabase.getInstance().getReference("students");

        saveButton.setOnClickListener(v -> {
            String name = fullName.getText().toString().trim();
            String id = idNumber.getText().toString().trim();
            String g = gender.getText().toString().trim();
            String major = courseMajor.getText().toString().trim();

            if (name.isEmpty() || id.isEmpty() || g.isEmpty() || major.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            } else {
                String studentKey = dbRef.push().getKey(); // Use a more descriptive variable name like studentKey or recordKey
                if (studentKey == null) {
                    Toast.makeText(StudentEntryActivity.this, "Failed to generate a unique key for the student.", Toast.LENGTH_SHORT).show();
                    return; // Exit if key generation fails
                }
                Student student = new Student(name, id, g, major); // Assuming Student class constructor matches these parameters

                dbRef.child(studentKey).setValue(student)
                        .addOnSuccessListener(aVoid -> {
                            Toast.makeText(StudentEntryActivity.this, "Student Saved", Toast.LENGTH_SHORT).show();
                            clearForm();
                        })
                        .addOnFailureListener(e -> Toast.makeText(StudentEntryActivity.this, "Failed to save student: " + e.getMessage(), Toast.LENGTH_SHORT).show());
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_fee_entry) {
            startActivity(new Intent(this, FeeEntryActivity.class));
            return true;
        } else if (id == R.id.menu_summary) {
            startActivity(new Intent(this, SummaryActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void clearForm() {
        fullName.setText("");
        idNumber.setText("");
        gender.setText("");
        courseMajor.setText("");
    }
}