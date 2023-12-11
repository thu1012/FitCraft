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

        exerciseList = new ArrayList<>();
        exerciseListView = findViewById(R.id.ListofWorkouts);
        exerciseListView.setAdapter(new ExerciseAdaptor(exerciseList, getApplicationContext(), "firstPage", ""));


        createButton = findViewById(R.id.createButton);
        createButton.setOnClickListener(view -> {
            Intent intent = new Intent(FirstPage.this, NewWorkoutActivity.class);
            startActivity(intent);
            finish();
    });
    }
}