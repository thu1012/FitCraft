package com.cs407.fitcraft;

import androidx.annotation.NonNull;

import java.util.List;

public class Workout {
    String description, name;
    List<String> exercises;

    public Workout(String name, String description, List<String> exercises) {
        this.name = name;
        this.description = description;
        this.exercises = exercises;
    }

    @NonNull
    public String toString() {
        return name + "; " + description + "; " + exercises;
    }
}
