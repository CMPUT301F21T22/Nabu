package ca.cmput301f21t22.nabu.ui.my_day;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ca.cmput301f21t22.nabu.data.MyDayCard;
import ca.cmput301f21t22.nabu.databinding.CardSocialHabitBinding;

/**
 * Creates UI cards that represent another user's habits by adapting data from MyDayCards.
 */
public class SocialHabitCardAdapter extends RecyclerView.Adapter<SocialHabitCardAdapter.ViewHolder> {
    @NonNull
    private List<MyDayCard> cards;

    /**
     * Assigns a new array list for the list of habit cards for social feeds.
     */
    public SocialHabitCardAdapter() {
        this.cards = new ArrayList<>();
    }

    /**
     * Sets up the card view on the parent view group.
     *
     * @param parent   The parent ViewGroup, the view for this to be placed within.
     * @param viewType An integer representation of the type of view of the parent ViewGroup.
     * @return The ViewHolder, an object representing the card view created.
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(CardSocialHabitBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    /**
     * Retrieves information for creating the card and then initiates the card's creation.
     *
     * @param holder   A ViewHolder that represents the card view being created.
     * @param position The integer position, within the cards list, of the HabitCard to take information from.
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MyDayCard card = this.cards.get(position);
        holder.onBindView(card);
    }

    /**
     * Returns an integer count on how many habits are going to be or have been adapted for a feed.
     *
     * @return The integer amount of the habits that should be adapted.
     */
    @Override
    public int getItemCount() {
        return this.cards.size();
    }

    /**
     * Sets a list of MyDayCards to be adapted.
     *
     * @param cards A MyDayCard List of MyDayCards to be adapted.
     */
    @SuppressLint("NotifyDataSetChanged")
    public void setCards(@Nullable List<MyDayCard> cards) {
        cards = cards != null ? cards : new ArrayList<>();
        this.cards = cards;
        this.notifyDataSetChanged();
    }

    /**
     * Manages the view of the card.
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        @NonNull
        private final CardSocialHabitBinding binding;

        /**
         * Sets the binding of the view to a layout binding instance.
         *
         * @param binding The layout binding instance to be set.
         */
        public ViewHolder(@NonNull CardSocialHabitBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        /**
         * Makes the proper edits to display the details of a MyDayCard within the view.
         *
         * @param card The MyDayCard to take data from to display.
         */
        public void onBindView(@NonNull MyDayCard card) {
            this.binding.labelHabitName.setText(card.getHabit().getTitle());
            this.binding.labelReason.setText(card.getHabit().getReason());

            ImageView[] icons = {
                    this.binding.iconComplete0,
                    this.binding.iconComplete1,
                    this.binding.iconComplete2,
                    this.binding.iconComplete3,
                    this.binding.iconComplete4,
                    this.binding.iconComplete5,
                    this.binding.iconComplete6,
                    };
            for (int i = 0; i < 7; i++) {
                icons[i].setImageResource(card.getIcon(i).getResource());
            }
        }
    }
}
