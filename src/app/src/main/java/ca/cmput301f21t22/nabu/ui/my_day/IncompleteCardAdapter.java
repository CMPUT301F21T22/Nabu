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
import ca.cmput301f21t22.nabu.databinding.CardMyDayIncompleteBinding;
import ca.cmput301f21t22.nabu.ui.ItemClickListener;

public class IncompleteCardAdapter extends RecyclerView.Adapter<IncompleteCardAdapter.ViewHolder> {
    @NonNull
    private List<MyDayCard> cards;
    @Nullable
    private ItemClickListener<IncompleteCardAdapter, MyDayCard> clickListener;

    public IncompleteCardAdapter() {
        this.cards = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(
                CardMyDayIncompleteBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MyDayCard card = this.cards.get(position);
        holder.onBindView(card);
        holder.binding.card.setOnClickListener((view) -> {
            if (this.clickListener != null) {
                this.clickListener.onItemClicked(this, card, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.cards.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setCards(@Nullable List<MyDayCard> cards) {
        cards = cards != null ? cards : new ArrayList<>();
        this.cards = cards;
        this.notifyDataSetChanged();
    }

    public void setClickListener(@Nullable ItemClickListener<IncompleteCardAdapter, MyDayCard> clickListener) {
        this.clickListener = clickListener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @NonNull
        private final CardMyDayIncompleteBinding binding;

        public ViewHolder(@NonNull CardMyDayIncompleteBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void onBindView(@NonNull MyDayCard card) {
            this.binding.labelHabitName.setText(card.getHabit().getTitle());

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
