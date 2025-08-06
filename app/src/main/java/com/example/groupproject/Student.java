package com.example.groupproject;

public class Student {
    private String name;
    private String id;
    private String gender;
    private String major;

    // Default constructor (required for Firebase)
    public Student() {
    }

    // Parameterized constructor
    public Student(String name, String id, String gender, String major) {
        this.name = name;
        this.id = id;
        this.gender = gender;
        this.major = major;
    }

    // Getter methods
    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getGender() {
        return gender;
    }

    public String getMajor() {
        return major;
    }

    // Setter methods
    public void setName(String name) {
        this.name = name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setMajor(String major) {
        this.major = major;
    }
}
