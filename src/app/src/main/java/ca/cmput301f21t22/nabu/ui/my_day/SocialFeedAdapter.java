package ca.cmput301f21t22.nabu.ui.my_day;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ca.cmput301f21t22.nabu.data.Event;
import ca.cmput301f21t22.nabu.data.Habit;
import ca.cmput301f21t22.nabu.data.HabitCard;
import ca.cmput301f21t22.nabu.data.MyDayUserCard;
import ca.cmput301f21t22.nabu.data.User;
import ca.cmput301f21t22.nabu.databinding.CardHabitBinding;
import ca.cmput301f21t22.nabu.databinding.CardSocialHabitBinding;
import ca.cmput301f21t22.nabu.databinding.SocialFeedBinding;
import ca.cmput301f21t22.nabu.ui.ItemClickListener;
import ca.cmput301f21t22.nabu.ui.ItemMenuClickListener;
import ca.cmput301f21t22.nabu.ui.habits.EventCardAdapter;

public class SocialFeedAdapter extends RecyclerView.Adapter<SocialFeedAdapter.ViewHolder> {
    @NonNull
    private List<MyDayUserCard> users;

    public SocialFeedAdapter() {
        this.users = new ArrayList<>();
    }

    @NonNull
    @Override
    public SocialFeedAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        SocialFeedBinding binding = SocialFeedBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        binding.feedView.setLayoutManager(new LinearLayoutManager(parent.getContext()));
        return new SocialFeedAdapter.ViewHolder(binding, new SocialHabitCardAdapter());
    }

    @Override
    public void onBindViewHolder(@NonNull SocialFeedAdapter.ViewHolder holder, int position) {
        MyDayUserCard userCard = this.users.get(position);

        holder.adapter.setCards(userCard.getUserHabits());
        holder.onBindView(userCard);
    }

    @Override
    public int getItemCount() {
        return this.users.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setCards(@Nullable List<MyDayUserCard> users) {
        users = users != null ? users : new ArrayList<>();
        this.users = users;
        this.notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @NonNull
        private final SocialFeedBinding binding;
        @NonNull
        private final SocialHabitCardAdapter adapter;

        public ViewHolder(@NonNull SocialFeedBinding binding, @NonNull SocialHabitCardAdapter adapter) {
            super(binding.getRoot());
            this.binding = binding;
            this.adapter = adapter;
        }

        public void onBindView(@NonNull MyDayUserCard userCard) {
            this.binding.feedNameText.setText(userCard.getEmail());

            this.binding.feedView.setAdapter(this.adapter);
        }
    }
}