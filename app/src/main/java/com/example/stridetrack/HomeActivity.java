package com.example.stridetrack;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class HomeActivity extends AppCompatActivity implements SensorEventListener {
    private HalfCirclePedometerView pedometerView;
    private SensorManager sensorManager;
    private Sensor stepCounterSensor;
    private int stepCount = 0;
    private static final int STEP_GOAL = 10000;
    private TextView stepsCountText, timeText, caloriesText, distanceText;
    private SharedPreferences sharedPreferences;
    private static final String PREFS_NAME = "StrideTrackPrefs";
    private static final String LAST_RESET_DATE = "last_reset_date";

    // Constants for metrics calculations
    private static final double STEP_LENGTH_CM = 78;
    private static final double CM_TO_KM = 0.00001;
    private static final double CALORIES_PER_STEP = 0.04;
    private static final double MINUTES_PER_STEP = 0.005;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        pedometerView = findViewById(R.id.halfCirclePedometer);
        stepsCountText = findViewById(R.id.stepsCountText);
        timeText = findViewById(R.id.timeText);
        caloriesText = findViewById(R.id.caloriesText);
        distanceText = findViewById(R.id.distanceText);
        ImageButton profileButton = findViewById(R.id.profileButton);


        profileButton.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, ProfileActivity.class);
            startActivity(intent);
        });


        sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        checkAndResetStepCount();


        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        stepCounterSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (stepCounterSensor != null) {
            sensorManager.registerListener(this, stepCounterSensor, SensorManager.SENSOR_DELAY_UI);
        } else {
            Toast.makeText(this, "Step Counter Sensor not available!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_STEP_COUNTER) {
            stepCount = (int) event.values[0];
            pedometerView.setCurrentSteps(stepCount);
            updateMetrics();
        }
    }


    private void updateMetrics() {

        double distanceKm = (stepCount * STEP_LENGTH_CM) * CM_TO_KM;


        double caloriesBurned = stepCount * CALORIES_PER_STEP;


        double activeMinutes = stepCount * MINUTES_PER_STEP;


        stepsCountText.setText("Steps: " + stepCount + " / " + STEP_GOAL);
        distanceText.setText(String.format(Locale.getDefault(), "%.2f km", distanceKm));
        caloriesText.setText(String.format(Locale.getDefault(), "%.2f Kcal", caloriesBurned));
        timeText.setText(String.format(Locale.getDefault(), "%.0f mins", activeMinutes));
    }


    private void checkAndResetStepCount() {
        String lastResetDate = sharedPreferences.getString(LAST_RESET_DATE, "");
        String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        if (!currentDate.equals(lastResetDate)) {
            stepCount = 0;
            pedometerView.setCurrentSteps(stepCount);
            updateMetrics();
            sharedPreferences.edit().putString(LAST_RESET_DATE, currentDate).apply();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
