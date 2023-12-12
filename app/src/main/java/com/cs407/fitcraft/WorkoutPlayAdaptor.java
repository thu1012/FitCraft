package com.cs407.fitcraft;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.VideoView;

import java.util.List;

public class WorkoutPlayAdaptor extends BaseAdapter implements ListAdapter {
    private final List<String> exercises;
    private final Activity activity;
    private final VideoView videoView;
    private final DatabaseHelper databaseHelper;

    public WorkoutPlayAdaptor(List<String> exercises, Activity activity, VideoView videoView) {
        this.exercises = exercises;
        this.activity = activity;
        this.videoView = videoView;
        this.databaseHelper = new DatabaseHelper();
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

    @SuppressLint("InflateParams")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.adaptor_workout_play, null);
        }

        TextView exerciseNameTextView = view.findViewById(R.id.workoutPlayAdaptorTextView);
        databaseHelper.getExercise(exercises.get(position), new DatabaseHelper.Callback<Exercise>() {
            @Override
            public void onSuccess(Exercise result) {
                exerciseNameTextView.setText(result.name);
            }

            @Override
            public void onError(Exception e) {
                Log.e("workout play adaptor", "Error retrieving exercise", e);
            }
        });

        ProgressBar progressBar = view.findViewById(R.id.workoutPlayAdaptorProgressBar);
        progressBar.setProgress(0);

        ImageButton playPauseButton = view.findViewById(R.id.workoutPlayAdaptorImageButton);
        playPauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VideoHelper videoHelper = new VideoHelper(videoView, false,
                        progressBar, activity, position);
                if ("pause".equals(v.getTag())) {
                    playPauseButton.setImageResource(android.R.drawable.ic_media_play);
                    v.setTag("play");
                    videoHelper.pauseVideo();
                } else {
                    playPauseButton.setImageResource(android.R.drawable.ic_media_pause);
                    v.setTag("pause");
                    databaseHelper.getExercise(exercises.get(position), new DatabaseHelper.Callback<Exercise>() {
                        @Override
                        public void onSuccess(Exercise result) {
                            videoHelper.setVideo(result, activity);
                            videoHelper.startVideo(progressBar);
                        }

                        @Override
                        public void onError(Exception e) {
                            Log.e("workout play adaptor", "Error retrieving exercise", e);
                        }
                    });
                }
            }
        });
        return view;
    }
}
