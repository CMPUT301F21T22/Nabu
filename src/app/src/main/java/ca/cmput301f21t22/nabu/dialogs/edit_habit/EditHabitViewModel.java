package ca.cmput301f21t22.nabu.dialogs.edit_habit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ca.cmput301f21t22.nabu.data.Habit;
import ca.cmput301f21t22.nabu.data.Occurrence;
import ca.cmput301f21t22.nabu.data.User;
import ca.cmput301f21t22.nabu.model.commands.AddHabitCommand;
import ca.cmput301f21t22.nabu.model.commands.UpdateHabitCommand;

public class EditHabitViewModel extends ViewModel {
    @NonNull
    private final MutableLiveData<Boolean> saved;

    @Nullable
    private String id;
    @NonNull
    private final MutableLiveData<String> title;
    @NonNull
    private final MutableLiveData<String> reason;
    @NonNull
    private final MutableLiveData<Date> startDate;
    @NonNull
    private final MutableLiveData<Boolean> onSunday;
    @NonNull
    private final MutableLiveData<Boolean> onMonday;
    @NonNull
    private final MutableLiveData<Boolean> onTuesday;
    @NonNull
    private final MutableLiveData<Boolean> onWednesday;
    @NonNull
    private final MutableLiveData<Boolean> onThursday;
    @NonNull
    private final MutableLiveData<Boolean> onFriday;
    @NonNull
    private final MutableLiveData<Boolean> onSaturday;
    @NonNull
    private List<String> events;
    @NonNull
    private final MutableLiveData<Boolean> shared;

    @Nullable
    private User currentUser;

    public EditHabitViewModel() {
        this.saved = new MutableLiveData<>();

        this.id = null;
        this.title = new MutableLiveData<>();
        this.reason = new MutableLiveData<>();
        this.startDate = new MutableLiveData<>();
        this.onSunday = new MutableLiveData<>();
        this.onMonday = new MutableLiveData<>();
        this.onTuesday = new MutableLiveData<>();
        this.onWednesday = new MutableLiveData<>();
        this.onThursday = new MutableLiveData<>();
        this.onFriday = new MutableLiveData<>();
        this.onSaturday = new MutableLiveData<>();
        this.events = new ArrayList<>();
        this.shared = new MutableLiveData<>();
    }

    @NonNull
    public LiveData<Boolean> isSaved() {
        return this.saved;
    }

    @NonNull
    public LiveData<String> getTitle() {
        return this.title;
    }

    public void setTitle(@NonNull String title) {
        this.title.setValue(title);
        this.saved.setValue(false);
    }

    @NonNull
    public LiveData<String> getReason() {
        return this.reason;
    }

    public void setReason(@NonNull String reason) {
        this.reason.setValue(reason);
        this.saved.setValue(false);
    }

    @NonNull
    public LiveData<Date> getStartDate() {
        return this.startDate;
    }

    public void setStartDate(@NonNull Date startDate) {
        this.startDate.setValue(startDate);
        this.saved.setValue(false);
    }

    @NonNull
    public LiveData<Boolean> isOnSunday() {
        return this.onSunday;
    }

    public void setOnSunday(boolean onSunday) {
        this.onSunday.setValue(onSunday);
        this.saved.setValue(false);
    }

    @NonNull
    public LiveData<Boolean> isOnMonday() {
        return this.onMonday;
    }

    public void setOnMonday(boolean onMonday) {
        this.onMonday.setValue(onMonday);
        this.saved.setValue(false);
    }

    @NonNull
    public LiveData<Boolean> isOnTuesday() {
        return this.onTuesday;
    }

    public void setOnTuesday(boolean onTuesday) {
        this.onTuesday.setValue(onTuesday);
        this.saved.setValue(false);
    }

    @NonNull
    public LiveData<Boolean> isOnWednesday() {
        return this.onWednesday;
    }

    public void setOnWednesday(boolean onWednesday) {
        this.onWednesday.setValue(onWednesday);
        this.saved.setValue(false);
    }

    @NonNull
    public LiveData<Boolean> isOnThursday() {
        return this.onThursday;
    }

    public void setOnThursday(boolean onThursday) {
        this.onThursday.setValue(onThursday);
        this.saved.setValue(false);
    }

    @NonNull
    public LiveData<Boolean> isOnFriday() {
        return this.onFriday;
    }

    public void setOnFriday(boolean onFriday) {
        this.onFriday.setValue(onFriday);
        this.saved.setValue(false);
    }

    @NonNull
    public LiveData<Boolean> isOnSaturday() {
        return this.onSaturday;
    }

    public void setOnSaturday(boolean onSaturday) {
        this.onSaturday.setValue(onSaturday);
        this.saved.setValue(false);
    }

    @NonNull
    public LiveData<Boolean> isShared() {
        return this.shared;
    }

    public void setShared(boolean shared) {
        this.shared.setValue(shared);
        this.saved.setValue(false);
    }

    public void setCurrentUser(@Nullable User currentUser) {
        this.currentUser = currentUser;
    }

    public void loadHabit(@Nullable Habit habit) {
        if (habit != null) {
            this.id = habit.getId();
            this.title.setValue(habit.getTitle());
            this.reason.setValue(habit.getReason());
            this.startDate.setValue(habit.getStartDate());

            Occurrence occurrence = habit.getOccurrence();
            this.onSunday.setValue(occurrence.isOnSunday());
            this.onMonday.setValue(occurrence.isOnMonday());
            this.onTuesday.setValue(occurrence.isOnTuesday());
            this.onWednesday.setValue(occurrence.isOnWednesday());
            this.onThursday.setValue(occurrence.isOnThursday());
            this.onFriday.setValue(occurrence.isOnFriday());
            this.onSaturday.setValue(occurrence.isOnSaturday());

            this.events = habit.getEvents();
            this.shared.setValue(habit.isShared());
        }
    }

    public void saveHabit() {
        String title = this.title.getValue();
        String reason = this.reason.getValue();
        Date startDate = this.startDate.getValue();
        Boolean shared = this.shared.getValue();

        Boolean onSunday = this.onSunday.getValue();
        Boolean onMonday = this.onMonday.getValue();
        Boolean onTuesday = this.onTuesday.getValue();
        Boolean onWednesday = this.onWednesday.getValue();
        Boolean onThursday = this.onThursday.getValue();
        Boolean onFriday = this.onFriday.getValue();
        Boolean onSaturday = this.onSaturday.getValue();

        Occurrence occurrence = new Occurrence(onSunday != null ? onSunday : false, onMonday != null ? onMonday : false,
                                               onTuesday != null ? onTuesday : false,
                                               onWednesday != null ? onWednesday : false,
                                               onThursday != null ? onThursday : false,
                                               onFriday != null ? onFriday : false,
                                               onSaturday != null ? onSaturday : false);

        Habit savedHabit =
                new Habit(this.id != null ? this.id : "", title != null ? title : "", reason != null ? reason : "",
                          startDate != null ? startDate : new Date(), occurrence, this.events,
                          shared != null ? shared : false);

        if (this.id != null) {
            new UpdateHabitCommand(savedHabit).execute().thenAccept(habit -> {
                if (habit != null) {
                    this.saved.setValue(true);
                }
            });
        } else if (this.currentUser != null) {
            new AddHabitCommand(this.currentUser, savedHabit).execute().thenAccept(habit -> {
                if (habit != null) {
                    this.saved.setValue(true);
                    this.id = habit.getId();
                }
            });
        }
    }
}
