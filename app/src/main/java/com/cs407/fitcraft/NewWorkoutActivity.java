package com.cs407.fitcraft;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;

public class NewWorkoutActivity extends AppCompatActivity {
    ArrayList<String> exerciseList;
    ListView exerciseListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_workout);

        exerciseList = new ArrayList<>(Arrays.asList("Exercise 1,Exercise 2,Exercise 3,Exercise 4,Exercise 5,Exercise 6,Exercise 7,Exercise 8,Exercise 9".split(",")));
        exerciseListView = findViewById(R.id.newWorkoutExerciseList);
        exerciseListView.setAdapter(new ExerciseAdaptor(exerciseList, getApplicationContext(), "newWorkout"));
    }
}