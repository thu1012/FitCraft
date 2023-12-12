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
    private boolean isFiltered = false;

    private ArrayList<String> exerciseList;
    private ArrayList<String> filteredList;

    private Context context;
    private String pageName;

    public ExerciseAdaptor(ArrayList<String> exerciseList, Context context, String pageName) {
        this.exerciseList = exerciseList;
        this.filteredList = new ArrayList<>(exerciseList);
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

        String item = isFiltered ? filteredList.get(position) : exerciseList.get(position);
        TextView exercise = view.findViewById(R.id.exerciseLayoutExerciseName);
        exercise.setText(item);

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
                    intent.putExtra("exerciseName", exerciseList.get(position));
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
