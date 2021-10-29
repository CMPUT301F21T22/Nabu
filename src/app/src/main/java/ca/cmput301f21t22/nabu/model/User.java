package ca.cmput301f21t22.nabu.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.List;
import java.util.Objects;

public class User extends LiveDocument<User.Properties> {
    @Nullable
    private String userId;
    @Nullable
    private List<String> habits;

    public User(@NonNull DocumentReference ref) {
        super(ref);
    }

    @Override
    public void readFields(@NonNull DocumentSnapshot snapshot) {
        String userId = snapshot.getString("userId");
        if (!Objects.equals(this.userId, userId)) {
            this.userId = userId;
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
    public String getUserId() {
        return this.userId;
    }

    public void setUserId(@Nullable String userId) {
        if (this.isAlive()) {
            this.ref.update("userId", userId);
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
