package com.cs407.fitcraft;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
public class WorkoutAdaptor extends BaseAdapter implements ListAdapter {

    private List<String> workouts;
    private Context context;
    private DatabaseHelper databaseHelper = new DatabaseHelper();


    public WorkoutAdaptor(List<String> workouts, Context context) {
        this.workouts = new ArrayList<>();
        this.workouts.addAll(workouts);
        this.context = context;
    }
    @Override
    public int getCount() {
        return workouts.size();
    }

    @Override
    public Object getItem(int i) {
        return workouts.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.exercise_layout, null);
        }

        TextView workoutTextView = view.findViewById(R.id.exerciseLayoutExerciseName);
        TextView workoutDescriptionTextView = view.findViewById(R.id.exerciseLayoutExerciseDescription);
        databaseHelper.getWorkout(workouts.get(position), new DatabaseHelper.Callback<Workout>() {
            @Override
            public void onSuccess(Workout workout) {
                workoutTextView.setText(workout.name);
                workoutDescriptionTextView.setText(workout.description);
            }

            @Override
            public void onError(Exception e) {
                Log.e("workout adaptor", "failed to retrieve workout name", e);
            }
        });
//        databaseHelper.getWorkoutIdList(new DatabaseHelper.Callback<List<String>>() {
//            @Override
//            public void onSuccess(List<String> result) {
//                workoutTextView.setText(result.get(position));
//            }
//
//            @Override
//            public void onError(Exception e) {
//                Log.e("ExerciseAdaptor", "Error getting exercise", e);
//            }
//        });

        Button btn = view.findViewById(R.id.exerciseLayoutBtn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, WorkoutPlay.class);
                intent.putExtra("workoutId", workouts.get(position));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
        btn.setText("Play Workout");
        return view;
    }
}
