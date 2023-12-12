package com.cs407.fitcraft;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;


public class FirstPage extends AppCompatActivity {
    private ListView workoutListView;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_page);

        context = this;
        DatabaseHelper databaseHelper = new DatabaseHelper();

        Button createButton = findViewById(R.id.createButton);
        createButton.setOnClickListener(view -> {
            Intent intent = new Intent(FirstPage.this, NewWorkoutActivity.class);
            startActivity(intent);
            finish();
        });

        databaseHelper.getWorkoutIdList(new DatabaseHelper.Callback<List<String>>() {
            @Override
            public void onSuccess(List<String> result) {
                workoutListView = findViewById(R.id.ListofWorkouts);
                workoutListView.setAdapter(new WorkoutAdaptor(result, context));
            }

            @Override
            public void onError(Exception e) {
                Log.e("first page", "Error loading workout id list", e);
            }
        });
    }
}
