package ca.cmput301f21t22.nabu.ui.my_day;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableList;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

import ca.cmput301f21t22.nabu.model.Occurrence;
import ca.cmput301f21t22.nabu.model.Collections;
import ca.cmput301f21t22.nabu.model.Event;
import ca.cmput301f21t22.nabu.model.Habit;
import ca.cmput301f21t22.nabu.model.LiveEvent;
import ca.cmput301f21t22.nabu.model.LiveHabit;
import ca.cmput301f21t22.nabu.model.LiveUser;
import ca.cmput301f21t22.nabu.model.User;
import ca.cmput301f21t22.nabu.ui.DateChangedReceiver;

public class MyDayViewModel extends ViewModel {
    @NonNull
    public final static String TAG = "MyDayViewModel";

    @NonNull
    private final MutableLiveData<LocalDate> now;
    @NonNull
    private final ObservableList<MyDayCard> cards;

    @NonNull
    private final DateChangedReceiver dateReceiver;
    @NonNull
    private final CollectionReference users;
    @NonNull
    private final CollectionReference habits;
    @NonNull
    private final CollectionReference events;
    @NonNull
    private final FirebaseAuth auth;

    public MyDayViewModel() {
        this.now = new MutableLiveData<>();
        this.cards = new ObservableArrayList<>();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        this.users = db.collection(Collections.USERS.getName());
        this.habits = db.collection(Collections.HABITS.getName());
        this.events = db.collection(Collections.EVENTS.getName());
        this.auth = FirebaseAuth.getInstance();
        this.auth.addAuthStateListener(auth -> this.onSignInChanged());

        this.dateReceiver = new DateChangedReceiver();
        this.dateReceiver.observe(((sender, property) -> {
            if (property == DateChangedReceiver.Properties.NOW) {
                this.now.setValue(((DateChangedReceiver) sender).getNow());
                this.onSignInChanged();
            }
        }));
    }

    @NonNull
    public LiveData<LocalDate> getNow() {
        return this.now;
    }

    @NonNull
    public ObservableList<MyDayCard> getCards() {
        return this.cards;
    }

    @NonNull
    public DateChangedReceiver getDateReceiver() {
        return this.dateReceiver;
    }

    public void onSignInChanged() {
        if (this.auth.getCurrentUser() != null) {
            new LiveUser(this.users.document(this.auth.getCurrentUser().getUid())).observe((sender, property) -> {
                LiveUser user = (LiveUser) sender;
                if (property == User.Properties.HABITS && user.getHabits() != null) {
                    this.onHabitsChanged(user.getHabits());
                }
            });
        }
    }

    public void onHabitsChanged(@NonNull List<String> habitIds) {
        for (MyDayCard card : this.cards) {
            card.setAlive(false);
        }

        for (String habitId : habitIds) {
            this.onHabitChanged(habitId);
        }

        this.cards.removeIf(card -> !card.isAlive());
    }

    public void onHabitChanged(@NonNull String habitId) {
        // Check to see if the habit already exists within the list.
        boolean existing = false;
        for (MyDayCard card : this.cards) {
            if (habitId.equals(card.getId())) {
                // Same habit, but potentially different events.
                card.setAlive(true);
                existing = true;
                break;
            }
        }

        // If it doesn't, add it.
        if (!existing) {
            this.cards.add(new MyDayCard(habitId, null, null));
        }

        // Update elements.
        new LiveHabit(this.habits.document(habitId)).observe(((sender, property) -> {
            LiveHabit habit = (LiveHabit) sender;
            if (property == Habit.Properties.TITLE && habit.getTitle() != null) {
                this.onHabitTitleChanged(habit.getId(), habit.getTitle());
            } else if (property == Habit.Properties.EVENTS && habit.getEvents() != null) {
                for (String eventId : habit.getEvents()) {
                    this.onHabitEventChanged(habit.getId(), eventId);
                }
            } else if (property == Habit.Properties.OCCURRENCE && habit.getOccurrence() != null) {
                this.onHabitOccurrenceChanged(habit.getId(), habit.getOccurrence());
            }
        }));
    }

