package ca.cmput301f21t22.nabu.ui;

public interface ItemClickListener<TSender, TItem> {
    void onItemClicked(TSender sender, TItem item, int position);
}
