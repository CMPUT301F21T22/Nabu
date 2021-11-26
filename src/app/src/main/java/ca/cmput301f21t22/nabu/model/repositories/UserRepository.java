package ca.cmput301f21t22.nabu.model.repositories;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;

import ca.cmput301f21t22.nabu.data.User;
import ca.cmput301f21t22.nabu.model.controllers.UserController;

/**
 * Singleton providing methods for reading, searching, and observing the database of remote user instances.
 */
public class UserRepository {
    @NonNull
    public final static String TAG = "UserRepository";

    @Nullable
    private static UserRepository INSTANCE;

    @NonNull
    private final MutableLiveData<User> currentUser;

    @NonNull
    private final Map<String, User> usersMap;
    @NonNull
    private final MutableLiveData<Map<String, User>> users;

    @NonNull
    private final CollectionReference usersCollection;
    @NonNull
    private final FirebaseAuth auth;

    @NonNull
    private final UserController controller;

    private UserRepository() {
        this.currentUser = new MutableLiveData<>();

        this.usersMap = new HashMap<>();
        this.users = new MutableLiveData<>();

        this.usersCollection = FirebaseFirestore.getInstance().collection("Users");
        this.usersCollection.addSnapshotListener(this::onUsersChanged);

        this.auth = FirebaseAuth.getInstance();
        this.auth.addAuthStateListener(this::onSignInChanged);

        this.controller = UserController.getInstance();
    }

    /**
     * @return Singleton instance of the UserRepository.
     */
    @NonNull
    public static UserRepository getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new UserRepository();
        }

        return INSTANCE;
    }

    @SuppressWarnings("unchecked")
    @NonNull
    private static User createFromSnapshot(@NonNull DocumentSnapshot snapshot) {
        String email = Objects.requireNonNull(snapshot.getString("email"));
        List<String> habits = (List<String>) snapshot.get("habits");
        List<String> following = (List<String>) snapshot.get("following");
        List<String> requests = (List<String>) snapshot.get("requests");
        return new User(
                snapshot.getId(), email, habits != null ? habits : new ArrayList<>(),
                following != null ? following : new ArrayList<>(), requests != null ? requests : new ArrayList<>());
    }

    /**
     * @return Handle to a live-updating copy of the current logged-in user.
     */
    @NonNull
    public LiveData<User> getCurrentUser() {
        return this.currentUser;
    }

    /**
     * @return Handle to a live-updating copy of all users in the database.
     */
    @NonNull
    public LiveData<Map<String, User>> getUsers() {
        return this.users;
    }

    /**
     * Find a user in the database based on a given predicate.
     *
     * @param predicate The predicate to test against.
     * @return The first user that matches the predicate.
     */
    @NonNull
    public Optional<User> findUser(Predicate<User> predicate) {
        return this.usersMap.values().stream().filter(predicate).findFirst();
    }

    /**
     * Retrieves a user from the database by ID.
     *
     * @param id The ID of the user to retrieve.
     * @return Future for the retrieved user.
     */
    @NonNull
    public CompletableFuture<User> retrieveUser(@NonNull String id) {
        CompletableFuture<User> future = new CompletableFuture<>();
        this.usersCollection.document(id).get().addOnSuccessListener(snapshot -> {
            try {
                future.complete(createFromSnapshot(snapshot));
            } catch (IllegalArgumentException e) {
                future.complete(null);
            }
        }).addOnFailureListener(e -> {
            Log.e(TAG, "Failed to retrieve user.", e);
            future.complete(null);
        });
        return future;
    }

    private void onSignInChanged(FirebaseAuth auth) {
        FirebaseUser user = auth.getCurrentUser();
        if (user == null) {
            this.currentUser.setValue(null);
            return;
        }

        this.usersCollection.document(user.getUid())
                .get()
                .addOnSuccessListener(snapshot -> this.currentUser.setValue(
                        snapshot.exists() ? createFromSnapshot(snapshot) :
                        new User(snapshot.getId(), Objects.requireNonNull(user.getEmail()), new ArrayList<>())))
                .addOnFailureListener(
                        e -> Log.e(TAG, "Could not retrieve currently logged in user from collection.", e));
    }

    private void onUsersChanged(@Nullable QuerySnapshot snapshots, @Nullable FirebaseFirestoreException e) {
        if (e != null || snapshots == null) {
            Log.e(TAG, "Failed to listen to collection updates.", e);
            return;
        }

        for (DocumentChange change : snapshots.getDocumentChanges()) {
            QueryDocumentSnapshot snapshot = change.getDocument();
            User currentUser = this.currentUser.getValue();
            User user = createFromSnapshot(snapshot);
            switch (change.getType()) {
                case ADDED:
                case MODIFIED:
                    this.usersMap.put(snapshot.getId(), user);
                    this.users.setValue(this.usersMap);
                    if (currentUser != null && snapshot.getId().equals(currentUser.getId())) {
                        this.currentUser.setValue(user);
                    }
                    break;
                case REMOVED:
                    this.usersMap.remove(snapshot.getId());
                    this.users.setValue(this.usersMap);
                    if (currentUser != null && snapshot.getId().equals(currentUser.getId())) {
                        this.currentUser.setValue(null);
                    }
                    break;
            }
        }
    }
}
