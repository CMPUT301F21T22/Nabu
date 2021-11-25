package ca.cmput301f21t22.nabu.ui.social;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import ca.cmput301f21t22.nabu.data.User;
import ca.cmput301f21t22.nabu.model.commands.AcceptRequestCommand;
import ca.cmput301f21t22.nabu.model.commands.RejectRequestCommand;
import ca.cmput301f21t22.nabu.model.commands.SendRequestCommand;
import ca.cmput301f21t22.nabu.model.commands.UnfollowUserCommand;

public class SocialViewModel extends ViewModel {
    @NonNull
    public final static String TAG = "SocialViewModel";

    @NonNull
    private final MutableLiveData<List<User>> requestCards;
    @NonNull
    private final MutableLiveData<List<User>> followingCards;

    @Nullable
    private User currentUser;
    @Nullable
    private Map<String, User> allCurrentUsers;
    /*@Nullable
    private List<User> userRequests;
    @Nullable
    private List<User> userFollowing;
     */

    public SocialViewModel() {
        this.requestCards = new MutableLiveData<>();
        this.followingCards = new MutableLiveData<>();
        //this.userRequests = new ArrayList<>();
        //this.userFollowing = new ArrayList<>();
    }

    @NonNull
    public LiveData<List<User>> getRequestCards() {
        return this.requestCards;
    }

    @NonNull
    public LiveData<List<User>> getFollowingCards() {
        return this.followingCards;
    }

    @Nullable
    public User getCurrentUser() {
        return this.currentUser;
    }

    public void setCurrentUser(@Nullable User currentUser) {
        this.currentUser = currentUser;
        this.onDataChanged();
    }

    public void setAllCurrentUsers(@Nullable Map<String, User> allUsers) {
        this.allCurrentUsers = allUsers;
        this.onDataChanged();
    }

    public void onAcceptClicked(@NonNull User user) {
        if (this.currentUser != null) {
            new AcceptRequestCommand(this.currentUser, user).execute();
        }
    }

    public void onDenyClicked(@NonNull User user) {
        if (this.currentUser != null) {
            new RejectRequestCommand(this.currentUser, user).execute();
        }
    }

    public void onUnfollowClicked(@NonNull User user) {
        if (this.currentUser != null) {
            new UnfollowUserCommand(this.currentUser, user);
        }
    }

    public void onEmailEntered(@NonNull String email) {
        if (this.currentUser != null && this.allCurrentUsers != null) {
            for (User user : this.allCurrentUsers.values()) {
                if (user.getEmail().trim().toUpperCase(Locale.ROOT).equals(email.trim().toUpperCase(Locale.ROOT))) {
                    new SendRequestCommand(this.currentUser, user).execute();
                    break;
                }
            }
        }
    }

    public void onDataChanged() {
        if (this.currentUser != null && this.allCurrentUsers != null) {
            List<User> usersRequesting = new ArrayList<>();
            for (String userId : this.currentUser.getRequests()) {
                User requestingUser = this.allCurrentUsers.get(userId);
                if (requestingUser != null) {
                    usersRequesting.add(requestingUser);
                }
            }
            this.requestCards.setValue(usersRequesting);

            List<User> usersFollowed = new ArrayList<>();
            for (String userId : this.currentUser.getFollowing()) {
                User requestingUser = this.allCurrentUsers.get(userId);
                if (requestingUser != null) {
                    usersFollowed.add(requestingUser);
                }
            }
            this.followingCards.setValue(usersFollowed);
        }
    }
}
