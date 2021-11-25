package ca.cmput301f21t22.nabu.ui.social;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ca.cmput301f21t22.nabu.data.User;
import ca.cmput301f21t22.nabu.model.Command;
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
    public LiveData<List<User>> getRequestCards() { return this.requestCards; }

    @NonNull
    public LiveData<List<User>> getFollowingCards() { return this.followingCards; }

    public void setCurrentUser(@Nullable User currentUser) {
        this.currentUser = currentUser;
        this.onDataChanged();
    }

    public void setAllCurrentUsers(@Nullable Map<String, User> allUsers) {
        this.allCurrentUsers = allUsers;
        this.onDataChanged();
    }

    public void onAcceptClicked(@NonNull User user){
        new AcceptRequestCommand(this.currentUser, user).execute();
    }

    public void onDenyClicked(@NonNull User user){
        new RejectRequestCommand(this.currentUser, user).execute();
    }

    public void onUnfollowClicked(@NonNull User user){
        new UnfollowUserCommand(this.currentUser, user);
    }

    public boolean onEmailEntered(@NonNull String email){
        User user = null;
        if(this.currentUser != null){
            for(User userCheck : this.allCurrentUsers.values()) {
                if (userCheck.getEmail() == email) {
                    user = userCheck;
                    break;
                }
            }
        }

        if (user == null) {
            return false;
        }

        new SendRequestCommand(this.currentUser, user).execute();

        return true;
    }

    public void onDataChanged() {

        if(this.currentUser != null) {
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
            this.requestCards.setValue(usersRequesting);
        }
    }

}
