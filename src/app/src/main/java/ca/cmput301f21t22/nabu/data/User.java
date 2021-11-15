package ca.cmput301f21t22.nabu.data;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * A data object storing all the details of a user. A user is associated with an
 * arbitrary number of habits.
 * <p>
 * It is uniquely identified by an ID string within the context of the Firebase
 * database, but is a plain data object otherwise.
 */
public class User implements Serializable {
    @NonNull
    private final String id;
    @NonNull
    private String email;
    @NonNull
    private List<String> habits;

    /**
     * Create a remote-linked instance of User.
     * <p>
     * Remote-linked instances of User should only be created by the Repositories,
     * or "cloned" from another instance of User.
     *
     * @param id     The unique remote ID of the user, corresponding to a Firebase Auth user.
     * @param email  The email address of the user.
     * @param habits A list of references to remote habits associated with this user.
     */
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
        return this.email.equals(user.email) && this.habits.equals(user.habits);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.email, this.habits);
    }

    /**
     * @return The unique remote ID of the user, corresponding to a Firebase Auth user.
     */
    @NonNull
    public String getId() {
        return this.id;
    }

    /**
     * @return The email address of the user.
     */
    @NonNull
    public String getEmail() {
        return this.email;
    }

    /**
     * @param email The email address of the user.
     */
    public void setEmail(@NonNull String email) {
        this.email = email;
    }

    /**
     * @return A list of references to remote habits associated with this user.
     */
    @NonNull
    public List<String> getHabits() {
        return this.habits;
    }

    /**
     * @param habits A list of references to remote habits associated with this user.
     */
    public void setHabits(@NonNull List<String> habits) {
        this.habits = habits;
    }
}
