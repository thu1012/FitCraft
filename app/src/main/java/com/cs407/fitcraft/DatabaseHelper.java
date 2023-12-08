package com.cs407.fitcraft;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

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
                            exercises.add(document.getString("name"));
                        }
                        callback.onSuccess(exercises);
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