package ca.cmput301f21t22.nabu.ui.habits;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Date;

import ca.cmput301f21t22.nabu.R;
import ca.cmput301f21t22.nabu.data.Event;
import ca.cmput301f21t22.nabu.databinding.EventCardBinding;

public class EventListAdapter extends ArrayAdapter<Event> {

    @Nullable
    ViewGroup container;
    private ArrayList<Event> events;
    private Context context;
    @Nullable
    private EventCardBinding binding;

    public EventListAdapter(Context context, ArrayList<Event> events) {
        super(context, 0, events);

        this.events = events;
        this.context = context;
    }

    //Set up views for list objects
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        //return super.getView(position, convertView, parent);
        View view = convertView;

        if (view == null) {
            this.binding = EventCardBinding.inflate(LayoutInflater.from(context));
        }

        Event event = events.get(position);

        TextView eventComment = this.binding.eventCommentText;
        TextView dates = this.binding.eventDatesText;
        //ImageView eventPhoto = this.binding.eventPhoto;
        //TextView eventLocation  = this.binding.eventLocation;

        eventComment.setText(event.getComment());
        dates.setText(event.getDate().toString());
        //eventPhoto.(event.getPhotoPath());
        //eventLocation.setText(event.getLocation());

        final ImageButton eventsMenuButton = this.binding.eventsPopupImageButton;
        eventsMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu eventsPopupMenu = new PopupMenu(context, eventsMenuButton);
                MenuInflater inflater = eventsPopupMenu.getMenuInflater();
                inflater.inflate(R.menu.habit_card_popup_menu, eventsPopupMenu.getMenu());
                eventsPopupMenu.show();
                final PopupMenu menu = eventsPopupMenu;
                menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getItemId() == eventsPopupMenu.getMenu().getItem(0).getItemId()) {
                            //Edit
                            Event inputEvent = new Event(new Date());
                            //TODO: Add call for edit habit fragment
                            edit(position, inputEvent);
                            return true;
                        } else if (item.getItemId() == eventsPopupMenu.getMenu().getItem(1).getItemId()) {
                            //remove
                            remove(events.get(position));
                            return true;
                        } else {
                            return false;
                        } //Default
                    }
                });
            }
        });

        return this.binding.getRoot();
    }

    @Override
    public void remove(@Nullable Event event) {
        super.remove(event);
        //this.habits.remove(habit);
        this.notifyDataSetChanged();
    }

    public void edit(int position, @Nullable Event event) {
        this.remove(this.getItem(position));
        this.insert(event, position);
        //this.habits.set(position, habit);
        this.notifyDataSetChanged();
    }
}
