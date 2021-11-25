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
import ca.cmput301f21t22.nabu.databinding.CardUserRequestBinding;
import ca.cmput301f21t22.nabu.ui.ItemClickListener;

/**
 * Creates UI cards for representing another user's request to follow the user. Detects input on
 * those cards on accepting or rejecting the follow request.
 */
public class RequestFollowCardAdapter extends
        RecyclerView.Adapter<RequestFollowCardAdapter.ViewHolder> {
    @NonNull
    private List<User> requests;
    @Nullable
    private ItemClickListener<RequestFollowCardAdapter, User> requestCardRejectButtonListener;
    @Nullable
    private ItemClickListener<RequestFollowCardAdapter, User> requestCardAcceptButtonListener;

    /**
     * Assigns a new array list for the list of other users that are requesting to follow the user.
     */
    public RequestFollowCardAdapter() {
        this.requests = new ArrayList<>();
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
                CardUserRequestBinding.inflate(LayoutInflater.from(parent.getContext()),
                        parent, false));
    }

    /**
     * Retrieves information for creating the card and then initiates the card's creation.
     * @param holder
     *  A ViewHolder that represents the card view being created.
     * @param position
     *  The integer position, within the requests list, of the User to take information from.
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user = this.requests.get(position);

        holder.onBindView(user);
        holder.binding.rejectFollowButton.setOnClickListener((view) -> {
            if (this.requestCardRejectButtonListener != null) {
                this.requestCardRejectButtonListener.onItemClicked(this, user, position);
            }
        });
        holder.binding.acceptFollowButton.setOnClickListener((view) -> {
            if (this.requestCardAcceptButtonListener != null) {
                this.requestCardAcceptButtonListener.onItemClicked(this, user, position);
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
        return this.requests.size();
    }

    /**
     * Sets a list of Users to be adapted.
     * @param requests
     *  A User List of Users to be adapted.
     */
    @SuppressLint("NotifyDataSetChanged")
    public void setCards(@Nullable List<User> requests) {
        requests = requests != null ? requests : new ArrayList<>();
        this.requests = requests;
        this.notifyDataSetChanged();
    }

    /**
     * Sets the listener for the deny follow request button of the card.
     * @param requestCardDenyButtonListener
     *  The listener to be set.
     */
    public void setDenyButtonListener(@Nullable ItemClickListener<RequestFollowCardAdapter, User>
                                              requestCardDenyButtonListener) {
        this.requestCardRejectButtonListener = requestCardDenyButtonListener;
    }

    /**
     * Sets the listener for the accept follow request button of the card.
     * @param requestCardAcceptButtonListener
     *  The listener to be set.
     */
    public void setAcceptButtonListener(@Nullable ItemClickListener<RequestFollowCardAdapter, User>
                                                requestCardAcceptButtonListener) {
        this.requestCardAcceptButtonListener = requestCardAcceptButtonListener;
    }

    /**
     * Manages the view of the card.
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        @NonNull
        private final CardUserRequestBinding binding;

        /**
         * Sets the binding of the view to a layout binding instance.
         * @param binding
         *  The layout binding instance to be set.
         */
        public ViewHolder(@NonNull CardUserRequestBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        /**
         * Makes the proper edits to display the user's username within the view.
         * @param user
         *  The user to take data from to display.
         */
        public void onBindView(@NonNull User user) {
            this.binding.labelUserName.setText(user.getEmail());
        }
    }
}
