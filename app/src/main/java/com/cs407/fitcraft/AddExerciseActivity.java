package com.cs407.fitcraft;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class AddExerciseActivity extends AppCompatActivity {
    private ArrayList<String> exerciseList;
    private ExerciseAdaptor adaptor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_exercise);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Add Exercise");
        }

        exerciseList = new ArrayList<>();
        ListView exerciseListView = findViewById(R.id.addExerciseListView);
        adaptor = new ExerciseAdaptor(exerciseList, getApplicationContext(), "addExercise",
                getIntent().getStringExtra("workoutId"),
                getIntent().getStringExtra("workoutName"));
        exerciseListView.setAdapter(adaptor);

        loadExercisesFromDatabase();
    }

    private void loadExercisesFromDatabase() {
        DatabaseHelper databaseHelper = new DatabaseHelper();
        databaseHelper.loadExercises(new DatabaseHelper.Callback<ArrayList<String>>() {
            @Override
            public void onSuccess(ArrayList<String> result) {
                exerciseList.clear();
                exerciseList.addAll(result);
                adaptor.notifyDataSetChanged();
            }

            @Override
            public void onError(Exception e) {
                Log.e("add exercise", "Error loading exercises", e);
            }
        });
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
