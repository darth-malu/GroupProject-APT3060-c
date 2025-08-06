# University Student Management App

**Course:** APT3060 – Mobile Programming  
**Platform:** Android  
**Database:** Firebase Realtime Database

## 📱 Application Overview

This Android application is designed to manage university student data and fee information. It provides a comprehensive solution for student registration, fee management, and data retrieval.

## ✨ Features

### 1. **Student Entry** (`StudentEntryActivity`)
- Register new students with their personal details
- Input fields: Full Name, Student ID, Gender, Course/Major
- Data validation and error handling
- Stores student information in Firebase Realtime Database

### 2. **Fee Entry** (`FeeEntryActivity`)
- Enter fee information for registered students
- Input fields: Student ID, Total Fees, Fees Paid
- Auto-calculated Balance Due (Total Fees - Fees Paid)
- Auto-filled current date for balance clearance
- Real-time balance calculation as user types
- Stores fee information linked to Student ID

### 3. **Summary View** (`SummaryActivity`)
- Search and display combined student + fee information
- Query by Student ID
- Clean, card-based UI using Material Design
- Shows complete student profile with fee status
- Color-coded balance status (red for outstanding, green for paid)

## 🏗️ Technical Implementation

### **Architecture**
- **Model Classes:** `Student.java`, `Fee.java`
- **Activities:** `StudentEntryActivity`, `FeeEntryActivity`, `SummaryActivity`
- **Database:** Firebase Realtime Database
- **UI Framework:** Material Design Components

### **Database Structure**
```
Firebase Realtime Database
├── students/
│   ├── [auto-generated-key]/
│   │   ├── name: "John Doe"
│   │   ├── id: "STU001"
│   │   ├── gender: "Male"
│   │   └── major: "Computer Science"
└── fees/
    └── [student-id]/
        ├── studentId: "STU001"
        ├── totalFees: 5000.0
        ├── feesPaid: 3000.0
        ├── balanceDue: 2000.0
        └── balanceClearanceDate: "06/08/2025"
```

### **Key Features**
- **Navigation:** Menu-based navigation between all activities
- **Validation:** Input validation with user-friendly error messages
- **Real-time Updates:** Live balance calculation in Fee Entry
- **Error Handling:** Comprehensive Firebase error handling
- **Material Design:** Clean, modern UI following Material Design guidelines

## 🚀 Setup Instructions

### **Prerequisites**
1. Android Studio Arctic Fox or later
2. Firebase project with Realtime Database enabled
3. `google-services.json` file (place in `app/` directory)

### **Installation**
1. Clone the repository
2. Open in Android Studio
3. Add your `google-services.json` file to the `app/` directory
4. Sync project with Gradle files
5. Run the application

### **Firebase Setup**
1. Create a Firebase project at [console.firebase.google.com](https://console.firebase.google.com)
2. Enable Realtime Database
3. Set database rules to allow read/write (for development):
   ```json
   {
     "rules": {
       ".read": true,
       ".write": true
     }
   }
   ```
4. Download `google-services.json` and place in `app/` directory

## 📋 Usage

1. **Adding Students:** Use the Student Entry screen to register new students
2. **Recording Fees:** Navigate to Fee Entry to input fee information
3. **Viewing Data:** Use the Summary screen to search and view complete student profiles

## 🔧 Dependencies

- androidx.appcompat:appcompat:1.6.1
- com.google.android.material:material:1.10.0
- androidx.cardview:cardview:1.0.0
- Firebase BOM:32.3.1
- Firebase Realtime Database
- Firebase Analytics

## 📝 Assignment Requirements Fulfilled

✅ **Fixed Issues:**
- Updated Student.java constructor to properly store parameters
- Added getter/setter methods for all fields

✅ **FeeEntryActivity Implementation:**
- Form with Total Fees, Fees Paid, auto-calculated Balance Due
- Auto-filled current date for balance clearance
- Firebase integration for fee data storage
- Fee.java model class with appropriate fields

✅ **SummaryActivity Implementation:**
- Retrieves combined student + fee information
- Queries Firebase for both student and fee data
- Clean UI with CardView components
- Proper error handling for database interactions

✅ **Additional Features:**
- All activities declared in AndroidManifest.xml
- Material Design guidelines followed
- Navigation menu between all activities
- Comprehensive error handling
- Input validation throughout the app

## 👥 Development Team

**Course:** APT3060 – Mobile Programming  
**Institution:** University Assignment  

---

*This application demonstrates practical implementation of Android development concepts including Activities, Firebase integration, Material Design, and database operations.*
