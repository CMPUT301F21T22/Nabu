package ca.cmput301f21t22.nabu.ui.settings;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SettingsViewModel extends ViewModel {
    @NonNull
    public final static String TAG = "SettingsViewModel";

    @NonNull
    private final MutableLiveData<FirebaseUser> currentUser;

    @NonNull
    private final FirebaseAuth auth;

    public SettingsViewModel() {
        this.currentUser = new MutableLiveData<>(null);

        this.auth = FirebaseAuth.getInstance();
        this.auth.addAuthStateListener(this::onSignInChanged);
    }

    public void onSignInChanged(FirebaseAuth firebaseAuth) {
        if (firebaseAuth.getCurrentUser() != null) {
            this.currentUser.setValue(firebaseAuth.getCurrentUser());
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

    public LiveData<FirebaseUser> getCurrentUser() {
        return this.currentUser;
    }
}
