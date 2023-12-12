package com.cs407.fitcraft;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ExerciseDetails extends AppCompatActivity {
    private DatabaseHelper databaseHelper;
    private List<String> exercises;
    private Map<String, Object> workoutData = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_details);

        String exerciseId = getIntent().getStringExtra("exerciseId");
        if (exerciseId == null) {
            Log.e("ExerciseDetails", "No exercise ID provided");
            finish(); // Close the activity or display an error message
            return;
        }

        databaseHelper = new DatabaseHelper();
        setupExerciseDetails(exerciseId);
        setupAddButton(exerciseId);
    }

    private void setupExerciseDetails(String exerciseId) {
        databaseHelper.getExercise(exerciseId, new DatabaseHelper.Callback<Exercise>() {
            @Override
            public void onSuccess(Exercise result) {
                setTitleAndDescription(result);
                setupVideoView(exerciseId);
            }

            @Override
            public void onError(Exception e) {
                Log.e("ExerciseDetails", "Error loading exercise", e);
                displayError();
            }
        });
    }

    private void setTitleAndDescription(Exercise exercise) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(exercise.name);
        }
        TextView textView = findViewById(R.id.exerciseDetailsTextView);
        textView.setText(exercise.description);
    }

    private void setupVideoView(String exerciseID) {
        VideoView videoView = findViewById(R.id.exerciseDetailsVideoView);
        VideoHelper videoHelper = new VideoHelper(videoView, true, this);
        videoHelper.setVideo(exerciseID, this);
    }

    private void displayError() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Error Loading Exercise");
        }
    }

    private void setupAddButton(String exerciseId) {
        Button addButton = findViewById(R.id.exerciseDetailsButton);
        addButton.setOnClickListener(v -> {
            String workoutId = getIntent().getStringExtra("workoutId");
            String workoutName = getIntent().getStringExtra("workoutName");
            if (workoutName == null) workoutName = "Default Workout Name";

            exercises = new ArrayList<>();
            String finalWorkoutName = workoutName;
            databaseHelper.getWorkout(workoutId, new DatabaseHelper.Callback<Workout>() {
                @Override
                public void onSuccess(Workout result) {
                    exercises.addAll(result.exercises);
                    exercises.add(exerciseId); // Adding the current exercise ID

                    workoutData.put("description", "Self defined workout");
                    workoutData.put("name", finalWorkoutName);
                    workoutData.put("exercises", exercises);

                    databaseHelper.writeWorkout(workoutData, workoutId, new DatabaseHelper.Callback<Workout>() {
                        @Override
                        public void onSuccess(Workout result) {
                            Intent intent = new Intent(ExerciseDetails.this, NewWorkoutActivity.class);
                            intent.putExtra("workoutId", workoutId);
                            intent.putExtra("workoutName", finalWorkoutName);
                            startActivity(intent);
                        }

                        @Override
                        public void onError(Exception e) {
                            Log.e("ExerciseDetails", "Error writing workout", e);
                        }
                    });
                }

                @Override
                public void onError(Exception e) {
                    Log.e("ExerciseDetails", "Failed to retrieve workout exercises", e);
                }
            });
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}



