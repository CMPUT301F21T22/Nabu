package ca.cmput301f21t22.nabu.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.List;
import java.util.Objects;

public class User extends Document<User.Properties> {
    @Nullable
    private String email;
    @Nullable
    private List<String> habits;

    public User(@NonNull DocumentReference ref) {
        super(User.Properties.class, ref);
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
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        User user = (User) o;
        return Objects.equals(this.email, user.email) && Objects.equals(this.habits, user.habits);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.email, this.habits);
    }

    @Nullable
    public String getEmail() {
        return this.email;
    }

    public void setEmail(@Nullable String email) {
        if (this.isAlive()) {
            this.ref.update("email", email);
        }
    }

    @Nullable
    public List<String> getHabits() {
        return this.habits;
    }

    public void setHabits(@Nullable List<String> habits) {
        if (this.isAlive()) {
            this.ref.update("habits", habits);
        }
    }

    public enum Properties {
        EMAIL, HABITS
    }
}
