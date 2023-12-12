package com.cs407.fitcraft;

import androidx.annotation.NonNull;

public class Exercise {
    String name, description, videoUrl;
    public Exercise(String name, String description, String videoUrl) {
        this.name = name;
        this.description = description;
        this.videoUrl = videoUrl;
    }

    @NonNull
    public String toString() {
        return name + "; " + description + "; " + videoUrl;
    }
}
