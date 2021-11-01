package ca.cmput301f21t22.nabu.ui.my_day;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableList;

public class MyDayCardAdapterObserver extends ObservableList.OnListChangedCallback<ObservableList<MyDayCard>> {
    @NonNull
    private final MyDayCardAdapter adapter;

    public MyDayCardAdapterObserver(@NonNull MyDayCardAdapter adapter) {
        this.adapter = adapter;
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onChanged(ObservableList<MyDayCard> sender) {
        this.adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemRangeChanged(ObservableList<MyDayCard> sender, int positionStart, int itemCount) {
        this.adapter.notifyItemRangeChanged(positionStart, itemCount);
    }

    @Override
    public void onItemRangeInserted(ObservableList<MyDayCard> sender, int positionStart, int itemCount) {
        this.adapter.notifyItemRangeInserted(positionStart, itemCount);
    }

    @Override
    public void onItemRangeMoved(ObservableList<MyDayCard> sender, int fromPosition, int toPosition, int itemCount) {
        for (int i = fromPosition; i < fromPosition + itemCount; i++) {
            this.adapter.notifyItemMoved(i, i + (toPosition - fromPosition));
        }
    }

    @Override
    public void onItemRangeRemoved(ObservableList<MyDayCard> sender, int positionStart, int itemCount) {
        this.adapter.notifyItemRangeRemoved(positionStart, itemCount);
    }
}
