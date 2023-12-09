package com.cs407.fitcraft;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;

public class WorkoutPlay extends AppCompatActivity {
    ArrayList<String> exerciseList;
    ListView exerciseListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_play);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("*Play Workout*");
        }

        exerciseList = new ArrayList<>(Arrays.asList("TestExercise,TestVideo,TestVideo2".split(",")));
        exerciseListView = findViewById(R.id.ListofWorkouts);
        exerciseListView.setAdapter(new WorkoutPlayAdaptor(exerciseList, this, findViewById(R.id.workoutPlayVideoView)));
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            // Respond to the action bar's Up/Home button
            Intent intent = new Intent(WorkoutPlay.this, FirstPage.class);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}