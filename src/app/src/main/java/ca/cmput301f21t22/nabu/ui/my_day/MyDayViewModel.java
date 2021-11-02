package ca.cmput301f21t22.nabu.ui.my_day;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import ca.cmput301f21t22.nabu.data.Event;
import ca.cmput301f21t22.nabu.data.Habit;
import ca.cmput301f21t22.nabu.data.MyDayCard;
import ca.cmput301f21t22.nabu.data.User;
import ca.cmput301f21t22.nabu.model.commands.AddEventCommand;
import ca.cmput301f21t22.nabu.model.commands.DeleteEventCommand;

public class MyDayViewModel extends ViewModel {
    @NonNull
    public final static String TAG = "MyDayViewModel";

    @NonNull
    private final List<MyDayCard> cardsList;
    @NonNull
    private final MutableLiveData<List<MyDayCard>> incompleteCards;
    @NonNull
    private final MutableLiveData<List<MyDayCard>> completeCards;

    @Nullable
    private Event mostRecentEvent;
    @NonNull
    private final MutableLiveData<Boolean> instantShowEdit;

    @Nullable
    private User currentUser;
    @Nullable
    private Map<String, Habit> currentHabits;
    @Nullable
    private Map<String, Event> currentEvents;

    public MyDayViewModel() {
        this.cardsList = new ArrayList<>();
        this.incompleteCards = new MutableLiveData<>();
        this.completeCards = new MutableLiveData<>();

        this.mostRecentEvent = null;
        this.instantShowEdit = new MutableLiveData<>();
    }

    @NonNull
    public LiveData<List<MyDayCard>> getIncompleteCards() {
        return this.incompleteCards;
    }

    @NonNull
    public MutableLiveData<List<MyDayCard>> getCompleteCards() {
        return this.completeCards;
    }

    @NonNull
    public MutableLiveData<Boolean> getInstantShowEdit() {
        return this.instantShowEdit;
    }

    public void setCurrentUser(@Nullable User currentUser) {
        this.currentUser = currentUser;
        this.onDataChanged();
    }

    public void setCurrentHabits(@Nullable Map<String, Habit> currentHabits) {
        this.currentHabits = currentHabits;
        this.onDataChanged();
    }

    public void setCurrentEvents(@Nullable Map<String, Event> currentEvents) {
        this.currentEvents = currentEvents;
        this.onDataChanged();
    }

    public void onCardClicked(@NonNull MyDayCard card) {
        Event todayEvent = card.getEvents()[0];
        boolean complete = todayEvent != null;
        if (complete) {
            new DeleteEventCommand(todayEvent).execute();
        } else {
            new AddEventCommand(card.getHabit(), new Event(new Date())).execute().thenAccept(event -> {
                this.mostRecentEvent = event;
                this.instantShowEdit.setValue(true);
            });
        }
    }

    public void onEditMostRecentEvent() {
        if (this.mostRecentEvent != null) {
            // Spawn editor.
        }
    }

    private void onDataChanged() {
        this.cardsList.clear();

        if (this.currentUser != null && this.currentHabits != null) {
            List<String> habitIds = this.currentUser.getHabits();
            // Process new habits.
            for (String habitId : habitIds) {
                this.cardsList.add(this.processHabit(Objects.requireNonNull(this.currentHabits.get(habitId))));
            }
        }

        this.updateLists();
    }

    @NonNull
    private MyDayCard processHabit(@NonNull Habit habit) {
        Event[] relevantEvents = new Event[7];
        List<String> eventIds = Objects.requireNonNull(habit).getEvents();
        if (this.currentEvents != null) {
            for (String eventId : eventIds) {
                Event event = Objects.requireNonNull(this.currentEvents.get(eventId));
                LocalDate eventDate = event.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                LocalDate today = LocalDate.now();
                if (!(eventDate.isBefore(today.minusDays(6)) || eventDate.isAfter(today))) {
                    int index = (int) ChronoUnit.DAYS.between(eventDate, today);
                    relevantEvents[index] = event;
                }
            }
        }
        return new MyDayCard(habit, relevantEvents);
    }

    private void updateLists() {
        List<MyDayCard> incomplete = new ArrayList<>();
        List<MyDayCard> complete = new ArrayList<>();

        LocalDate today = LocalDate.now();
        for (MyDayCard card : this.cardsList) {
            Habit habit = card.getHabit();
            boolean due = habit.getOccurrence().isOnDayOfWeek(today.getDayOfWeek());
            if (card.getEvents()[0] != null && due) {
                complete.add(card);
            } else if (due) {
                incomplete.add(card);
            }
        }

        this.incompleteCards.setValue(incomplete);
        this.completeCards.setValue(complete);
    }
}
