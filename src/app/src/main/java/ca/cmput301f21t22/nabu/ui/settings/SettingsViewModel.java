package ca.cmput301f21t22.nabu.ui.settings;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;

import ca.cmput301f21t22.nabu.data.User;
import ca.cmput301f21t22.nabu.model.commands.ResetUserCommand;
import ca.cmput301f21t22.nabu.model.repositories.UserRepository;

public class SettingsViewModel extends ViewModel {
    @NonNull
    public final static String TAG = "SettingsViewModel";

    @NonNull
    private final MutableLiveData<User> currentUser;

    @NonNull
    private final FirebaseAuth auth;

    public SettingsViewModel() {
        this.currentUser = new MutableLiveData<>();

        this.auth = FirebaseAuth.getInstance();

        UserRepository repoUsers = UserRepository.getInstance();
        repoUsers.getCurrentUser().observeForever(this.currentUser::setValue);
    }

    public void signOut() {
        this.auth.signOut();
    }

    public void reset() {
        User current = this.currentUser.getValue();
        if (current != null) {
            new ResetUserCommand(current).execute();
        }
    }

    @NonNull
    public LiveData<User> getCurrentUser() {
        return this.currentUser;
    }
}
