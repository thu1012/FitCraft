package com.cs407.fitcraft;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ExerciseAdaptor extends BaseAdapter implements ListAdapter {
    private ArrayList<String> exerciseList;
    private Context context;
    private String pageName;
    private DatabaseHelper databaseHelper;

    private String workoutId;

    public ExerciseAdaptor(ArrayList<String> exerciseList, Context context, String pageName, String workoutId) {
        this.exerciseList = exerciseList;
        this.context = context;
        this.pageName = pageName;
        this.databaseHelper = new DatabaseHelper();
        this.workoutId = workoutId;
    }

    @Override
    public int getCount() {
        return exerciseList.size();
    }

    @Override
    public Object getItem(int pos) {
        return exerciseList.get(pos);
    }

    // TODO: this method need to be updated to use exercise id
    @Override
    public long getItemId(int pos) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.exercise_layout, null);
        }

        TextView exercise = view.findViewById(R.id.exerciseLayoutExerciseName);
        databaseHelper.getExercise(exerciseList.get(position), new DatabaseHelper.Callback<Exercise>() {
            @Override
            public void onSuccess(Exercise result) {
                exercise.setText(result.name);
            }

            @Override
            public void onError(Exception e) {
                Log.e("ExerciseAdaptor", "Error getting exercise", e);
            }
        });

        Button btn = view.findViewById(R.id.exerciseLayoutBtn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pageName.equals("newWorkout")) {
                    // Remove the item from the list
                    exerciseList.remove(position);
                    // Notify the adapter that the data set has changed
                    notifyDataSetChanged();
                } else if (pageName.equals("addExercise")) {
                    Intent intent = new Intent(context, ExerciseDetails.class);
                    intent.putExtra("workoutId", workoutId);
                    intent.putExtra("exerciseName", exerciseList.get(position));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                } else if (pageName.equals("firstPage")) {
                    Intent intent = new Intent(context, WorkoutPlay.class);
                    intent.putExtra("workoutId", exerciseList.get(position));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            }
        });
        if (pageName.equals("newWorkout")) {
            btn.setText("Remove");
        } else if (pageName.equals("addExercise")) {
            btn.setText("Details");
        } else if (pageName.equals("firstPage")){
            btn.setText("Play Workout");
        }
        return view;
    }
}
