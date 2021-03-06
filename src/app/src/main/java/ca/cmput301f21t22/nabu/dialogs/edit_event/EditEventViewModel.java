package ca.cmput301f21t22.nabu.dialogs.edit_event;

import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.Date;

import ca.cmput301f21t22.nabu.data.Event;
import ca.cmput301f21t22.nabu.data.LatLngPoint;
import ca.cmput301f21t22.nabu.model.commands.UpdateEventCommand;
import ca.cmput301f21t22.nabu.model.commands.UploadLocalPhotoCommand;

public class EditEventViewModel extends ViewModel {
    @NonNull
    private final MutableLiveData<Boolean> saved;

    @NonNull
    private String id;
    @NonNull
    private final MutableLiveData<Date> date;
    @NonNull
    private final MutableLiveData<String> comment;
    @NonNull
    private final MutableLiveData<String> photoPath;
    @NonNull
    private final MutableLiveData<LatLngPoint> location;

    @Nullable
    private Uri localPhotoPath;

    public EditEventViewModel() {
        this.saved = new MutableLiveData<>();

        this.id = "";
        this.date = new MutableLiveData<>();
        this.comment = new MutableLiveData<>();
        this.photoPath = new MutableLiveData<>();
        this.location = new MutableLiveData<>();
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
    public LiveData<LatLngPoint> getLocation() {
        return this.location;
    }

    public void setLocation(@Nullable LatLngPoint location) {
        this.location.setValue(location);
        this.saved.setValue(false);
    }

    public void loadEvent(@NonNull Event event) {
        this.id = event.getId();
        this.date.setValue(event.getDate());
        this.comment.setValue(event.getComment());
        this.photoPath.setValue(event.getPhotoPath());
        this.location.setValue(event.getLocation());

        this.saved.setValue(false);
    }

    public void saveEvent() {
        Date date = this.date.getValue();
        if (date == null) {
            return;
        }

        new UpdateEventCommand(new Event(this.id, date, this.comment.getValue(), this.photoPath.getValue(),
                                         this.location.getValue())).execute()
                .thenAccept(event -> {
                    if (event != null) {
                        this.saved.setValue(true);
                    }
                });
    }

    public void setLocalPhotoPath(@Nullable Uri localPhotoPath) {
        this.localPhotoPath = localPhotoPath;
    }

    public void uploadLocalPhoto() {
        if (this.localPhotoPath == null) {
            return;
        }

        new UploadLocalPhotoCommand(this.localPhotoPath).execute().thenAccept(this.photoPath::setValue);
    }
}
