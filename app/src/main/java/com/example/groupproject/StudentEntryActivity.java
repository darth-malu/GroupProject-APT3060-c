package com.example.groupproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class StudentEntryActivity extends AppCompatActivity {
    private static final String TAG = "StudentEntryActivity";
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
        try {
            // Try to get the database instance with explicit URL
            FirebaseDatabase database = FirebaseDatabase.getInstance("https://class-23f8f-default-rtdb.firebaseio.com/");
            dbRef = database.getReference("students");
            Log.d(TAG, "Firebase database reference initialized with explicit URL");
        } catch (Exception e) {
            Log.e(TAG, "Failed to initialize Firebase database: " + e.getMessage(), e);
            // Fallback to default instance
            dbRef = FirebaseDatabase.getInstance().getReference("students");
            Log.d(TAG, "Using default Firebase database instance");
        }

        saveButton.setOnClickListener(v -> {
            Log.d(TAG, "Save button clicked");
            String name = fullName.getText().toString().trim();
            String id = idNumber.getText().toString().trim();
            String g = gender.getText().toString().trim();
            String major = courseMajor.getText().toString().trim();

            Log.d(TAG, "Input values - Name: " + name + ", ID: " + id + ", Gender: " + g + ", Major: " + major);

            if (name.isEmpty() || id.isEmpty() || g.isEmpty() || major.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                Log.w(TAG, "Validation failed - empty fields detected");
            } else {
                Log.d(TAG, "Validation passed, attempting to save to Firebase");
                String studentKey = dbRef.push().getKey(); // Use a more descriptive variable name like studentKey or recordKey
                if (studentKey == null) {
                    Toast.makeText(StudentEntryActivity.this, "Failed to generate a unique key for the student.", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "Failed to generate unique key");
                    return; // Exit if key generation fails
                }
                Log.d(TAG, "Generated key: " + studentKey);
                Student student = new Student(name, id, g, major); // Assuming Student class constructor matches these parameters

                Log.d(TAG, "Attempting to write to Firebase path: students/" + studentKey);
                dbRef.child(studentKey).setValue(student)
                        .addOnSuccessListener(aVoid -> {
                            Log.d(TAG, "Student saved successfully");
                            Toast.makeText(StudentEntryActivity.this, "Student Saved", Toast.LENGTH_SHORT).show();
                            clearForm();
                        })
                        .addOnFailureListener(e -> {
                            Log.e(TAG, "Failed to save student: " + e.getMessage(), e);
                            Toast.makeText(StudentEntryActivity.this, "Failed to save student: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        })
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "Task completed successfully");
                            } else {
                                Log.e(TAG, "Task failed: " + (task.getException() != null ? task.getException().getMessage() : "Unknown error"));
                            }
                        });
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