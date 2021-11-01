package ca.cmput301f21t22.nabu.ui.my_day;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableList;
import androidx.recyclerview.widget.RecyclerView;

import ca.cmput301f21t22.nabu.databinding.CardMyDayBinding;

public class MyDayCardAdapter extends RecyclerView.Adapter<MyDayCardAdapter.ViewHolder> {
    @NonNull
    private final ObservableList<MyDayCard> cards;

    public MyDayCardAdapter(@NonNull ObservableList<MyDayCard> items) {
        this.cards = items;
        this.cards.addOnListChangedCallback(new MyDayCardAdapterObserver(this));
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(CardMyDayBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.onBindView(this.cards.get(position));
    }

    @Override
    public int getItemCount() {
        return this.cards.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final CardMyDayBinding binding;

        public ViewHolder(@NonNull CardMyDayBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void onBindView(MyDayCard card) {
            this.binding.labelHabitName.setText(card.getTitle());

            ImageView[] icons = {
                    this.binding.iconComplete0,
                    this.binding.iconComplete1,
                    this.binding.iconComplete2,
                    this.binding.iconComplete3,
                    this.binding.iconComplete4,
                    this.binding.iconComplete5,
                    this.binding.iconComplete6,
                    };
            MyDayCardMarker[] markers = card.getMarkers();
            for (int i = 0; i < markers.length; i++) {
                icons[i].setImageResource(markers[i].getIcon().getRes());
            }
        }
    }
}
