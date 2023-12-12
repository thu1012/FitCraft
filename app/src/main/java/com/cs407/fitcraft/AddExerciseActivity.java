package com.cs407.fitcraft;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
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

        exerciseList = new ArrayList<>();

        exerciseListView = findViewById(R.id.addExerciseListView);
        adapter = new ExerciseAdaptor(exerciseList, getApplicationContext(), "addExercise",
                getIntent().getStringExtra("workoutId"),
                getIntent().getStringExtra("workoutName"));
        exerciseListView.setAdapter(adapter);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Add Exercise");
        }
        loadExercisesFromDatabase();

        SearchView searchView = findViewById(R.id.addExerciseSearchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return true;
            }
        });
    }



    private void loadExercisesFromDatabase() {
        databaseHelper.loadExercises(new DatabaseHelper.Callback<ArrayList<String>>() {
            @Override
            public void onSuccess(ArrayList<String> result) {
                adapter.updateLists(result);
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
