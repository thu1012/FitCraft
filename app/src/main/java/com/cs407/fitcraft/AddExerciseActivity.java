package com.cs407.fitcraft;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;

public class AddExerciseActivity extends AppCompatActivity {
    ArrayList<String> exerciseList;
    ListView exerciseListView;

    ExerciseAdaptor adapter;
    DatabaseHelper databaseHelper = new DatabaseHelper();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_exercise);

        // Initialize exercise list
        exerciseList = new ArrayList<>();

        // Initialize ListView and Adapter
        exerciseListView = findViewById(R.id.addExerciseListView);
        adapter = new ExerciseAdaptor(exerciseList, getApplicationContext(), "addExercise");
        exerciseListView.setAdapter(adapter);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Add Exercise");
        }

        // Now load exercises from database
        loadExercisesFromDatabase();
    }


    private void loadExercisesFromDatabase() {
        databaseHelper.loadExercises(new DatabaseHelper.Callback<ArrayList<String>>() {
            @Override
            public void onSuccess(ArrayList<String> result) {
                exerciseList.clear();
                exerciseList.addAll(result);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError(Exception e) {
                // Handle any errors here
                Log.e("AddExerciseActivity", "Error loading exercises", e);
            }
        });
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