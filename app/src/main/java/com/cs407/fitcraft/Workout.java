package com.cs407.fitcraft;

import java.util.ArrayList;
import java.util.List;

public class Workout {
    String description, name;
    List<String> exercises;
    public Workout(String name, String description, List<String> exercises) {
        this.name = name;
        this.description = description;
        this.exercises = exercises;
    }

    public Workout(String name, String description) {
        this(name, description, new ArrayList<>());
    }
}
