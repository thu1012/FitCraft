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
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ExerciseAdaptor extends BaseAdapter implements ListAdapter, Filterable {
    private boolean isFiltered = false;
    private ArrayList<String> exerciseList;
    private ArrayList<String> filteredList;
    private Context context;
    private String pageName;
    private DatabaseHelper databaseHelper;
    private String workoutId;
    private String workoutName;
    private final Map<String, Exercise> exerciseCache = new HashMap<>();

    public ExerciseAdaptor(ArrayList<String> exerciseList, Context context, String pageName, String workoutId, String workoutName) {
        this.exerciseList = exerciseList;
        this.filteredList = new ArrayList<>(exerciseList);
        this.context = context;
        this.pageName = pageName;
        this.workoutId = workoutId;
        this.workoutName = workoutName;
        this.databaseHelper = new DatabaseHelper();
        for (String exerciseId : exerciseList) {
            databaseHelper.getExercise(exerciseId, new DatabaseHelper.Callback<Exercise>() {
                @Override
                public void onSuccess(Exercise exercise) {
                    exerciseCache.put(exerciseId, exercise);
                    notifyDataSetChanged();
                }

                @Override
                public void onError(Exception e) {
                    Log.e("ExerciseAdaptor", "Error getting exercise details", e);
                }
            });
        }
    }

    @Override
    public int getCount() {
        return isFiltered ? filteredList.size() : exerciseList.size();
    }

    @Override
    public Object getItem(int pos) {
        return isFiltered ? filteredList.get(pos) : exerciseList.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        return 0;
    }

    private static class ViewHolder {
        TextView nameTextView;
        TextView descriptionTextView;
        Button btn;
    }

    @SuppressLint({"InflateParams", "SetTextI18n"})
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewHolder holder;

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.adaptor_exercise_layout, null);
            holder = new ViewHolder();
            holder.nameTextView = view.findViewById(R.id.exerciseLayoutExerciseName);
            holder.descriptionTextView = view.findViewById(R.id.exerciseLayoutExerciseDescription);
            holder.btn = view.findViewById(R.id.exerciseLayoutBtn);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        String exerciseId = isFiltered ? filteredList.get(position) : exerciseList.get(position);

        if (exerciseCache.containsKey(exerciseId)) {
            Exercise exercise = exerciseCache.get(exerciseId);
            holder.nameTextView.setText(exercise.name);
            holder.descriptionTextView.setText(exercise.description);
        } else {
            holder.nameTextView.setText("Loading...");
            holder.descriptionTextView.setText("");
            databaseHelper.getExercise(exerciseId, new DatabaseHelper.Callback<Exercise>() {
                @Override
                public void onSuccess(Exercise exercise) {
                    exerciseCache.put(exerciseId, exercise);
                    if (exerciseId.equals((isFiltered ? filteredList.get(position) : exerciseList.get(position)))) {
                        holder.nameTextView.setText(exercise.name);
                        holder.descriptionTextView.setText(exercise.description);
                    }
                }

                @Override
                public void onError(Exception e) {
                    Log.e("ExerciseAdaptor", "Error getting exercise details", e);
                }
            });
        }

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

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                if (constraint == null || constraint.length() == 0) {
                    results.values = exerciseList;
                    results.count = exerciseList.size();
                } else {
                    String searchStr = constraint.toString().toLowerCase();
                    ArrayList<String> resultsData = new ArrayList<>();
                    for (String exerciseId : exerciseList) {
                        Exercise exercise = exerciseCache.get(exerciseId);
                        if (exercise != null && exercise.name.toLowerCase().contains(searchStr)) {
                            resultsData.add(exerciseId);
                        }
                    }
                    results.values = resultsData;
                    results.count = resultsData.size();
                }
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (constraint == null || constraint.length() == 0) {
                    isFiltered = false;
                    filteredList = new ArrayList<>(exerciseList);
                } else {
                    isFiltered = true;
                    filteredList = (ArrayList<String>) results.values;
                }
                notifyDataSetChanged();
            }
        };
    }

    public void updateLists(ArrayList<String> newExercises) {
        exerciseList.clear();
        exerciseList.addAll(newExercises);
        filteredList.clear();
        filteredList.addAll(newExercises);
        isFiltered = false; // Reset the filter
    }

}
