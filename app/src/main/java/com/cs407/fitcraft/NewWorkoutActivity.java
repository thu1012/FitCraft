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

        exerciseList = new ArrayList<String>(Arrays.asList("111,222,333,444,555,666".split(",")));
        exerciseListView = findViewById(R.id.newWorkoutExerciseList);
        exerciseListView.setAdapter(new ExerciseAdaptor(exerciseList, getApplicationContext(), "newWorkout"));
    }
}