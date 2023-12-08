package com.cs407.fitcraft;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.MediaController;
import android.widget.VideoView;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class VideoSetter {
    public void setVideo(VideoView videoView, String exerciseName, Context context) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Exercises").document(exerciseName)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            String videoUrl = (String) task.getResult().get("url");
                            FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
                            assert videoUrl != null;
                            StorageReference httpsReference = firebaseStorage.getReferenceFromUrl(videoUrl);

                            httpsReference.getDownloadUrl()
                                    .addOnCompleteListener(new OnCompleteListener<Uri>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Uri> task) {
                                            if (task.isSuccessful()) {
                                                Log.i("INFO", "successfully downloaded video");
                                                setVideoHelper(videoView, task.getResult(), context);
                                            } else {
                                                Log.e("ERROR", "Error downloading video", task.getException());
                                            }
                                        }
                                    });
                        } else {
                            Log.e("ERROR", "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    public void setVideoHelper(VideoView videoView, Uri uri, Context context) {
        videoView.setVideoURI(uri);
        MediaController mediaController = new MediaController(context);
        mediaController.setAnchorView(videoView);
        videoView.setMediaController(mediaController);
        videoView.start();
    }
}
