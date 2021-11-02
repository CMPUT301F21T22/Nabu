package ca.cmput301f21t22.nabu.data;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class User implements Serializable {
    @NonNull
    private final String id;
    @NonNull
    private String email;
    @NonNull
    private List<String> habits;

    public User(@NonNull String id, @NonNull String email, @NonNull List<String> habits) {
        this.id = id;
        this.email = email;
        this.habits = habits;
    }

    @Override
    public boolean equals(@Nullable Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        User user = (User) o;
        return this.id.equals(user.id) && this.email.equals(user.email) && this.habits.equals(user.habits);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.email, this.habits);
    }

    @NonNull
    public String getId() {
        return this.id;
    }

    @NonNull
    public String getEmail() {
        return this.email;
    }

    public void setEmail(@NonNull String email) {
        this.email = email;
    }

    @NonNull
    public List<String> getHabits() {
        return this.habits;
    }

    public void setHabits(@NonNull List<String> habits) {
        this.habits = habits;
    }
}
