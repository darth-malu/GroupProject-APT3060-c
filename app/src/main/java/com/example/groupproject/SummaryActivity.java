package com.example.groupproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SummaryActivity extends AppCompatActivity {
    EditText searchStudentIdInput;
    Button searchButton;
    LinearLayout summaryContainer;
    CardView studentInfoCard, feeInfoCard;
    TextView studentNameText, studentIdText, studentGenderText, studentMajorText;
    TextView totalFeesText, feesPaidText, balanceDueText, clearanceDateText;
    TextView noDataMessage;

    DatabaseReference studentsRef, feesRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);

        // Initialize views
        initializeViews();

        // Initialize Firebase references
        studentsRef = FirebaseDatabase.getInstance().getReference("students");
        feesRef = FirebaseDatabase.getInstance().getReference("fees");

        // Set search button click listener
        searchButton.setOnClickListener(v -> searchStudentData());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_student_entry) {
            startActivity(new Intent(this, StudentEntryActivity.class));
            return true;
        } else if (id == R.id.menu_fee_entry) {
            startActivity(new Intent(this, FeeEntryActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void initializeViews() {
        searchStudentIdInput = findViewById(R.id.searchStudentIdInput);
        searchButton = findViewById(R.id.searchButton);
        summaryContainer = findViewById(R.id.summaryContainer);
        studentInfoCard = findViewById(R.id.studentInfoCard);
        feeInfoCard = findViewById(R.id.feeInfoCard);
        noDataMessage = findViewById(R.id.noDataMessage);

        // Student info TextViews
        studentNameText = findViewById(R.id.studentNameText);
        studentIdText = findViewById(R.id.studentIdText);
        studentGenderText = findViewById(R.id.studentGenderText);
        studentMajorText = findViewById(R.id.studentMajorText);

        // Fee info TextViews
        totalFeesText = findViewById(R.id.totalFeesText);
        feesPaidText = findViewById(R.id.feesPaidText);
        balanceDueText = findViewById(R.id.balanceDueText);
        clearanceDateText = findViewById(R.id.clearanceDateText);
    }

    private void searchStudentData() {
        String studentId = searchStudentIdInput.getText().toString().trim();

        if (studentId.isEmpty()) {
            Toast.makeText(this, "Please enter a Student ID", Toast.LENGTH_SHORT).show();
            return;
        }

        // Hide previous results
        hideAllCards();
        
        // Search for student and fee data
        searchStudentInfo(studentId);
        searchFeeInfo(studentId);
    }

    private void searchStudentInfo(String studentId) {
        studentsRef.orderByChild("id").equalTo(studentId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Student student = snapshot.getValue(Student.class);
                        if (student != null) {
                            displayStudentInfo(student);
                            return;
                        }
                    }
                }
                showNoStudentDataMessage();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(SummaryActivity.this, "Error retrieving student data: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void searchFeeInfo(String studentId) {
        feesRef.child(studentId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Fee fee = dataSnapshot.getValue(Fee.class);
                    if (fee != null) {
                        displayFeeInfo(fee);
                        return;
                    }
                }
                showNoFeeDataMessage();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(SummaryActivity.this, "Error retrieving fee data: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displayStudentInfo(Student student) {
        studentNameText.setText("Name: " + student.getName());
        studentIdText.setText("Student ID: " + student.getId());
        studentGenderText.setText("Gender: " + student.getGender());
        studentMajorText.setText("Major: " + student.getMajor());
        
        studentInfoCard.setVisibility(View.VISIBLE);
        summaryContainer.setVisibility(View.VISIBLE);
    }

    private void displayFeeInfo(Fee fee) {
        totalFeesText.setText(String.format("Total Fees: $%.2f", fee.getTotalFees()));
        feesPaidText.setText(String.format("Fees Paid: $%.2f", fee.getFeesPaid()));
        balanceDueText.setText(String.format("Balance Due: $%.2f", fee.getBalanceDue()));
        clearanceDateText.setText("Clearance Date: " + fee.getBalanceClearanceDate());
        
        // Set color based on balance due
        if (fee.getBalanceDue() > 0) {
            balanceDueText.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
        } else {
            balanceDueText.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
        }
        
        feeInfoCard.setVisibility(View.VISIBLE);
        summaryContainer.setVisibility(View.VISIBLE);
    }

    private void showNoStudentDataMessage() {
        noDataMessage.setText("No student data found for the entered ID");
        noDataMessage.setVisibility(View.VISIBLE);
        summaryContainer.setVisibility(View.VISIBLE);
    }

    private void showNoFeeDataMessage() {
        // If student info is already visible, just show a message about missing fee data
        if (studentInfoCard.getVisibility() == View.VISIBLE) {
            Toast.makeText(this, "No fee data found for this student", Toast.LENGTH_SHORT).show();
        } else {
            noDataMessage.setText("No data found for the entered Student ID");
            noDataMessage.setVisibility(View.VISIBLE);
            summaryContainer.setVisibility(View.VISIBLE);
        }
    }

    private void hideAllCards() {
        studentInfoCard.setVisibility(View.GONE);
        feeInfoCard.setVisibility(View.GONE);
        noDataMessage.setVisibility(View.GONE);
        summaryContainer.setVisibility(View.GONE);
    }
}
