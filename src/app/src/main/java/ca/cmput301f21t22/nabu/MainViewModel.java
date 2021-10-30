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

public class MainViewModel extends ViewModel {
    @NonNull
    public final static String TAG = "MainViewModel";

    @NonNull
    private final MutableLiveData<Boolean> showSignIn;

    @NonNull
    private final FirebaseAuth auth;

    public MainViewModel() {
        this.showSignIn = new MutableLiveData<>(false);

        this.auth = FirebaseAuth.getInstance();
        this.auth.addAuthStateListener(this::onSignInChanged);
    }

    public void onSignIn(FirebaseAuthUIAuthenticationResult result) {
        IdpResponse response = result.getIdpResponse();
        if (result.getResultCode() == Activity.RESULT_OK && this.auth.getCurrentUser() != null) {
            Log.d(TAG, "User logged in with id: " + this.auth.getCurrentUser().getUid());
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

    public void onSignInChanged(FirebaseAuth auth) {
        this.showSignIn.setValue(auth.getCurrentUser() == null);
    }

    @NonNull
    public LiveData<Boolean> getShowSignIn() {
        return this.showSignIn;
    }
}
