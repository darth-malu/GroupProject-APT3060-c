package com.example.groupproject;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class FeeEntryActivity extends AppCompatActivity {
    EditText studentIdInput, totalFeesInput, feesPaidInput;
    TextView balanceDueDisplay, clearanceDateDisplay;
    Button saveFeeButton;
    DatabaseReference dbRef;
    
    // Calendar instance for date picker
    private Calendar selectedDate;
    private SimpleDateFormat dateFormatter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fee_entry);

        // Initialize views
        studentIdInput = findViewById(R.id.studentIdInput);
        totalFeesInput = findViewById(R.id.totalFeesInput);
        feesPaidInput = findViewById(R.id.feesPaidInput);
        balanceDueDisplay = findViewById(R.id.balanceDueDisplay);
        clearanceDateDisplay = findViewById(R.id.clearanceDateDisplay);
        saveFeeButton = findViewById(R.id.saveFeeButton);

        // Initialize Firebase database reference
        dbRef = FirebaseDatabase.getInstance("https://class-23f8f-default-rtdb.firebaseio.com/").getReference("fees");

        // Initialize date components
        selectedDate = Calendar.getInstance();
        dateFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

        // Set current date for balance clearance
        setCurrentDate();

        // Make clearance date clickable for date selection
        setupDatePicker();

        // Add text watchers for automatic balance calculation
        setupBalanceCalculation();

        // Set save button click listener
        saveFeeButton.setOnClickListener(v -> saveFeeData());
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
        } else if (id == R.id.menu_summary) {
            startActivity(new Intent(this, SummaryActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setCurrentDate() {
        String currentDate = dateFormatter.format(selectedDate.getTime());
        clearanceDateDisplay.setText("Balance Clearance Date: " + currentDate + " (Tap to change)");
    }

    private void setupDatePicker() {
        clearanceDateDisplay.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    selectedDate.set(Calendar.YEAR, year);
                    selectedDate.set(Calendar.MONTH, month);
                    selectedDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    
                    String selectedDateStr = dateFormatter.format(selectedDate.getTime());
                    clearanceDateDisplay.setText("Balance Clearance Date: " + selectedDateStr + " (Tap to change)");
                },
                selectedDate.get(Calendar.YEAR),
                selectedDate.get(Calendar.MONTH),
                selectedDate.get(Calendar.DAY_OF_MONTH)
            );
            
            // Set minimum date to today (optional - remove if you want past dates)
            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
            
            datePickerDialog.show();
        });
    }

    private void setupBalanceCalculation() {
        TextWatcher balanceWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                calculateBalance();
            }
        };

        totalFeesInput.addTextChangedListener(balanceWatcher);
        feesPaidInput.addTextChangedListener(balanceWatcher);
    }

    private void calculateBalance() {
        try {
            String totalFeesStr = totalFeesInput.getText().toString();
            String feesPaidStr = feesPaidInput.getText().toString();

            if (!totalFeesStr.isEmpty() && !feesPaidStr.isEmpty()) {
                double totalFees = Double.parseDouble(totalFeesStr);
                double feesPaid = Double.parseDouble(feesPaidStr);
                double balance = totalFees - feesPaid;

                balanceDueDisplay.setText(String.format("Balance Due: $%.2f", balance));
            } else {
                balanceDueDisplay.setText("Balance Due: $0.00");
            }
        } catch (NumberFormatException e) {
            balanceDueDisplay.setText("Balance Due: Invalid input");
        }
    }

    private void saveFeeData() {
        String studentId = studentIdInput.getText().toString().trim();
        String totalFeesStr = totalFeesInput.getText().toString().trim();
        String feesPaidStr = feesPaidInput.getText().toString().trim();

        // Validation
        if (studentId.isEmpty() || totalFeesStr.isEmpty() || feesPaidStr.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            double totalFees = Double.parseDouble(totalFeesStr);
            double feesPaid = Double.parseDouble(feesPaidStr);

            if (totalFees < 0 || feesPaid < 0) {
                Toast.makeText(this, "Fees cannot be negative", Toast.LENGTH_SHORT).show();
                return;
            }

            double balanceDue = totalFees - feesPaid;
            String selectedDateStr = dateFormatter.format(selectedDate.getTime());

            // Create Fee object
            Fee fee = new Fee(studentId, totalFees, feesPaid, balanceDue, selectedDateStr);

            // Save to Firebase using student ID as key
            dbRef.child(studentId).setValue(fee)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(FeeEntryActivity.this, "Fee data saved successfully", Toast.LENGTH_SHORT).show();
                        clearForm();
                    })
                    .addOnFailureListener(e -> 
                        Toast.makeText(FeeEntryActivity.this, "Failed to save fee data: " + e.getMessage(), Toast.LENGTH_SHORT).show());

        } catch (NumberFormatException e) {
            Toast.makeText(this, "Please enter valid numeric values", Toast.LENGTH_SHORT).show();
        }
    }

    private void clearForm() {
        studentIdInput.setText("");
        totalFeesInput.setText("");
        feesPaidInput.setText("");
        balanceDueDisplay.setText("Balance Due: $0.00");
        
        // Reset date to current date
        selectedDate = Calendar.getInstance();
        setCurrentDate();
    }
}
