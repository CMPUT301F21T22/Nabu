package ca.cmput301f21t22.nabu.ui.habits;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;

import ca.cmput301f21t22.nabu.data.Event;
import ca.cmput301f21t22.nabu.databinding.CardEventBinding;
import ca.cmput301f21t22.nabu.ui.ItemClickListener;
import ca.cmput301f21t22.nabu.ui.ItemMenuClickListener;

public class EventCardAdapter extends RecyclerView.Adapter<EventCardAdapter.ViewHolder> {
    @NonNull
    private List<Event> events;
    @Nullable
    private ItemClickListener<EventCardAdapter, Event> clickListener;
    @Nullable
    private ItemMenuClickListener<Event> menuClickListener;

    public EventCardAdapter() {
        this.events = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(CardEventBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Event event = this.events.get(position);
        holder.onBindView(event);
        holder.binding.card.setOnClickListener((view) -> {
            if (this.clickListener != null) {
                this.clickListener.onItemClicked(this, event, position);
            }
        });
        holder.binding.buttonOverflowMenu.setOnClickListener((view) -> {
            if (this.menuClickListener != null) {
                this.menuClickListener.onItemMenuClicked(view, event, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.events.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setEvents(@Nullable List<Event> events) {
        events = events != null ? events : new ArrayList<>();
        this.events = events;
        this.notifyDataSetChanged();
    }

    public void setClickListener(@Nullable ItemClickListener<EventCardAdapter, Event> clickListener) {
        this.clickListener = clickListener;
    }

    public void setMenuClickListener(@Nullable ItemMenuClickListener<Event> menuClickListener) {
        this.menuClickListener = menuClickListener;
    }
    /*
    @Nullable
    ViewGroup container;
    private ArrayList<Event> events;
    private Context context;
    @Nullable
    private CardEventBinding binding;

    public EventCardAdapter(Context context, ArrayList<Event> events) {
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
            this.binding = CardEventBinding.inflate(LayoutInflater.from(context));
        }

        Event event = events.get(position);

        TextView eventComment = this.binding.labelComment;
        TextView dates = this.binding.labelDate;
        //ImageView eventPhoto = this.binding.eventPhoto;
        //TextView eventLocation  = this.binding.eventLocation;

        eventComment.setText(event.getComment());
        dates.setText(event.getDate().toString());
        //eventPhoto.(event.getPhotoPath());
        //eventLocation.setText(event.getLocation());

        final ImageButton eventsMenuButton = this.binding.buttonOverflowMenu;
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
     */

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @NonNull
        private final DateFormat dateFormat;
        @NonNull
        private final CardEventBinding binding;

        public ViewHolder(@NonNull CardEventBinding binding) {
            super(binding.getRoot());
            this.dateFormat = DateFormat.getDateInstance();
            this.binding = binding;
        }

        public void onBindView(@NonNull Event event) {
            this.binding.labelDate.setText(this.dateFormat.format(event.getDate()));
            if (event.getComment() != null && !event.getComment().equals("")) {
                this.binding.labelComment.setText(event.getComment());
                this.binding.labelComment.setVisibility(View.VISIBLE);
            }
        }
    }
}
