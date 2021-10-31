package ca.cmput301f21t22.nabu;

import android.app.Activity;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import ca.cmput301f21t22.nabu.model.Collections;
import ca.cmput301f21t22.nabu.model.LiveUser;
import ca.cmput301f21t22.nabu.model.User;

public class MainViewModel extends ViewModel {
    @NonNull
    public final static String TAG = "MainViewModel";

    @NonNull
    private final MutableLiveData<Boolean> showSignIn;

    @NonNull
    private final CollectionReference users;
    @NonNull
    private final FirebaseAuth auth;

    public MainViewModel() {
        this.showSignIn = new MutableLiveData<>(false);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        this.users = db.collection(Collections.USERS.getName());
        this.auth = FirebaseAuth.getInstance();
        this.auth.addAuthStateListener(this::onSignInChanged);
    }

    public void onSignIn(@NonNull FirebaseAuthUIAuthenticationResult result) {
        IdpResponse response = result.getIdpResponse();
        if (result.getResultCode() == Activity.RESULT_OK && this.auth.getCurrentUser() != null) {
            FirebaseUser firebaseUser = this.auth.getCurrentUser();
            Log.d(TAG, "User logged in with id: " + firebaseUser.getUid());
            LiveUser currentUser = new LiveUser(this.users.document(firebaseUser.getUid()));
            currentUser.observe(((sender, property) -> {
                LiveUser user = (LiveUser) sender;
                if (property == User.Properties.EMAIL) {
                    user.setEmail(firebaseUser.getEmail());
                }
            }));
            this.showSignIn.setValue(false);
        } else {
            if (response != null) {
                Log.e(TAG, "Error signing in user.", response.getError());
            } else {
                // If the user cancels, we try signing them in again.
                this.showSignIn.setValue(true);
            }
        }
    }

    public void onSignInChanged(@NonNull FirebaseAuth newAuth) {
        this.showSignIn.setValue(newAuth.getCurrentUser() == null);
    }

    @NonNull
    public LiveData<Boolean> getShowSignIn() {
        return this.showSignIn;
    }
}
