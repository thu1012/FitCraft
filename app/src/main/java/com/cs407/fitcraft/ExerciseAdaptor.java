package com.cs407.fitcraft;

import android.content.Context;
import android.content.Intent;
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

public class ExerciseAdaptor extends BaseAdapter implements ListAdapter, Filterable {
    private ArrayList<Exercise> exerciseList;
    private ArrayList<Exercise> filteredList;
    private Context context;
    private String pageName;
    private boolean isFiltered = false;

    public ExerciseAdaptor(ArrayList<Exercise> exerciseList, Context context, String pageName) {
        this.exerciseList = exerciseList;
        this.filteredList = new ArrayList<>(exerciseList); // Initialize with a copy of exerciseList
        this.context = context;
        this.pageName = pageName;
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

        TextView exercise = view.findViewById(R.id.exerciseLayoutExerciseName);
        Exercise item = isFiltered ? filteredList.get(position) : exerciseList.get(position);
        exercise.setText(item.name);

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
                    intent.putExtra("exerciseName", exerciseList.get(position).documentName);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                } else if (pageName.equals("firstPage")) {
                    Intent intent = new Intent(context, WorkoutPlay.class);
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

    public void updateLists(ArrayList<Exercise> newExercises) {
        exerciseList.clear();
        exerciseList.addAll(newExercises);
        filteredList.clear();
        filteredList.addAll(newExercises);
        isFiltered = false; // Reset the filter
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
                    ArrayList<Exercise> resultsData = new ArrayList<>();
                    for (Exercise e : exerciseList) {
                        String s = e.name;
                        if (s.toLowerCase().contains(searchStr)) {
                            resultsData.add(e);
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
                    // Filter cleared
                    filteredList = new ArrayList<>(exerciseList);
                    isFiltered = false;
                } else {
                    // Filter active
                    filteredList = (ArrayList<Exercise>) results.values;
                    isFiltered = true;
                }
                notifyDataSetChanged();
            }

        };
    }
}
