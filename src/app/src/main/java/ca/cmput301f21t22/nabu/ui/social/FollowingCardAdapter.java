package ca.cmput301f21t22.nabu.ui.social;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ca.cmput301f21t22.nabu.data.User;
import ca.cmput301f21t22.nabu.databinding.CardUserFollowingBinding;
import ca.cmput301f21t22.nabu.databinding.CardUserRequestBinding;
import ca.cmput301f21t22.nabu.ui.ItemClickListener;

/**
 * Creates UI cards for representing other users that the user is following. Detects input on
 * those cards to unfollow the user.
 */
public class FollowingCardAdapter extends RecyclerView.Adapter<FollowingCardAdapter.ViewHolder> {
    @NonNull
    private List<User> usersFollowing;
    @Nullable
    private ItemClickListener<FollowingCardAdapter, User> unfollowButtonListener;

    /**
     * Assigns a new array list for the list of other users that the user is following.
     */
    public FollowingCardAdapter() {
        this.usersFollowing = new ArrayList<>();
    }

    /**
     * Sets up the card view on the parent view group.
     * @param parent
     *  The parent ViewGroup, the view for this to be placed within.
     * @param viewType
     *  An integer representation of the type of view of the parent ViewGroup.
     * @return
     *  The ViewHolder, an object representing the card view created.
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(
                CardUserFollowingBinding.inflate(LayoutInflater.from(parent.getContext()),
                        parent, false));
    }

    /**
     * Retrieves information for creating the card and then initiates the card's creation.
     * @param holder
     *  A ViewHolder that represents the card view being created.
     * @param position
     *  The integer position, within the usersFollowing list, of the User to take information from.
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user = this.usersFollowing.get(position);

        holder.onBindView(user);
        holder.binding.unfollowButton.setOnClickListener((view) -> {
            if (this.unfollowButtonListener != null) {
                this.unfollowButtonListener.onItemClicked(this, user);
            }
        });
    }

    /**
     * Returns an integer count on how many Users are going to be or have been adapted.
     * @return
     *  The integer amount of the Users that should be adapted.
     */
    @Override
    public int getItemCount() {
        return this.usersFollowing.size();
    }

    /**
     * Sets a list of Users to be adapted.
     * @param usersFollowing
     *  A User List of Users to be adapted.
     */
    @SuppressLint("NotifyDataSetChanged")
    public void setCards(@Nullable List<User> usersFollowing) {
        usersFollowing = usersFollowing != null ? usersFollowing : new ArrayList<>();
        this.usersFollowing = usersFollowing;
        this.notifyDataSetChanged();
    }

    /**
     * Sets the listener for the unfollow button of the card.
     * @param unfollowButtonListener
     *  The listener to be set.
     */
    public void setUnfollowButtonListener(@Nullable ItemClickListener<FollowingCardAdapter, User>
                                              unfollowButtonListener) {
        this.unfollowButtonListener = unfollowButtonListener;
    }

    /**
     * Manages the view of the card.
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        @NonNull
        private final CardUserFollowingBinding binding;

        /**
         * Sets the binding of the view to a layout binding instance.
         * @param binding
         *  The layout binding instance to be set.
         */
        public ViewHolder(@NonNull CardUserFollowingBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        /**
         * Makes the proper edits to display the other user's username within the view.
         * @param user
         *  The user to take data from to display.
         */
        public void onBindView(@NonNull User user) {
            this.binding.labelUserName.setText(user.getEmail());
        }
    }
}
