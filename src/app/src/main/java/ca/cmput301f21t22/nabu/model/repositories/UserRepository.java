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

import ca.cmput301f21t22.nabu.data.User;

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

    private UserRepository() {
        this.currentUser = new MutableLiveData<>();

        this.usersMap = new HashMap<>();
        this.users = new MutableLiveData<>();

        this.usersCollection = FirebaseFirestore.getInstance().collection("Users");
        this.usersCollection.addSnapshotListener(this::onUsersChanged);

        this.auth = FirebaseAuth.getInstance();
        this.auth.addAuthStateListener(auth -> this.onSignInChanged());
    }

    @NonNull
    public static UserRepository getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new UserRepository();
        }

        return INSTANCE;
    }

    @NonNull
    private static User createFromSnapshot(@NonNull DocumentSnapshot snapshot) {
        String email = Objects.requireNonNull(snapshot.getString("email"));
        @SuppressWarnings("unchecked") List<String> habits =
                Objects.requireNonNull((List<String>) snapshot.get("habits"));
        return new User(snapshot.getId(), email, habits);
    }

    @NonNull
    public LiveData<User> getCurrentUser() {
        return this.currentUser;
    }

    @NonNull
    public LiveData<Map<String, User>> getUsers() {
        return this.users;
    }

    private void onSignInChanged() {
        FirebaseUser user = this.auth.getCurrentUser();
        if (user == null) {
            this.currentUser.setValue(null);
            return;
        }

        this.usersCollection.document(user.getUid())
                .get()
                .addOnSuccessListener(this::onCurrentUserLoaded)
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

    private void onCurrentUserLoaded(@NonNull DocumentSnapshot snapshot) {
        FirebaseUser fbUser = this.auth.getCurrentUser();
        if (fbUser == null) {
            Log.e(TAG, "No logged in user.");
            return;
        }

        // If there's a non-null logged in user, but they're not in Firestore, it's a new user, and we should
        // add it to the database.
        if (!snapshot.exists()) {
            // TODO: This belongs in a UserController
            Map<String, Object> map = new HashMap<>();
            map.put("email", fbUser.getEmail());
            map.put("habits", new ArrayList<>());
            snapshot.getReference().set(map);
        } else {
            User user = createFromSnapshot(snapshot);
            this.currentUser.setValue(user);
            this.usersMap.put(snapshot.getId(), user);
            this.users.setValue(this.usersMap);
        }
    }
}
