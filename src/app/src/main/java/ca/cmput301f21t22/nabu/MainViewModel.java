package ca.cmput301f21t22.nabu;

import android.app.Activity;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult;

import ca.cmput301f21t22.nabu.model.UserRepository;

public class MainViewModel extends ViewModel {
    @NonNull
    public final static String TAG = "MainViewModel";

    @NonNull
    private final MutableLiveData<Boolean> signedIn;

    public MainViewModel() {
        this.signedIn = new MutableLiveData<>();

        UserRepository repoUsers = UserRepository.getInstance();
        repoUsers.getCurrentUser().observeForever(user -> this.signedIn.setValue(user != null));
    }

    @NonNull
    public LiveData<Boolean> getSignedIn() {
        return this.signedIn;
    }

    public void onSignIn(@NonNull FirebaseAuthUIAuthenticationResult result) {
        IdpResponse response = result.getIdpResponse();
        if (result.getResultCode() == Activity.RESULT_OK) {
            this.signedIn.setValue(true);
        } else if (response != null) {
            Log.e(TAG, "Error signing in user.", response.getError());
        } else {
            this.signedIn.setValue(false);
        }
    }
}
