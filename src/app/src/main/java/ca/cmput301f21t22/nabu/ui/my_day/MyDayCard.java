package ca.cmput301f21t22.nabu.ui.my_day;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import ca.cmput301f21t22.nabu.data.Occurrence;

public class MyDayCard {
    @NonNull
    private final String id;
    @Nullable
    private final String title;
    @Nullable
    private final Occurrence occurrence;
    @NonNull
    private final MyDayCardMarker[] markers;

    private boolean alive;

    public MyDayCard(@NonNull String id, @Nullable String title, @Nullable Occurrence occurrence) {
        this(id, title, occurrence, new MyDayCardMarker[]{
                new MyDayCardMarker(null, null, MyDayCardMarker.Icon.INCOMPLETE),
                new MyDayCardMarker(null, null, MyDayCardMarker.Icon.NOT_DUE),
                new MyDayCardMarker(null, null, MyDayCardMarker.Icon.NOT_DUE),
                new MyDayCardMarker(null, null, MyDayCardMarker.Icon.NOT_DUE),
                new MyDayCardMarker(null, null, MyDayCardMarker.Icon.NOT_DUE),
                new MyDayCardMarker(null, null, MyDayCardMarker.Icon.NOT_DUE),
                new MyDayCardMarker(null, null, MyDayCardMarker.Icon.NOT_DUE),
                });
    }

    public MyDayCard(@NonNull String id,
                     @Nullable String title,
                     @Nullable Occurrence occurrence,
                     @NonNull MyDayCardMarker[] markers) {
        this.id = id;
        this.title = title;
        this.occurrence = occurrence;
        this.markers = markers;

        this.alive = true;
    }

    @NonNull
    public String getId() {
        return this.id;
    }

    @Nullable
    public String getTitle() {
        return this.title;
    }

    @Nullable
    public Occurrence getOccurrence() {
        return this.occurrence;
    }

    @NonNull
    public MyDayCardMarker[] getMarkers() {
        return this.markers;
    }

    public boolean isAlive() {
        return this.alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }
}
