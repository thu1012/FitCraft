package com.cs407.fitcraft;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.List;

public class FirstPage extends AppCompatActivity {
    Button createButton;
    ArrayList<String> workoutList;
    ListView workoutListView;
    Context context = this;
    DatabaseHelper databaseHelper = new DatabaseHelper();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_page);

//        exerciseList = new ArrayList<>();
//        exerciseListView = findViewById(R.id.ListofWorkouts);
//        exerciseListView.setAdapter(new ExerciseAdaptor(exerciseList, getApplicationContext(), "firstPage"));


        createButton = findViewById(R.id.createButton);
        createButton.setOnClickListener(view -> {
            Intent intent = new Intent(FirstPage.this, NewWorkoutActivity.class);
            startActivity(intent);
            finish();
        });

        context = this;

        databaseHelper.getWorkoutIdList(new DatabaseHelper.Callback<List<String>>() {
            @Override
            public void onSuccess(List<String> result) {
                workoutListView = findViewById(R.id.ListofWorkouts);
                workoutListView.setAdapter(new WorkoutAdaptor(result, context));
            }

            @Override
            public void onError(Exception e) {
                Log.e("Workout list", "Error loading workout list", e);
            }
        });


//新添加
//        String workoutId = getIntent().getStringExtra("workoutName");
//        //exerciseName= databaseHelper.getExerciseName();
//        if(workoutId==null) workoutId = "TestWorkout";
//        databaseHelper.getWorkoutName(workoutId, new DatabaseHelper.Callback<String>() {
//            @Override
//            public void onSuccess(String result) {
//                // Enable the Up button
//            }
//            @Override
//            public void onError(Exception e) {
//                Log.e("Exercise Details", "Error loading exercise", e);
//            }
//        });


    }

}



