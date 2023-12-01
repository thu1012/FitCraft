package com.cs407.fitcraft;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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

        exerciseList = new ArrayList<>(Arrays.asList("Exercise 1,Exercise 2,Exercise 3,Exercise 4,Exercise 5,Exercise 6,Exercise 7,Exercise 8,Exercise 9".split(",")));
        exerciseListView = findViewById(R.id.newWorkoutExerciseList);
        exerciseListView.setAdapter(new ExerciseAdaptor(exerciseList, getApplicationContext(), "newWorkout"));

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("*New Workout*");
        }

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
}