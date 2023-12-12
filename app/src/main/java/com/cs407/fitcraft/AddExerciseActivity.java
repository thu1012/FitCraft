package com.cs407.fitcraft;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class AddExerciseActivity extends AppCompatActivity {
    private ArrayList<String> exerciseList;
    private ExerciseAdaptor adapter;
    private final DatabaseHelper databaseHelper = new DatabaseHelper();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_exercise);

        exerciseList = new ArrayList<>();

        ListView exerciseListView = findViewById(R.id.addExerciseListView);
        adapter = new ExerciseAdaptor(exerciseList, getApplicationContext(), "addExercise",
                getIntent().getStringExtra("workoutId"),
                getIntent().getStringExtra("workoutName"));
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
//            Intent intent = new Intent(AddExerciseActivity.this, FirstPage.class);
//            startActivity(intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
