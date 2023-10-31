package com.cs407.fitcraft;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;

public class AddExerciseActivity extends AppCompatActivity {
    ArrayList<String> exerciseList;
    ListView exerciseListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_exercise);

        exerciseList = new ArrayList<>(Arrays.asList("Found Exercise 1,Found Exercise 2,Found Exercise 3,Found Exercise 4,Found Exercise 5,Found Exercise 6,Found Exercise 7,Found Exercise 8,Found Exercise 9".split(",")));
        exerciseListView = findViewById(R.id.addExerciseListView);
        exerciseListView.setAdapter(new ExerciseAdaptor(exerciseList, getApplicationContext(), "addExercise"));

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Add Exercise");
        }
    }
}