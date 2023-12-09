package com.cs407.fitcraft;

import androidx.annotation.WorkerThread;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.ListView;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WorkoutPlay extends AppCompatActivity {
    ArrayList<String> exerciseList;
    ListView exerciseListView;
    DatabaseHelper databaseHelper = new DatabaseHelper();

    Activity activity = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_play);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("*Play Workout*");
        }

        String workoutId = getIntent().getStringExtra("workoutId");
        if(workoutId==null) workoutId = "TestWorkout";
        databaseHelper.getWorkout(workoutId, new DatabaseHelper.Callback<Workout>() {
            @Override
            public void onSuccess(Workout result) {
                //exerciseList = new ArrayList<>(Arrays.asList(result));
                exerciseListView = findViewById(R.id.ListofWorkouts);
                exerciseListView.setAdapter(new WorkoutPlayAdaptor(result.exercises, activity, findViewById(R.id.workoutPlayVideoView)));
            }

            @Override
            public void onError(Exception e) {
                Log.e("Workout Play", "Error loading workout exercises", e);
            }
        });
        // exerciseList = new ArrayList<>();
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