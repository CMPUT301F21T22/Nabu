package ca.cmput301f21t22.nabu.model;

import androidx.annotation.NonNull;

public enum Collection {
    USERS("Users"), HABITS("Habits"), EVENTS("Events");

    @NonNull
    private final String name;

    Collection(@NonNull String name) {
        this.name = name;
    }

    @NonNull
    public String getName() {
        return this.name;
    }
}
