package com.example.stridetrack;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class ProfileActivity extends AppCompatActivity {

    private static final String TAG = "ProfileActivity";

    private ImageView profileImage;
    private EditText heightInput;
    private EditText weightInput;
    private TextView dobText;
    private Button submitButton;

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        profileImage = findViewById(R.id.profileImage);
        heightInput = findViewById(R.id.heightInput);
        weightInput = findViewById(R.id.weightInput);
        dobText = findViewById(R.id.dobText);
        submitButton = findViewById(R.id.submitButton);


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("profiles");


        dobText.setOnClickListener(v -> showDatePickerDialog());


        submitButton.setOnClickListener(v -> saveProfileData());


        loadProfileData();
    }

    private void saveProfileData() {
        String height = heightInput.getText().toString().trim();
        String weight = weightInput.getText().toString().trim();
        String dob = dobText.getText().toString().trim();

        // Validate inputs
        if (TextUtils.isEmpty(height) || TextUtils.isEmpty(weight) || TextUtils.isEmpty(dob)) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }


        String profileId = databaseReference.push().getKey();
        Profile profile = new Profile(height, weight, dob);


        databaseReference.child(profileId).setValue(profile)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(ProfileActivity.this, "Profile saved", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "Profile saved successfully");
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(ProfileActivity.this, "Error saving profile", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "Error saving profile", e);
                });
    }


    private void loadProfileData() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Profile profile = snapshot.getValue(Profile.class);
                    if (profile != null) {
                        heightInput.setText(profile.getHeight());
                        weightInput.setText(profile.getWeight());
                        dobText.setText(profile.getDob());
                        Log.d(TAG, "Profile loaded: " + profile.toString());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ProfileActivity.this, "Failed to load profile", Toast.LENGTH_SHORT).show();
                Log.w(TAG, "Failed to read profile data.", error.toException());
            }
        });
    }


    public static class Profile {
        private String height;
        private String weight;
        private String dob;

        public Profile() {

        }

        public Profile(String height, String weight, String dob) {
            this.height = height;
            this.weight = weight;
            this.dob = dob;
        }

        public String getHeight() {
            return height;
        }

        public String getWeight() {
            return weight;
        }

        public String getDob() {
            return dob;
        }

        @Override
        public String toString() {
            return "Height: " + height + ", Weight: " + weight + ", DOB: " + dob;
        }
    }


    private void showDatePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    String selectedDate = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
                    dobText.setText(selectedDate);
                },
                year, month, day
        );
        datePickerDialog.show();
    }
}
