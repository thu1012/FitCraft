package com.cs407.fitcraft;

import static java.security.AccessController.getContext;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.core.app.TaskStackBuilder;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Button;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;
import android.text.Editable;
import android.widget.TextView;


public class NewWorkoutActivity extends AppCompatActivity {
    ArrayList<String> exerciseList;
    ListView exerciseListView;
    Button newWorkoutAddBtn;
    Button newWorkoutDoneBtn;
    DatabaseHelper databaseHelper = new DatabaseHelper();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_workout);

        EditText editTextExample = findViewById(R.id.editTextExample);
        String userEnteredText = editTextExample.getText().toString();

        editTextExample.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE ||
                        (event != null && event.getAction() == KeyEvent.ACTION_DOWN &&
                                event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    // Close keyboard
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(editTextExample.getWindowToken(), 0);
                    // Remove focus from EditText
                    editTextExample.clearFocus();
                    return true; // Consume the event
                }
                return false; // Pass on the event to other listeners
            }
        });


        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(userEnteredText);
        }

        editTextExample.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int before, int count) {
                // Do something before the text changes (if needed)
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                // Do something when the text changes
                updateActionBarTitle(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // Do something after the text changes (if needed)
            }
        });

        String workoutId = getIntent().getStringExtra("workoutId");
        String workoutName = getIntent().getStringExtra("workoutName");
        exerciseList = new ArrayList<>();
        if(workoutId==null) {
            workoutId = UUID.randomUUID().toString();
        } else {
            String finalWorkoutId = workoutId;
            databaseHelper.getWorkout(workoutId, new DatabaseHelper.Callback<Workout>() {
                @Override
                public void onSuccess(Workout result) {
                    exerciseList.addAll(result.exercises);
                    exerciseListView = findViewById(R.id.newWorkoutExerciseList);
                    exerciseListView.setAdapter(new ExerciseAdaptor(exerciseList, getApplicationContext(), "newWorkout", finalWorkoutId, workoutName));
                    updateActionBarTitle(result.name);
                }

                @Override
                public void onError(Exception e) {
                    Log.e("New Workout", "Failed to retrieve workout exercises", e);
                }
            });
        }

        newWorkoutAddBtn = findViewById(R.id.newWorkoutAddBtn);
        String finalWorkoutId = workoutId;
        newWorkoutAddBtn.setOnClickListener(view -> {
            Intent intent = new Intent(NewWorkoutActivity.this, AddExerciseActivity.class);
            intent.putExtra("workoutId", finalWorkoutId);
            intent.putExtra("workoutName", getSupportActionBar().getTitle());
            exerciseDetailsLauncher.launch(intent);
        });

        newWorkoutDoneBtn = findViewById(R.id.newWorkoutDoneBtn);
        newWorkoutDoneBtn = findViewById(R.id.newWorkoutDoneBtn);
        newWorkoutDoneBtn.setOnClickListener(view -> {
            Intent intent = new Intent(NewWorkoutActivity.this, FirstPage.class);
            startActivity(intent);
            finish();
        });
    }

    private void updateActionBarTitle(String title) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(title);
        }
    }


    private ActivityResultLauncher<Intent> exerciseDetailsLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    Intent data = result.getData();
                    String exerciseName = data.getStringExtra("exerciseName");
                    Log.d("NewWorkoutActivity", "Received exercise: " + exerciseName);
                    exerciseList.add(exerciseName);
                    ((ExerciseAdaptor)exerciseListView.getAdapter()).notifyDataSetChanged();
                }
            }
    );


    private void handleExerciseDetailsResult(Intent data) {
        if (data != null) {
            // Get the exercise name from the result
            String exerciseName = data.getStringExtra("exerciseName");

            // Add the exercise name to the list
            exerciseList.add(exerciseName);
            // Notify the adapter that the data set has changed
            ((ExerciseAdaptor) exerciseListView.getAdapter()).notifyDataSetChanged();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            // Get the exercise name from the result
            String exerciseName = data.getStringExtra("exerciseName");

            // Add the exercise name to the list
            exerciseList.add(exerciseName);
            // Notify the adapter that the data set has changed
            ((ExerciseAdaptor) exerciseListView.getAdapter()).notifyDataSetChanged();
        }
    }

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
