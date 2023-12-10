package com.cs407.fitcraft;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Intent;
import android.util.Log;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DatabaseHelper {
    private FirebaseFirestore db;

    public DatabaseHelper() {
        db = FirebaseFirestore.getInstance();
    }

    public void loadExercises(final Callback<ArrayList<String>> callback) {
        db.collection("Exercises")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        ArrayList<String> exercises = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            exercises.add(document.getId());
                        }
                        callback.onSuccess(exercises);
                    } else {
                        callback.onError(task.getException());
                    }
                });
    }

    public void getExercise(String exerciseId, final Callback<Exercise> callback) {
        db.collection("Exercises").document(exerciseId)
                .get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()) {
                        DocumentSnapshot snapshot = task.getResult();
                        String name = (String) snapshot.get("name");
                        String description = (String) snapshot.get("description");
                        String url = (String) snapshot.get("url");
                        Exercise exercise = new Exercise(name, description, url, exerciseId);
                        callback.onSuccess(exercise);
                    } else {
                        callback.onError(task.getException());
                    }
                });
    }

    public void getWorkout(String workoutId, final Callback<Workout> callback) {
        db.collection("Workouts").document(workoutId)
                .get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()) {
                        DocumentSnapshot snapshot = task.getResult();
                        String name = (String) snapshot.get("name");
                        String description = (String) snapshot.get("description");
                        List<String> exercises = (List<String>) snapshot.get("exercises");
                        Workout workout = new Workout(name, description, exercises);
                        callback.onSuccess(workout);
                    } else {
                        callback.onError(task.getException());
                    }
                });
    }

    public void writeWorkout(Map<String, Object> workoutData, final Callback<Workout> callback) {

        db.collection("Workouts").document(workoutData.get("name").toString())
                .set(workoutData)
                .addOnSuccessListener(aVoid -> callback.onSuccess(null))
                .addOnFailureListener(callback::onError);
    }

    // You can add more methods for other operations like loading videos, etc.

    public interface Callback<T> {
        void onSuccess(T result);
        void onError(Exception e);
    }
}

