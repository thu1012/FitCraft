package com.cs407.fitcraft;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.core.app.TaskStackBuilder;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Button;

import java.util.ArrayList;
import java.util.Arrays;

public class NewWorkoutActivity extends AppCompatActivity {
    ArrayList<String> exerciseList;
    ListView exerciseListView;

    Button newWorkoutAddBtn;

    Button newWorkoutDoneBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_workout);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("*New Workout*");
        }
        exerciseList = new ArrayList<>(Arrays.asList("Exercise 1,Exercise 2,Exercise 3,Exercise 4,Exercise 5,Exercise 6,Exercise 7,Exercise 8,Exercise 9".split(",")));
        exerciseListView = findViewById(R.id.newWorkoutExerciseList);
        exerciseListView.setAdapter(new ExerciseAdaptor(exerciseList, getApplicationContext(), "newWorkout"));


        newWorkoutAddBtn = findViewById(R.id.newWorkoutAddBtn);
        newWorkoutAddBtn.setOnClickListener(view -> {
            Intent intent = new Intent(NewWorkoutActivity.this, AddExerciseActivity.class);
            startActivity(intent);
            finish();
        });

        newWorkoutDoneBtn = findViewById(R.id.newWorkoutDoneBtn);
        newWorkoutDoneBtn.setOnClickListener(view -> {
            Intent intent = new Intent(NewWorkoutActivity.this, FirstPage.class);
            startActivity(intent);
            finish();
        });
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            // Respond to the action bar's Up/Home button
            Intent intent = new Intent(NewWorkoutActivity.this, FirstPage.class);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}