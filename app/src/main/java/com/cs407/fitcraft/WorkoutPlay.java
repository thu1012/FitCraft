package com.cs407.fitcraft;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;

public class WorkoutPlay extends AppCompatActivity {
    ArrayList<String> exerciseList;
    ListView exerciseListView;
    ImageButton returnButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_play);

        exerciseList = new ArrayList<>(Arrays.asList("Exercise 1,Exercise 2,Exercise 3,Exercise 4,Exercise 5,Exercise 6,Exercise 7,Exercise 8,Exercise 9".split(",")));
        exerciseListView = findViewById(R.id.ListofWorkouts);
        exerciseListView.setAdapter(new ExerciseAdapter(exerciseList, getApplicationContext(), "newWorkout"));

        returnButton = findViewById(R.id.returnButton);
        returnButton.setOnClickListener(view -> {
            Intent intent = new Intent(WorkoutPlay.this, FirstPage.class);
            startActivity(intent);
            finish();
        });
    }
}