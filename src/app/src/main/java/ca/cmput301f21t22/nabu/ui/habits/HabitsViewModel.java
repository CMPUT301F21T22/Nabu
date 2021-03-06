package ca.cmput301f21t22.nabu.ui.habits;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ca.cmput301f21t22.nabu.data.Event;
import ca.cmput301f21t22.nabu.data.EventChronologicalComparator;
import ca.cmput301f21t22.nabu.data.Habit;
import ca.cmput301f21t22.nabu.data.HabitCard;
import ca.cmput301f21t22.nabu.data.User;
import ca.cmput301f21t22.nabu.model.commands.DeleteEventCommand;
import ca.cmput301f21t22.nabu.model.commands.DeleteHabitCommand;
import ca.cmput301f21t22.nabu.model.commands.ReorderHabitCommand;

public class HabitsViewModel extends ViewModel {
    @NonNull
    public final static String TAG = "HabitsViewModel";

    @NonNull
    private final List<HabitCard> cardsList;
    @NonNull
    private final MutableLiveData<List<HabitCard>> cards;

    @Nullable
    private User currentUser;
    @Nullable
    private Map<String, Habit> currentHabits;
    @Nullable
    private Map<String, Event> currentEvents;

    public HabitsViewModel() {
        this.cardsList = new ArrayList<>();
        this.cards = new MutableLiveData<>();
    }

    @Nullable
    public User getCurrentUser() {
        return this.currentUser;
    }

    public void setCurrentUser(@Nullable User currentUser) {
        this.currentUser = currentUser;
        this.onDataChanged();
    }

    @NonNull
    public LiveData<List<HabitCard>> getCards() {
        return this.cards;
    }

    public void setCurrentHabits(@Nullable Map<String, Habit> currentHabits) {
        this.currentHabits = currentHabits;
        this.onDataChanged();
    }

    public void setCurrentEvents(@Nullable Map<String, Event> currentEvents) {
        this.currentEvents = currentEvents;
        this.onDataChanged();
    }

    public void onCardClicked(int position) {
        HabitCard oldCard = this.cardsList.get(position);
        HabitCard newCard = new HabitCard(oldCard.getHabit(), oldCard.getEvents());
        newCard.setExpanded(!oldCard.isExpanded());
        this.cardsList.set(position, newCard);
        this.cards.setValue(this.cardsList);
    }

    public boolean reorderHabits(int originalPosition, int targetPosition) {
        if (this.currentUser != null) {
            try {
                new ReorderHabitCommand(this.currentUser, originalPosition, targetPosition).execute();
                return true;
            } catch (Exception e) {
                return false;
            }
        }
        return false;
    }

    public void deleteHabit(@NonNull Habit habit) {
        new DeleteHabitCommand(habit).execute();
    }

    public void deleteEvent(@NonNull Event event) {
        new DeleteEventCommand(event).execute();
    }

    private void onDataChanged() {
        Map<String, Boolean> cards = new HashMap<>();
        for (HabitCard card : this.cardsList) {
            cards.put(card.getHabit().getId(), card.isExpanded());
        }

        this.cardsList.clear();

        if (this.currentUser != null && this.currentHabits != null) {
            List<String> habitIds = this.currentUser.getHabits();
            // Process new habits.
            for (String habitId : habitIds) {
                Habit habit = this.currentHabits.get(habitId);
                if (habit != null) {
                    Boolean expanded = cards.getOrDefault(habitId, false);
                    this.cardsList.add(this.processHabit(habit, expanded != null ? expanded : false));
                }
            }
        }

        this.cards.setValue(this.cardsList);
    }

    @NonNull
    private HabitCard processHabit(@NonNull Habit habit, boolean expanded) {
        ArrayList<Event> events = new ArrayList<>();
        List<String> eventIds = habit.getEvents();
        if (this.currentEvents != null) {
            for (String eventId : eventIds) {
                events.add(this.currentEvents.get(eventId));
            }
        }
        events.sort(new EventChronologicalComparator());

        HabitCard card = new HabitCard(habit, events);
        card.setExpanded(expanded);
        return card;
    }
}
