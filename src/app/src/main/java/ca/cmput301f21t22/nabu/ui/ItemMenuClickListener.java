package ca.cmput301f21t22.nabu.ui;

import android.view.View;

public interface ItemMenuClickListener<TItem> {
    void onItemMenuClicked(View sender, TItem item);
}
