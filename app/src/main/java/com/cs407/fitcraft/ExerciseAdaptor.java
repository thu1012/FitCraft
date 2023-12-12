package com.cs407.fitcraft;

import android.annotation.SuppressLint;
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
import java.util.HashMap;
import java.util.Map;

public class ExerciseAdaptor extends BaseAdapter implements ListAdapter {
    private final ArrayList<String> exerciseList;
    private final Context context;
    private final String pageName;
    private final String workoutId;
    private final String workoutName;
    private final DatabaseHelper databaseHelper;

    public ExerciseAdaptor(ArrayList<String> exerciseList, Context context, String pageName, String workoutId, String workoutName) {
        this.exerciseList = exerciseList;
        this.context = context;
        this.pageName = pageName;
        this.workoutId = workoutId;
        this.workoutName = workoutName;
        this.databaseHelper = new DatabaseHelper();
    }

    @Override
    public int getCount() {
        return exerciseList.size();
    }

    @Override
    public Object getItem(int pos) { return exerciseList.get(pos); }

    @Override
    public long getItemId(int pos) {
        return 0;
    }

    @SuppressLint({"InflateParams", "SetTextI18n"})
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.adaptor_exercise_layout, null);
        }

        TextView exercise = view.findViewById(R.id.exerciseLayoutExerciseName);
        TextView exerciseDescription = view.findViewById(R.id.exerciseLayoutExerciseDescription);
        databaseHelper.getExercise(exerciseList.get(position), new DatabaseHelper.Callback<Exercise>() {
            @Override
            public void onSuccess(Exercise result) {
                exercise.setText(result.name);
                exerciseDescription.setText(result.description);
            }

            @Override
            public void onError(Exception e) {
                Log.e("exercise adaptor", "Error getting exercise", e);
            }
        });

        Button btn = view.findViewById(R.id.exerciseLayoutBtn);
        btn.setOnClickListener(v -> {
            if (pageName.equals("newWorkout")) {
                exerciseList.remove(position);
                databaseHelper.getWorkout(workoutId, new DatabaseHelper.Callback<Workout>() {
                    @Override
                    public void onSuccess(Workout result) {
                        String description = result.description;
                        Map<String, Object> workoutData = new HashMap<>();
                        workoutData.put("description", description);
                        workoutData.put("name", workoutName);
                        workoutData.put("exercises", exerciseList);
                        databaseHelper.writeWorkout(workoutData, workoutId, new DatabaseHelper.Callback<Workout>() {
                            @Override
                            public void onSuccess(Workout result) {
                                Intent intent = new Intent(context, NewWorkoutActivity.class);
                                intent.putExtra("workoutId", workoutId);
                                intent.putExtra("workoutName", workoutName);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                context.startActivity(intent);
                            }

                            @Override
                            public void onError(Exception e) {
                                Log.e("exercise adaptor", "Error loading workout exercises", e);
                            }
                        });

                    }

                    @Override
                    public void onError(Exception e) {
                        Log.e("New Workout", "Failed to retrieve workout exercises", e);
                    }
                });
            } else if (pageName.equals("addExercise")) {
                Intent intent = new Intent(context, ExerciseDetails.class);
                intent.putExtra("workoutId", workoutId);
                intent.putExtra("workoutName", workoutName);
                intent.putExtra("exerciseName", exerciseList.get(position));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
        if (pageName.equals("newWorkout")) {
            btn.setText("Remove");
        } else if (pageName.equals("addExercise")) {
            btn.setText("Details");
        }
        return view;
    }
}
