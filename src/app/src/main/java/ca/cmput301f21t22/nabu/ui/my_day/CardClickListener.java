package ca.cmput301f21t22.nabu.ui.my_day;

import ca.cmput301f21t22.nabu.data.MyDayCard;

public interface CardClickListener<TSender> {
    void onItemClicked(TSender sender, MyDayCard item);
}
