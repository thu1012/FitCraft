package com.cs407.fitcraft;

import static androidx.core.content.ContextCompat.startActivity;

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

    public ExerciseAdaptor(ArrayList<String> exerciseList, Context context, String pageName, String workoutId, String workoutName) {
        this.exerciseList = exerciseList;
        this.filteredList = new ArrayList<>(exerciseList);
        this.context = context;
        this.pageName = pageName;
        this.databaseHelper = new DatabaseHelper();
        this.workoutId = workoutId;
        this.workoutName = workoutName;
    }

    @Override
    public int getCount() {
        return isFiltered ? filteredList.size() : exerciseList.size();
    }

    @Override
    public Object getItem(int pos) {
        return isFiltered ? filteredList.get(pos) : exerciseList.get(pos);
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

        String item = isFiltered ? filteredList.get(position) : exerciseList.get(position);
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
                    // get the name and description
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
                                    Log.d("ExerciseRemoved", "Removed Exercise successfully written!");
                                    Intent intent = new Intent(context, NewWorkoutActivity.class);
                                    intent.putExtra("workoutId", workoutId);
                                    intent.putExtra("workoutName", workoutName);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    context.startActivity(intent);
                                }

                                @Override
                                public void onError(Exception e) {
                                    Log.e("Workout Play", "Error loading workout exercises", e);
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
                    for (String s : exerciseList) {
                        if (s.toLowerCase().contains(searchStr)) {
                            resultsData.add(s);
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