    public void onHabitTitleChanged(@NonNull String habitId, @NonNull String title) {
        ListIterator<MyDayCard> iterator = this.cards.listIterator();
        while (iterator.hasNext()) {
            MyDayCard card = iterator.next();
            if (habitId.equals(card.getId())) {
                iterator.set(new MyDayCard(habitId, title, card.getOccurrence(), card.getMarkers()));
                return;
            }
        }
    }

    public void onHabitOccurrenceChanged(@NonNull String habitId, @NonNull Occurrence occurrence) {
        Set<DayOfWeek> due = occurrence.asSetOfDaysOfWeek();
        ListIterator<MyDayCard> iterator = this.cards.listIterator();
        while (iterator.hasNext()) {
            MyDayCard card = iterator.next();
            if (habitId.equals(card.getId())) {
                MyDayCardMarker[] markers = card.getMarkers();
                LocalDate day = this.dateReceiver.getNow();
                for (int i = 0; i < 7; i++) {
                    MyDayCardMarker.Icon icon;

                    // The event is due today
                    if (due.contains(day.getDayOfWeek())) {
                        String id = markers[i].getId();
                        Boolean complete = markers[i].isComplete();
                        if ((id == null || complete == null || !complete) && i == 0) {
                            icon = MyDayCardMarker.Icon.INCOMPLETE;
                        } else if (id == null || complete == null || !complete) {
                            icon = MyDayCardMarker.Icon.FAILED;
                        } else {
                            icon = MyDayCardMarker.Icon.COMPLETE;
                        }
                    }
                    // The event is not due today
                    else {
                        icon = MyDayCardMarker.Icon.NOT_DUE;
                    }

                    markers[i] = new MyDayCardMarker(markers[i].getId(), markers[i].isComplete(), icon);
                    day = day.minusDays(1);
                }

                iterator.set(new MyDayCard(habitId, card.getTitle(), occurrence, markers));
                return;
            }
        }
    }

    public void onHabitEventChanged(@NonNull String habitId, @NonNull String eventId) {
        new LiveEvent(this.events.document(eventId)).observe(((sender, property) -> {
            LiveEvent event = (LiveEvent) sender;
            if (property == Event.Properties.DATE) {
                ListIterator<MyDayCard> iterator = this.cards.listIterator();
                while (iterator.hasNext()) {
                    MyDayCard card = iterator.next();
                    Occurrence occurrence = card.getOccurrence();
                    if (habitId.equals(card.getId()) && occurrence != null) {
                        Date eventDate = event.getDate();
                        MyDayCardMarker[] markers = card.getMarkers();
                        Set<DayOfWeek> dueOn = occurrence.asSetOfDaysOfWeek();

                        LocalDate day = this.dateReceiver.getNow();
                        for (int i = 0; i < 7; i++) {
                            boolean due = dueOn.contains(day.getDayOfWeek());
                            if (eventId.equals(markers[i].getId())) {
                                markers[i] = new MyDayCardMarker(null, null, due ? MyDayCardMarker.Icon.FAILED :
                                                                             MyDayCardMarker.Icon.NOT_DUE);
                            }
                            day = day.minusDays(1);
                        }

                        if (eventDate != null) {
                            LocalDate eventLocalDate =
                                    eventDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                            LocalDate today = this.dateReceiver.getNow();
                            if (!(eventLocalDate.isBefore(today.minusDays(6)) || eventLocalDate.isAfter(today))) {
                                int index = (int) ChronoUnit.DAYS.between(eventLocalDate, today);
                                boolean due = dueOn.contains(eventLocalDate.getDayOfWeek());
                                markers[index] =
                                        new MyDayCardMarker(eventId, true, due ? MyDayCardMarker.Icon.COMPLETE :
                                                                           (index == 0 ?
                                                                            MyDayCardMarker.Icon.INCOMPLETE :
                                                                            MyDayCardMarker.Icon.FAILED));
                                iterator.set(new MyDayCard(habitId, card.getTitle(), card.getOccurrence(), markers));
                            }
                        }
                    }
                }
            }
        }));
    }
}
