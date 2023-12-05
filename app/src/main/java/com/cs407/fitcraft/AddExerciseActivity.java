package com.cs407.fitcraft;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import java.util.ArrayList;
import java.util.Arrays;

public class AddExerciseActivity extends AppCompatActivity {
    ArrayList<String> exerciseList;
    ListView exerciseListView;

    ArrayAdapter<String> adapter;


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



    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            // Respond to the action bar's Up/Home button
            Intent intent = new Intent(AddExerciseActivity.this, NewWorkoutActivity.class);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}