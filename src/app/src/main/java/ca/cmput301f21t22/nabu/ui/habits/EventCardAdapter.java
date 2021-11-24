package ca.cmput301f21t22.nabu.ui.habits;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

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

    public static class ViewHolder extends RecyclerView.ViewHolder implements OnMapReadyCallback {
        @NonNull
        private final DateFormat dateFormat;
        @NonNull
        private final CardEventBinding binding;
        @Nullable
        private Event cache;
        @Nullable
        private GoogleMap map;

        public ViewHolder(@NonNull CardEventBinding binding) {
            super(binding.getRoot());
            this.dateFormat = DateFormat.getDateInstance();
            this.binding = binding;

            this.binding.mapLocation.onCreate(null);
            this.binding.mapLocation.getMapAsync(this);
        }

        @Override
        public void onMapReady(@NonNull GoogleMap googleMap) {
            this.map = googleMap;
            if (this.cache != null) {
                this.onBindView(this.cache);
            }
        }

        public void onBindView(@NonNull Event event) {
            this.binding.labelDate.setText(this.dateFormat.format(event.getDate()));
            if (event.getComment() != null && !event.getComment().equals("")) {
                this.binding.labelComment.setText(event.getComment());
                this.binding.labelComment.setVisibility(View.VISIBLE);
            }
            if (event.getLocation() != null && this.map != null) {
                LatLng position = new LatLng(event.getLocation().getLatitude(), event.getLocation().getLongitude());
                this.map.clear();
                this.map.moveCamera(CameraUpdateFactory.newLatLng(position));
                this.map.addMarker(new MarkerOptions().position(position).draggable(true));
                this.map.setMapType(GoogleMap.MAP_TYPE_NORMAL);

                this.binding.mapLocation.setVisibility(View.VISIBLE);
            }

            this.cache = event;
        }
    }
}
