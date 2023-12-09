package com.cs407.fitcraft;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.VideoView;
import android.util.Log;

public class ExerciseDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_details);

        String exerciseName = getIntent().getStringExtra("exerciseName");
        if(exerciseName==null) exerciseName = "TestExercise";

        // Enable the Up button
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("*"+exerciseName+"*");
        }

        VideoView videoView = findViewById(R.id.exerciseDetailsVideoView);
        VideoHelper videoHelper = new VideoHelper(videoView, true,  this);
        videoHelper.setVideo(exerciseName, this);

        Button addButton = findViewById(R.id.addButton);
        String finalExerciseName = exerciseName;
        addButton.setOnClickListener(v -> {
            Log.d("ExerciseDetails", "Add button clicked with exercise: " + finalExerciseName);
            Intent returnIntent = new Intent();
            returnIntent.putExtra("exerciseName", finalExerciseName);
            setResult(Activity.RESULT_OK, returnIntent);
            finish();
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            // Respond to the action bar's Up/Home button
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
