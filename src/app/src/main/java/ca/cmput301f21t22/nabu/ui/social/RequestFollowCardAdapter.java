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

public class RequestFollowCardAdapter extends RecyclerView.Adapter<RequestFollowCardAdapter.ViewHolder> {
    @NonNull
    private List<User> requests;
    @Nullable
    private ItemClickListener<RequestFollowCardAdapter, User> requestCardRejectButtonListener;
    @Nullable
    private ItemClickListener<RequestFollowCardAdapter, User> requestCardAcceptButtonListener;

    public RequestFollowCardAdapter() {
        this.requests = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(
                CardUserRequestBinding.inflate(LayoutInflater.from(parent.getContext()),
                        parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user = this.requests.get(position);

        holder.onBindView(user);
        holder.binding.rejectFollowButton.setOnClickListener((view) -> {
            if (this.requestCardRejectButtonListener != null) {
                this.requestCardRejectButtonListener.onItemClicked(this, user);
            }
        });
        holder.binding.acceptFollowButton.setOnClickListener((view) -> {
            if (this.requestCardAcceptButtonListener != null) {
                this.requestCardAcceptButtonListener.onItemClicked(this, user);
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.requests.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setCards(@Nullable List<User> requests) {
        requests = requests != null ? requests : new ArrayList<>();
        this.requests = requests;
        this.notifyDataSetChanged();
    }

    public void setDenyButtonListener(@Nullable ItemClickListener<RequestFollowCardAdapter, User>
                                              requestCardDenyButtonListener) {
        this.requestCardRejectButtonListener = requestCardDenyButtonListener;
    }

    public void setAcceptButtonListener(@Nullable ItemClickListener<RequestFollowCardAdapter, User>
                                                requestCardAcceptButtonListener) {
        this.requestCardAcceptButtonListener = requestCardAcceptButtonListener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @NonNull
        private final CardUserRequestBinding binding;

        public ViewHolder(@NonNull CardUserRequestBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void onBindView(@NonNull User user) {
            this.binding.labelUserName.setText(user.getEmail());
        }
    }
}
