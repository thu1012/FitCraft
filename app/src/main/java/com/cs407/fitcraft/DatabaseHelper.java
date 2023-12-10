package com.cs407.fitcraft;

import android.util.Log;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

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


    public void getWorkoutList(final Callback<List<Workout>> callback) {
        db.collection("Workouts")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<Workout> workoutList = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String id = document.getString("id");
                            String description = document.getString("description");
                            List<String> workouts = (List<String>) document.get("workouts");
                            Workout workout = new Workout(id, description, workouts);
                            workoutList.add(workout);
                        }
                        callback.onSuccess(workoutList);
                    } else {
                        callback.onError(task.getException());
                    }
                });
    }

    public void getWorkoutIdList(final Callback<List<String>> callback) {
        db.collection("Workouts")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<String> workoutIdList = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String id = document.getString("id");
                            workoutIdList.add(id);
                        }
                        callback.onSuccess(workoutIdList);
                    } else {
                        callback.onError(task.getException());
                    }
                });
    }

    public void getExerciseName(String exerciseId, final Callback<String> callback) {
        db.collection("Exercises").document(exerciseId)
                .get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()) {
                        DocumentSnapshot snapshot = task.getResult();
                        String name = (String) snapshot.get("name");
                        callback.onSuccess(name);
                    } else {
                        callback.onError(task.getException());
                    }
                });
    }


    // You can add more methods for other operations like loading videos, etc.

    public interface Callback<T> {
        void onSuccess(T result);
        void onError(Exception e);
    }
}
