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
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import ca.cmput301f21t22.nabu.data.Event;
import ca.cmput301f21t22.nabu.data.Habit;
import ca.cmput301f21t22.nabu.data.MyDayCard;
import ca.cmput301f21t22.nabu.data.User;

public class MyDayViewModel extends ViewModel {
    @NonNull
    public final static String TAG = "MyDayViewModel";

    @NonNull
    private final List<MyDayCard> cardsList;
    @NonNull
    private final MutableLiveData<List<MyDayCard>> cards;

    @Nullable
    private User currentUser;
    @Nullable
    private Map<String, Habit> currentHabits;
    @Nullable
    private Map<String, Event> currentEvents;

    public MyDayViewModel() {
        this.cardsList = new ArrayList<>();
        this.cards = new MutableLiveData<>();
    }

    @NonNull
    public LiveData<List<MyDayCard>> getCards() {
        return this.cards;
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

    private void onDataChanged() {
        if (this.currentUser == null || this.currentHabits == null || this.currentUser.getHabits().size() == 0 ||
            this.currentHabits.size() == 0) {
            this.cardsList.clear();
            this.cards.setValue(this.cardsList);
            return;
        }

        Set<String> habitIds = new HashSet<>(this.currentUser.getHabits());
        ListIterator<MyDayCard> cardIterator = this.cardsList.listIterator();
        while (cardIterator.hasNext()) {
            Habit habit = cardIterator.next().getHabit();
            // Replace.
            if (habitIds.contains(habit.getId())) {
                cardIterator.set(this.processHabit(habit));
                // Remove the id from the set to indicate we've processed it.
                habitIds.remove(habit.getId());
            }
            // Remove.
            else {
                cardIterator.remove();
            }
        }

        // Process new habits.
        for (String habitId : habitIds) {
            this.cardsList.add(this.processHabit(Objects.requireNonNull(this.currentHabits.get(habitId))));
        }

        this.cards.setValue(this.cardsList);
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
}
