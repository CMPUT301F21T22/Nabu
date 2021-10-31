package ca.cmput301f21t22.nabu.ui.settings;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import ca.cmput301f21t22.nabu.model.Collections;
import ca.cmput301f21t22.nabu.model.LiveUser;

public class SettingsViewModel extends ViewModel {
    @NonNull
    public final static String TAG = "SettingsViewModel";

    @NonNull
    private final MutableLiveData<LiveUser> currentUser;

    @NonNull
    private final FirebaseFirestore db;
    @NonNull
    private final CollectionReference users;
    @NonNull
    private final FirebaseAuth auth;

    public SettingsViewModel() {
        this.currentUser = new MutableLiveData<>(null);

        this.db = FirebaseFirestore.getInstance();
        this.users = this.db.collection(Collections.USERS.getName());
        this.auth = FirebaseAuth.getInstance();
        this.auth.addAuthStateListener(this::onSignInChanged);
    }

    public void onSignInChanged(@NonNull FirebaseAuth newAuth) {
        if (newAuth.getCurrentUser() != null) {
            LiveUser currentUser = new LiveUser(this.users.document(newAuth.getCurrentUser().getUid()));
            this.currentUser.setValue(currentUser);
        }
    }

    public void doSignOut() {
        this.auth.signOut();
    }

    public void doReset() {
        // TODO: Implement Reset.
    }

    @NonNull
    public LiveData<LiveUser> getCurrentUser() {
        return this.currentUser;
    }
}
