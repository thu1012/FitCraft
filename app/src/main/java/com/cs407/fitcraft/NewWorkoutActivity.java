package com.cs407.fitcraft;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.UUID;

public class NewWorkoutActivity extends AppCompatActivity {
    private ArrayList<String> exerciseList;
    private ListView exerciseListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_workout);

        EditText workoutNameEditText = findViewById(R.id.newWorkoutEditText);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            String userEnteredText = workoutNameEditText.getText().toString();
            if ("".equals(userEnteredText)) userEnteredText = "Custom Workout";
            getSupportActionBar().setTitle(userEnteredText);
        }

        workoutNameEditText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE ||
                    (event != null && event.getAction() == KeyEvent.ACTION_DOWN &&
                            event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(workoutNameEditText.getWindowToken(), 0);
                updateActionBarTitle(workoutNameEditText.getText().toString());
                workoutNameEditText.clearFocus();
                return true;
            }
            return false;
        });

        String workoutId = getIntent().getStringExtra("workoutId");
        String workoutName = getIntent().getStringExtra("workoutName");
        exerciseList = new ArrayList<>();
        if (workoutId == null) {
            workoutId = UUID.randomUUID().toString();
        } else {
            String finalWorkoutId = workoutId;
            DatabaseHelper databaseHelper = new DatabaseHelper();
            databaseHelper.getWorkout(workoutId, new DatabaseHelper.Callback<Workout>() {
                @Override
                public void onSuccess(Workout result) {
                    exerciseList.addAll(result.exercises);
                    exerciseListView = findViewById(R.id.newWorkoutExerciseList);
                    exerciseListView.setAdapter(new ExerciseAdaptor(exerciseList, getApplicationContext(),
                            "newWorkout", finalWorkoutId, workoutName));
                    updateActionBarTitle(result.name);
                }

                @Override
                public void onError(Exception e) {
                    Log.e("new workout", "Failed to retrieve workout exercises", e);
                }
            });
        }

        Button newWorkoutAddBtn = findViewById(R.id.newWorkoutAddBtn);
        String finalWorkoutId = workoutId;
        newWorkoutAddBtn.setOnClickListener(view -> {
            Intent intent = new Intent(NewWorkoutActivity.this, AddExerciseActivity.class);
            intent.putExtra("workoutId", finalWorkoutId);
            intent.putExtra("workoutName", getSupportActionBar().getTitle());
            exerciseDetailsLauncher.launch(intent);
        });

        Button newWorkoutDoneBtn = findViewById(R.id.newWorkoutDoneBtn);
        newWorkoutDoneBtn.setOnClickListener(view -> {
            Intent intent = new Intent(NewWorkoutActivity.this, FirstPage.class);
            startActivity(intent);
            finish();
        });
    }

    private void updateActionBarTitle(String newTitle) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(newTitle);
        }
    }

    private final ActivityResultLauncher<Intent> exerciseDetailsLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    assert result.getData() != null;
                    String exerciseName = result.getData().getStringExtra("exerciseName");
                    exerciseList.add(exerciseName);
                    ((ExerciseAdaptor) exerciseListView.getAdapter()).notifyDataSetChanged();
                }
            }
    );

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            // Add the exercise name to the list
            exerciseList.add(data.getStringExtra("exerciseName"));
            // Notify the adapter that the data set has changed
            ((ExerciseAdaptor) exerciseListView.getAdapter()).notifyDataSetChanged();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            // Respond to the action bar's Up/Home button
            Intent intent = new Intent(NewWorkoutActivity.this, FirstPage.class);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
