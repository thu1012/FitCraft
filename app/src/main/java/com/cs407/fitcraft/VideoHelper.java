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
    private static VideoHelper currentInstance;

    private final VideoView videoView;
    private final DatabaseHelper databaseHelper;
    private final boolean onLoop;
    private ProgressBar progressBar;
    private Handler progressHandler = new Handler();
    private Runnable progressRunnable;
    private final Activity activity;
    private int position;

    public VideoHelper(VideoView videoView, boolean onLoop, Activity activity) {
        this.videoView = videoView;
        this.onLoop = onLoop;
        this.activity = activity;
        this.databaseHelper = new DatabaseHelper();
        initializeVideoView();
    }

    public VideoHelper(VideoView videoView, boolean onLoop, ProgressBar progressBar, Activity activity, int position) {
        this.videoView = videoView;
        this.onLoop = onLoop;
        this.progressBar = progressBar;
        this.activity = activity;
        this.databaseHelper = new DatabaseHelper();
        this.position = position;
        initializeVideoView();
    }

    private void initializeVideoView() {
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(onLoop);
                mp.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
                    @Override
                    public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
                        MediaController mc = new MediaController(activity);
                        videoView.setMediaController(mc);
                        mc.setAnchorView(videoView);
                        mc.hide();
                    }
                });
            }
        });
    }

    public void setVideo(String exerciseId, Context context) {
        if (currentInstance != null && currentInstance != this) {
            currentInstance.pauseVideo();
        }
        currentInstance = this;

        databaseHelper.getExercise(exerciseId, new DatabaseHelper.Callback<Exercise>() {
            @Override
            public void onSuccess(Exercise result) {
                FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
                StorageReference httpsReference = firebaseStorage.getReferenceFromUrl(result.videoUrl);

                httpsReference.getDownloadUrl()
                        .addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                if (task.isSuccessful()) {
                                    Log.i("INFO", "successfully downloaded video");
                                    setVideoHelper(task.getResult(), context);
                                } else {
                                    Log.e("ERROR", "Error downloading video", task.getException());
                                }
                            }
                        });
            }

            @Override
            public void onError(Exception e) {
                Log.e("VideoSetter", "Error getting documents.", e);
            }
        });
    }

    // Rest of the code remains unchanged...
    public void setVideoHelper(Uri uri, Context context) {
        videoView.setVideoURI(uri);
        MediaController mediaController = new MediaController(context);
        mediaController.setAnchorView(videoView);
        videoView.setMediaController(mediaController);
        if(onLoop) videoView.start();
        setupProgressUpdate();
    }

    private void setupProgressUpdate() {
        progressRunnable = new Runnable() {
            @Override
            public void run() {
                if (videoView.isPlaying()) {
                    int videoPosition = videoView.getCurrentPosition();
                    int videoDuration = videoView.getDuration();
                    updateProgressBar(position, videoPosition, videoDuration);
                    progressHandler.postDelayed(this, 1000); // Update every second
                } else {
                    progressHandler.postDelayed(this, 500); // Recheck after a short delay
                }
            }
        };
        progressHandler.post(progressRunnable);
    }


    private void updateProgressBar(int position, int currentPosition, int totalDuration) {
        if(this.position!=position) return;
        if (totalDuration > 0) {
            int progress = (int) (((float) currentPosition / totalDuration) * 100);
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(progressBar!=null && videoView.isPlaying()) {
                        progressBar.setProgress(progress);
                    }
                }
            });

        }
    }

    public void startVideo(ProgressBar progressBar) {
        videoView.start();
        progressHandler.post(progressRunnable);
        this.progressBar = progressBar;
    }
    public void pauseVideo() {
        if (currentInstance == this) {
            videoView.pause();
            progressHandler.removeCallbacks(progressRunnable);
            progressBar = null;
            currentInstance = null;
        }
    }
}
