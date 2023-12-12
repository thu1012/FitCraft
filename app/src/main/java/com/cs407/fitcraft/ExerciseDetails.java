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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_details);

        String exerciseName = getIntent().getStringExtra("exerciseName");
        if (exerciseName == null) exerciseName = "TestExercise";

        databaseHelper = new DatabaseHelper();
        databaseHelper.getExercise(exerciseName, new DatabaseHelper.Callback<Exercise>() {
            @Override
            public void onSuccess(Exercise result) {
                if (getSupportActionBar() != null) {
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                    getSupportActionBar().setTitle(result.name);
                }
                TextView textView = findViewById(R.id.exerciseDetailsTextView);
                textView.setText(result.description);
            }

            @Override
            public void onError(Exception e) {
                Log.e("exercise details", "Error loading exercise", e);
                if (getSupportActionBar() != null) {
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                    getSupportActionBar().setTitle("Default Exercise Name");
                }
            }
        });

        VideoView videoView = findViewById(R.id.exerciseDetailsVideoView);
        VideoHelper videoHelper = new VideoHelper(videoView, true, this);
        videoHelper.setVideo(exerciseName, this);

        String finalExerciseName = exerciseName;
        Log.d("finalExerciseName", finalExerciseName);
        Map<String, Object> workoutData = new HashMap<>();
        workoutData.put("description", "Self defined workout");

        String workoutName = getIntent().getStringExtra("workoutName");
        if (workoutName == null) workoutData.put("name", "Default Workout Name");
        else workoutData.put("name", workoutName);

        exercises = new ArrayList<>();
        databaseHelper.getWorkout(getIntent().getStringExtra("workoutId"), new DatabaseHelper.Callback<Workout>() {
            @Override
            public void onSuccess(Workout result) {
                Log.i("ExerciseDetail", "Successfully retrieved old exercises");
                exercises.add(finalExerciseName);
                if (result.exercises != null) {
                    exercises.addAll(result.exercises);
                }
            }

            @Override
            public void onError(Exception e) {
                Log.e("New Workout", "Failed to retrieve workout exercises", e);
            }
        });

        workoutData.put("exercises", exercises);

        Button addButton = findViewById(R.id.exerciseDetailsButton);
        addButton.setOnClickListener(v -> {
            Log.d("ExerciseDetails", "Add button clicked with exercise: " + finalExerciseName);
            databaseHelper.writeWorkout(workoutData, getIntent().getStringExtra("workoutId"), new DatabaseHelper.Callback<Workout>() {
                @Override
                public void onSuccess(Workout result) {
                    Intent intent = new Intent(ExerciseDetails.this, NewWorkoutActivity.class);
                    intent.putExtra("workoutId", getIntent().getStringExtra("workoutId"));
                    intent.putExtra("workoutName", getIntent().getStringExtra("workoutName"));
                    Log.d("ExerciseDetails", "Workout successfully written!");
                    startActivity(intent);
                }

                @Override
                public void onError(Exception e) {
                    Log.e("Workout Play", "Error loading workout exercises", e);
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


