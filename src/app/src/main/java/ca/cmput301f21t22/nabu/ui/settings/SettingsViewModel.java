package ca.cmput301f21t22.nabu.ui.settings;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import ca.cmput301f21t22.nabu.model.Collection;
import ca.cmput301f21t22.nabu.model.User;

public class SettingsViewModel extends ViewModel {
    @NonNull
    public final static String TAG = "SettingsViewModel";

    @NonNull
    private final MutableLiveData<User> currentUser;

    @NonNull
    private final FirebaseFirestore db;
    @NonNull
    private final CollectionReference users;
    @NonNull
    private final FirebaseAuth auth;

    public SettingsViewModel() {
        this.currentUser = new MutableLiveData<>(null);

        this.db = FirebaseFirestore.getInstance();
        this.users = this.db.collection(Collection.USERS.getName());
        this.auth = FirebaseAuth.getInstance();
        this.auth.addAuthStateListener(this::onSignInChanged);
    }

    public void onSignInChanged(@NonNull FirebaseAuth firebaseAuth) {
        if (firebaseAuth.getCurrentUser() != null) {
            User currentUser = new User(this.users.document(firebaseAuth.getCurrentUser().getUid()));
            this.currentUser.setValue(currentUser);
        } else {
            this.currentUser.setValue(null);
        }
    }

    public void doSignOut() {
        this.auth.signOut();
    }

    public void doReset() {
        // TODO: Implement Reset.
    }

    @NonNull
    public LiveData<User> getCurrentUser() {
        return this.currentUser;
    }
}
