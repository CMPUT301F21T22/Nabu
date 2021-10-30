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
            this.notifyPropertyChanged(Properties.USER_ID);
        }

        // noinspection unchecked
        List<String> habits = (List<String>) snapshot.get("habits");
        if (!Objects.equals(this.habits, habits)) {
            this.habits = habits;
            this.notifyPropertyChanged(Properties.HABITS);
        }
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
        USER_ID, HABITS
    }
}
