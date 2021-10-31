package ca.cmput301f21t22.nabu.model;

import androidx.annotation.Nullable;

import java.util.List;

public interface User {
    @Nullable
    String getEmail();

    void setEmail(@Nullable String email);

    @Nullable
    List<String> getHabits();

    void setHabits(@Nullable List<String> habits);
}
