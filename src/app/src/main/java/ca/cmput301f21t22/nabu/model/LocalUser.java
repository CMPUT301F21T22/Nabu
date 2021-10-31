package ca.cmput301f21t22.nabu.model;

import androidx.annotation.Nullable;

import java.util.List;
import java.util.Objects;

public class LocalUser implements User {
    @Nullable
    private String email;
    @Nullable
    private List<String> habits;

    public LocalUser() {
    }

    public LocalUser(@Nullable String email, @Nullable List<String> habits) {
        this.email = email;
        this.habits = habits;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        LocalUser localUser = (LocalUser) o;
        return Objects.equals(this.email, localUser.email) && Objects.equals(this.habits, localUser.habits);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.email, this.habits);
    }

    @Override
    @Nullable
    public String getEmail() {
        return this.email;
    }

    @Override
    public void setEmail(@Nullable String email) {
        this.email = email;
    }

    @Override
    @Nullable
    public List<String> getHabits() {
        return this.habits;
    }

    @Override
    public void setHabits(@Nullable List<String> habits) {
        this.habits = habits;
    }
}
