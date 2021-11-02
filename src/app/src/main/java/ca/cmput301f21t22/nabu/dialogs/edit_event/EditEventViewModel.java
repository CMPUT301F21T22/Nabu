package ca.cmput301f21t22.nabu.dialogs.edit_event;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.firestore.GeoPoint;

import java.util.Date;

import ca.cmput301f21t22.nabu.data.Event;

public class EditEventViewModel extends ViewModel {
    private boolean saveable;
    @Nullable
    private Event savedEvent;
    @NonNull
    private final MutableLiveData<Boolean> saved;

    @NonNull
    private final MutableLiveData<String> id;
    @NonNull
    private final MutableLiveData<Date> date;
    @NonNull
    private final MutableLiveData<String> comment;
    @NonNull
    private final MutableLiveData<String> photoPath;
    @NonNull
    private final MutableLiveData<GeoPoint> location;

    public EditEventViewModel() {
        this.saved = new MutableLiveData<>();

        this.id = new MutableLiveData<>();
        this.date = new MutableLiveData<>();
        this.comment = new MutableLiveData<>();
        this.photoPath = new MutableLiveData<>();
        this.location = new MutableLiveData<>();
    }

    @NonNull
    public LiveData<String> getId() {
        return this.id;
    }

    public void setSaveable(boolean saveable) {
        this.saveable = saveable;
    }

    @Nullable
    public Event getSavedEvent() {
        return this.savedEvent;
    }

    @NonNull
    public LiveData<Boolean> isSaved() {
        return this.saved;
    }

    @NonNull
    public LiveData<Date> getDate() {
        return this.date;
    }

    public void setDate(@NonNull Date date) {
        this.date.setValue(date);
        this.saved.setValue(false);
    }

    @NonNull
    public LiveData<String> getComment() {
        return this.comment;
    }

    public void setComment(@Nullable String comment) {
        this.comment.setValue(comment);
        this.saved.setValue(false);
    }

    @NonNull
    public LiveData<String> getPhotoPath() {
        return this.photoPath;
    }

    public void setPhotoPath(@Nullable String photoPath) {
        this.photoPath.setValue(photoPath);
        this.saved.setValue(false);
    }

    @NonNull
    public LiveData<GeoPoint> getLocation() {
        return this.location;
    }

    public void setLocation(@Nullable GeoPoint location) {
        this.location.setValue(location);
        this.saved.setValue(false);
    }

    public void loadEvent(@NonNull Event event) {
        this.id.setValue(event.getId());

        this.date.setValue(event.getDate());
        this.comment.setValue(event.getComment());
        this.photoPath.setValue(event.getPhotoPath());
        this.location.setValue(event.getLocation());

        this.saved.setValue(false);
    }

    public void saveEvent() {
        String id = this.id.getValue();
        Date date = this.date.getValue();
        if (!this.saveable || id == null || date == null) {
            return;
        }

        this.savedEvent =
                new Event(id, date, this.comment.getValue(), this.photoPath.getValue(), this.location.getValue());
        this.saved.setValue(true);
    }
}
