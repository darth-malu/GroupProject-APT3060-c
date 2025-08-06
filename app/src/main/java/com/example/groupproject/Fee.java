package com.example.groupproject;

public class Fee {
    private String studentId;
    private double totalFees;
    private double feesPaid;
    private double balanceDue;
    private String balanceClearanceDate;

    // Default constructor (required for Firebase)
    public Fee() {
    }

    // Parameterized constructor
    public Fee(String studentId, double totalFees, double feesPaid, double balanceDue, String balanceClearanceDate) {
        this.studentId = studentId;
        this.totalFees = totalFees;
        this.feesPaid = feesPaid;
        this.balanceDue = balanceDue;
        this.balanceClearanceDate = balanceClearanceDate;
    }

    // Getter methods
    public String getStudentId() {
        return studentId;
    }

    public double getTotalFees() {
        return totalFees;
    }

    public double getFeesPaid() {
        return feesPaid;
    }

    public double getBalanceDue() {
        return balanceDue;
    }

    public String getBalanceClearanceDate() {
        return balanceClearanceDate;
    }

    // Setter methods
    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public void setTotalFees(double totalFees) {
        this.totalFees = totalFees;
        calculateBalanceDue();
    }

    public void setFeesPaid(double feesPaid) {
        this.feesPaid = feesPaid;
        calculateBalanceDue();
    }

    public void setBalanceDue(double balanceDue) {
        this.balanceDue = balanceDue;
    }

    public void setBalanceClearanceDate(String balanceClearanceDate) {
        this.balanceClearanceDate = balanceClearanceDate;
    }

    // Helper method to calculate balance due
    private void calculateBalanceDue() {
        this.balanceDue = this.totalFees - this.feesPaid;
    }
}
