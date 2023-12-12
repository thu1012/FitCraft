package com.cs407.fitcraft;

import java.util.ArrayList;
import java.util.List;

public class WorkoutList {
    String description, name;
    List<String> exercises;
    public WorkoutList(String name, String description, List<String> exercises) {
        this.name = name;
        this.description = description;
        this.exercises = exercises;
    }

    public WorkoutList(String name, String description) {
        this(name, description, new ArrayList<>());
    }
}
