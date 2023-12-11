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
import java.util.List;
import java.util.Map;

public class WorkoutAdaptor extends BaseAdapter implements ListAdapter {

    private final List<String> workouts;
    private final Context context;
    private final DatabaseHelper databaseHelper;

    private final Map<String, Workout> workoutCache = new HashMap<>();

    public WorkoutAdaptor(List<String> workouts, Context context) {
        this.workouts = new ArrayList<>();
        this.workouts.addAll(workouts);
        this.context = context;
        this.databaseHelper = new DatabaseHelper();

        // Preload workouts and cache them
        for (String workoutId : workouts) {
            databaseHelper.getWorkout(workoutId, new DatabaseHelper.Callback<Workout>() {
                @Override
                public void onSuccess(Workout workout) {
                    workoutCache.put(workoutId, workout);
                    notifyDataSetChanged(); // Notify the adapter to refresh the list
                }

                @Override
                public void onError(Exception e) {
                    Log.e("workout adaptor", "failed to retrieve workout name", e);
                }
            });
        }
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

    // ViewHolder pattern to optimize ListView performance
    private static class ViewHolder {
        TextView nameTextView;
        TextView descriptionTextView;
        Button btn;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.exercise_layout, parent, false);
            holder = new ViewHolder();
            holder.nameTextView = convertView.findViewById(R.id.exerciseLayoutExerciseName);
            holder.descriptionTextView = convertView.findViewById(R.id.exerciseLayoutExerciseDescription);
            holder.btn = convertView.findViewById(R.id.exerciseLayoutBtn);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        String workoutId = workouts.get(position);

        if (workoutCache.containsKey(workoutId)) {
            Workout workout = workoutCache.get(workoutId);
            assert workout != null;
            holder.nameTextView.setText(workout.name);
            holder.descriptionTextView.setText(workout.description);
        } else {
            holder.nameTextView.setText("Loading...");
            holder.descriptionTextView.setText("");
            databaseHelper.getWorkout(workoutId, new DatabaseHelper.Callback<Workout>() {
                @Override
                public void onSuccess(Workout workout) {
                    workoutCache.put(workoutId, workout);
                    if (workoutId.equals(workouts.get(position))) {
                        holder.nameTextView.setText(workout.name);
                        holder.descriptionTextView.setText(workout.description);
                    }
                }

                @Override
                public void onError(Exception e) {
                    Log.e("workout adaptor", "failed to retrieve workout name", e);
                }
            });
        }

        holder.btn.setText("play workout");
        holder.btn.setOnClickListener(v -> {
            Intent intent = new Intent(context, WorkoutPlay.class);
            intent.putExtra("workoutId", workoutId);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        });

        return convertView;
    }
}
