package com.cs407.fitcraft;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class WorkoutPlay extends AppCompatActivity {
    private ListView exerciseListView;
    private TextView descriptionTextView;
    private final Activity activity = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_play);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Loading");
        }

        String workoutId = getIntent().getStringExtra("workoutId");
        if (workoutId == null) workoutId = "TestWorkout";

        DatabaseHelper databaseHelper = new DatabaseHelper();
        databaseHelper.getWorkout(workoutId, new DatabaseHelper.Callback<Workout>() {
            @Override
            public void onSuccess(Workout result) {
                exerciseListView = findViewById(R.id.workoutPlayListView);
                exerciseListView.setAdapter(new WorkoutPlayAdaptor(result.exercises, activity,
                        findViewById(R.id.workoutPlayVideoView)));
                if (getSupportActionBar() != null) {
                    getSupportActionBar().setTitle(result.name);
                }
                descriptionTextView = findViewById(R.id.workoutPlayTextView);
                descriptionTextView.setText(result.description);
            }

            @Override
            public void onError(Exception e) {
                Log.e("workout play", "Error loading workout exercises", e);
            }
        });
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(WorkoutPlay.this, FirstPage.class);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}