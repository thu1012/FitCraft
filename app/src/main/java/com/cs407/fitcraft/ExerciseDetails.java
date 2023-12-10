package com.cs407.fitcraft;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.VideoView;

public class ExerciseDetails extends AppCompatActivity {
    DatabaseHelper databaseHelper = new DatabaseHelper();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_details);
        String exerciseName = getIntent().getStringExtra("exerciseName");
        //exerciseName= databaseHelper.getExerciseName();
        if(exerciseName==null) exerciseName = "TestExercise";

        databaseHelper.getExerciseName(exerciseName, new DatabaseHelper.Callback<String>() {
            @Override
            public void onSuccess(String result) {
                // Enable the Up button
                if (getSupportActionBar() != null) {
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                    getSupportActionBar().setTitle(result);
                }
            }

            @Override
            public void onError(Exception e) {
                Log.e("Exercise Details", "Error loading exercise", e);
                if (getSupportActionBar() != null) {
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                    getSupportActionBar().setTitle("Default Exercise Name");
                }
            }
        });


        VideoView videoView = findViewById(R.id.exerciseDetailsVideoView);
        VideoHelper videoHelper = new VideoHelper(videoView, true,  this);
        videoHelper.setVideo(exerciseName, this);
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
