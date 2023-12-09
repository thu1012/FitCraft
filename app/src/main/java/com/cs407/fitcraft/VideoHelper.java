package com.cs407.fitcraft;

import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.VideoView;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class VideoHelper {
    private final VideoView videoView;
    private final DatabaseHelper databaseHelper;
    private final boolean onLoop;
    private ProgressBar progressBar;
    private final Handler progressHandler = new Handler();
    private Runnable progressRunnable;
    private final Activity activity;

    public VideoHelper(VideoView videoView, boolean onLoop, Activity activity) {
        this(videoView, onLoop, null, activity);
    }

    public VideoHelper(VideoView videoView, boolean onLoop, ProgressBar progressBar, Activity activity) {
        this.videoView = videoView;
        this.onLoop = onLoop;
        this.progressBar = progressBar;
        this.activity = activity;
        this.databaseHelper = new DatabaseHelper();

        setupVideoView();
    }

    private void setupVideoView() {
        videoView.setOnPreparedListener(mp -> {
            mp.setLooping(onLoop);
            mp.setOnVideoSizeChangedListener((mp1, width, height) -> setupMediaController());
        });
    }

    private void setupMediaController() {
        MediaController mediaController = new MediaController(activity);
        videoView.setMediaController(mediaController);
        mediaController.setAnchorView(videoView);
        mediaController.hide();
    }

    public void setVideo(String exerciseId, Context context) {
        databaseHelper.getExercise(exerciseId, new DatabaseHelper.Callback<Exercise>() {
            @Override
            public void onSuccess(Exercise result) {
                downloadAndSetVideo(result.videoUrl, context);
            }

            @Override
            public void onError(Exception e) {
                Log.e("VideoHelper", "Error getting exercise.", e);
            }
        });
    }

    public void setVideo(Exercise exercise, Context context) {
        downloadAndSetVideo(exercise.videoUrl, context);
    }

    private void downloadAndSetVideo(String videoUrl, Context context) {
        FirebaseStorage.getInstance().getReferenceFromUrl(videoUrl).getDownloadUrl()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.i("VideoHelper", "Video download successful.");
                        setVideoHelper(task.getResult(), context);
                    } else {
                        Log.e("VideoHelper", "Error downloading video", task.getException());
                    }
                });
    }

    public void setVideoHelper(Uri uri, Context context) {
        videoView.setVideoURI(uri);
        setupMediaController();
        if(onLoop) startVideo();
        setupProgressUpdate();
    }

    private void setupProgressUpdate() {
        progressRunnable = new Runnable() {
            @Override
            public void run() {
                if (videoView.isPlaying()) {
                    updateProgressBars(videoView.getCurrentPosition(), videoView.getDuration());
                    progressHandler.postDelayed(this, 1000); // Update every second
                } else {
                    progressHandler.postDelayed(this, 500); // Recheck after a short delay
                }
            }
        };
        progressHandler.post(progressRunnable);
    }

    private void updateProgressBars(int currentPosition, int totalDuration) {
        if (totalDuration > 0) {
            int progress = (int) (((float) currentPosition / totalDuration) * 100);
            activity.runOnUiThread(() -> {
                if(progressBar != null) {
                    progressBar.setProgress(progress);
                }
            });
        }
    }

    public void startVideo() {
        videoView.start();
        progressHandler.post(progressRunnable);
    }

    public void pauseVideo() {
        videoView.pause();
        progressHandler.removeCallbacks(progressRunnable);
    }
}
