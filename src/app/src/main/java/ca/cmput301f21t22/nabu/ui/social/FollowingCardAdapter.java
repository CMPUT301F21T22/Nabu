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

public class FollowingCardAdapter extends RecyclerView.Adapter<FollowingCardAdapter.ViewHolder> {
    @NonNull
    private List<User> usersFollowing;
    @Nullable
    private ItemClickListener<FollowingCardAdapter, User> unfollowButtonListener;

    public FollowingCardAdapter() {
        this.usersFollowing = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(
                CardUserFollowingBinding.inflate(LayoutInflater.from(parent.getContext()),
                        parent, false));
    }

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

    @Override
    public int getItemCount() {
        return this.usersFollowing.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setCards(@Nullable List<User> usersFollowing) {
        usersFollowing = usersFollowing != null ? usersFollowing : new ArrayList<>();
        this.usersFollowing = usersFollowing;
        this.notifyDataSetChanged();
    }

    public void setUnfollowButtonListener(@Nullable ItemClickListener<FollowingCardAdapter, User>
                                              unfollowButtonListener) {
        this.unfollowButtonListener = unfollowButtonListener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @NonNull
        private final CardUserFollowingBinding binding;

        public ViewHolder(@NonNull CardUserFollowingBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void onBindView(@NonNull User user) {
            this.binding.labelUserName.setText(user.getEmail());
        }
    }
}
