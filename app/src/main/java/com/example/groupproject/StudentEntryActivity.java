package com.example.groupproject;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class StudentEntryActivity extends AppCompatActivity {
    EditText fullName, idNumber, gender, courseMajor;
    Button saveButton;
    DatabaseReference dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_entry);

        fullName = findViewById(R.id.fullNameInput);
        idNumber = findViewById(R.id.idNumberInput);
        gender = findViewById(R.id.genderInput);
        courseMajor = findViewById(R.id.courseMajorInput);
        saveButton = findViewById(R.id.saveButton);

        dbRef = FirebaseDatabase.getInstance().getReference("students");

        saveButton.setOnClickListener(v -> {
            String name = fullName.getText().toString();
            String id = idNumber.getText().toString();
            String g = gender.getText().toString();
            String major = courseMajor.getText().toString();

            String studentId = dbRef.push().getKey();
            Student student = new Student(name, id, g, major);

            dbRef.child(studentId).setValue(student);
            Toast.makeText(this, "Student Saved", Toast.LENGTH_SHORT).show();
        });
    }
}
