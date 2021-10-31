package ca.cmput301f21t22.nabu.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.List;
import java.util.Objects;

public class LiveUser extends LiveDocument<LiveUser.Properties> implements User {
    @Nullable
    private String email;
    @Nullable
    private List<String> habits;

    public LiveUser(@NonNull DocumentReference ref) {
        super(LiveUser.Properties.class, ref);
    }

    @Override
    public void readFields(@NonNull DocumentSnapshot snapshot) {
        String email = snapshot.getString("email");
        if (!Objects.equals(this.email, email)) {
            this.email = email;
            this.notifyPropertyChanged(Properties.EMAIL);
        }

        // noinspection unchecked
        List<String> habits = (List<String>) snapshot.get("habits");
        if (!Objects.equals(this.habits, habits)) {
            this.habits = habits;
            this.notifyPropertyChanged(Properties.HABITS);
        }
    }

    @Override
    public boolean equals(@Nullable Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        LiveUser user = (LiveUser) o;
        return Objects.equals(this.email, user.email) && Objects.equals(this.habits, user.habits);
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
        if (this.isAlive()) {
            this.ref.update("email", email);
        }
    }

    @Override
    @Nullable
    public List<String> getHabits() {
        return this.habits;
    }

    @Override
    public void setHabits(@Nullable List<String> habits) {
        if (this.isAlive()) {
            this.ref.update("habits", habits);
        }
    }

    public enum Properties {
        EMAIL, HABITS
    }
}
