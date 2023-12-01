package com.cs407.fitcraft;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;

public class FirstPage extends AppCompatActivity {
    Button createButton;
    ArrayList<String> exerciseList;
    ListView exerciseListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_page);

        exerciseList = new ArrayList<>(Arrays.asList("Workout 1,Workout 2,Workout 3,Workout 4,Workout 5,Workout 6,Workout 7,Workout 8,Workout 9".split(",")));
        exerciseListView = findViewById(R.id.ListofWorkouts);
        exerciseListView.setAdapter(new ExerciseAdaptor(exerciseList, getApplicationContext(), "firstPage"));


        createButton = findViewById(R.id.createButton);
        createButton.setOnClickListener(view -> {
            Intent intent = new Intent(FirstPage.this, NewWorkoutActivity.class);
            startActivity(intent);
            finish();
    });
    }
}