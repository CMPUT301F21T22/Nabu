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
import java.util.List;
import java.util.Map;
import java.util.Objects;

import ca.cmput301f21t22.nabu.data.Event;
import ca.cmput301f21t22.nabu.data.Habit;
import ca.cmput301f21t22.nabu.data.MyDayCard;
import ca.cmput301f21t22.nabu.data.MyDayUserCard;
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
    @NonNull
    private final List<MyDayUserCard> followingUserCardsList;
    @NonNull
    private final MutableLiveData<List<MyDayUserCard>> followingUserCards;
    @NonNull
    private final MutableLiveData<List<MyDayUserCard>> generalUserCards;

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
    @Nullable
    private Map<String, User> allCurrentUsers;

    public MyDayViewModel() {
        this.cardsList = new ArrayList<>();
        this.incompleteCards = new MutableLiveData<>();
        this.completeCards = new MutableLiveData<>();

        this.followingUserCardsList = new ArrayList<>();
        this.followingUserCards = new MutableLiveData<>();
        this.generalUserCards = new MutableLiveData<>();

        this.mostRecentEvent = null;
        this.instantShowEdit = new MutableLiveData<>();
    }

    @NonNull
    public LiveData<List<MyDayCard>> getIncompleteCards() {
        return this.incompleteCards;
    }

    @NonNull
    public LiveData<List<MyDayCard>> getCompleteCards() {
        return this.completeCards;
    }

    @NonNull
    public LiveData<List<MyDayUserCard>> getFollowingUserCards() {
        return this.followingUserCards;
    }

    @NonNull
    public Event getMostRecentEvent() {
        return Objects.requireNonNull(this.mostRecentEvent);
    }

    @NonNull
    public LiveData<Boolean> getInstantShowEdit() {
        return this.instantShowEdit;
    }

    public void setCurrentUser(@Nullable User currentUser) {
        this.currentUser = currentUser;
        this.onUserDataChanged();
        this.onSocialDataChanged();
    }

    public void setCurrentHabits(@Nullable Map<String, Habit> currentHabits) {
        this.currentHabits = currentHabits;
        this.onUserDataChanged();
        this.onSocialDataChanged();
    }

    public void setCurrentEvents(@Nullable Map<String, Event> currentEvents) {
        this.currentEvents = currentEvents;
        this.onUserDataChanged();
        this.onSocialDataChanged();
    }

    public void setAllCurrentUsers(@Nullable Map<String, User> allUsers) {
        this.allCurrentUsers = allUsers;
        this.onSocialDataChanged();
    }

    public void onCardClicked(@NonNull MyDayCard card) {
        Event todayEvent = card.getEvents()[0];
        boolean complete = todayEvent != null;
        if (complete) {
            new DeleteEventCommand(todayEvent).execute();
        } else {
            new AddEventCommand(card.getHabit(), new Event()).execute().thenAccept(event -> {
                this.mostRecentEvent = event;
                this.instantShowEdit.setValue(true);
            });
        }
    }

    private void onUserDataChanged() {
        this.cardsList.clear();

        if (this.currentUser != null && this.currentHabits != null) {
            List<String> habitIds = this.currentUser.getHabits();
            // Process new habits.
            for (String habitId : habitIds) {
                Habit habit = this.currentHabits.get(habitId);
                LocalDate today = LocalDate.now();
                if (habit != null) {
                    LocalDate startDate = habit.getStartDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                    if (startDate.isBefore(today) || startDate.isEqual(today)) {
                        this.cardsList.add(this.processHabit(habit));
                    }
                }
            }
        }

        this.updateUserLists();
    }

    @NonNull
    private MyDayCard processHabit(@NonNull Habit habit) {
        Event[] relevantEvents = new Event[7];
        List<String> eventIds = habit.getEvents();
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

    @NonNull
    private MyDayUserCard processUser(@NonNull User user, Map<String, Habit> currentSocialHabits) {
        ArrayList<MyDayCard> habits = new ArrayList<>();
        for (String habitId : user.getHabits()) {
            Habit habit = currentSocialHabits.get(habitId);
            if (habit != null && habit.isShared() == true) {
                LocalDate startDate = habit.getStartDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                if (startDate.isBefore(LocalDate.now()) || startDate.isEqual(LocalDate.now())) {
                    habits.add(this.processHabit(habit));
                }
            }
        }
        return new MyDayUserCard(user, habits);
    }

    private void updateUserLists() {
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

    private void onSocialDataChanged() {
        this.followingUserCardsList.clear();

        if (this.currentUser != null && this.allCurrentUsers != null) {
            //Process new users.
            for (String followingUserId : this.currentUser.getFollowing()) {
                User followingUser = this.allCurrentUsers.get(followingUserId);
                if (followingUser != null) {
                    this.followingUserCardsList.add(this.processUser(followingUser, this.currentHabits));
                }
            }
        }

        this.updateSocialLists();
    }

    private void updateSocialLists() {
        this.followingUserCards.setValue(this.followingUserCardsList);
    }
}
