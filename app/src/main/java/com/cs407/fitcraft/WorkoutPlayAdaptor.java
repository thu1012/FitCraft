package com.cs407.fitcraft;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.VideoView;

import java.util.List;

// ExerciseAdapter.java

public class WorkoutPlayAdaptor extends BaseAdapter implements ListAdapter {

    private List<String> exercises;
    private Activity activity;
    private DatabaseHelper databaseHelper;
    private VideoView videoView;

    public WorkoutPlayAdaptor(List<String> exercises, Activity activity, VideoView videoView) {
        this.exercises = exercises;
        this.activity = activity;
        databaseHelper = new DatabaseHelper();
        this.videoView = videoView;
    }

    @Override
    public int getCount() {
        return exercises.size();
    }

    @Override
    public Object getItem(int i) {
        return exercises.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item, parent, false);
        }

        // Fetching the TextView and setting its text
        TextView exerciseNameTextView = convertView.findViewById(R.id.exerciseNameTextView);
        databaseHelper.getExercise(exercises.get(position), new DatabaseHelper.Callback<Exercise>() {
            @Override
            public void onSuccess(Exercise result) {
                exerciseNameTextView.setText(result.name);
            }

            @Override
            public void onError(Exception e) {
                Log.e("Workout play Adaptor", "Error retrieving exercise", e);
            }
        });

        // Assuming you'll want to do something with the ProgressBar:
        ProgressBar exerciseProgress = convertView.findViewById(R.id.exerciseProgressBar);
        // Set the progress as required. Here's an example setting it to 50 for demonstration:
        exerciseProgress.setProgress(0); // You'll likely want to change this

        // Handling the play/pause button:
        ProgressBar progressBar = convertView.findViewById(R.id.exerciseProgressBar);
        ImageButton playPauseButton = convertView.findViewById(R.id.playPauseImageButton);
        playPauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VideoHelper localVideoHelper = new VideoHelper(videoView, false, progressBar, activity);
                // Handle play/pause actions here.
                // For now, let's toggle the image between play and pause as an example:
                if ("pause".equals(v.getTag())) {
                    playPauseButton.setImageResource(android.R.drawable.ic_media_play);
                    v.setTag("play");
                    localVideoHelper.pauseVideo();
                } else {
                    playPauseButton.setImageResource(android.R.drawable.ic_media_pause);
                    v.setTag("pause");
                    databaseHelper.getExercise(exercises.get(position), new DatabaseHelper.Callback<Exercise>() {
                        @Override
                        public void onSuccess(Exercise result) {
                            localVideoHelper.setVideo(result, activity);
                            localVideoHelper.startVideo();
                        }

                        @Override
                        public void onError(Exception e) {
                            Log.e("Workout play Adaptor", "Error retrieving exercise", e);
                        }
                    });
                }
            }
        });

        return convertView;
    }
}
