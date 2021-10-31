package ca.cmput301f21t22.nabu.ui.my_day;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import ca.cmput301f21t22.nabu.model.Collection;
import ca.cmput301f21t22.nabu.model.Habit;
import ca.cmput301f21t22.nabu.model.User;

public class MyDayViewModel extends ViewModel {
    @NonNull
    public final static String TAG = "MyDayViewModel";
    @NonNull
    private final MutableLiveData<List<Habit>> userHabits;
    @NonNull
    private final FirebaseFirestore db;
    @NonNull
    private final CollectionReference users;
    @NonNull
    private final CollectionReference habits;
    @NonNull
    private final CollectionReference events;
    @NonNull
    private final FirebaseAuth auth;

    public MyDayViewModel() {
        this.userHabits = new MutableLiveData<>();

        this.db = FirebaseFirestore.getInstance();
        this.users = this.db.collection(Collection.USERS.getName());
        this.habits = this.db.collection(Collection.HABITS.getName());
        this.events = this.db.collection(Collection.EVENTS.getName());
        this.auth = FirebaseAuth.getInstance();
        this.auth.addAuthStateListener(this::onSignInChanged);
    }

    @NonNull
    public LiveData<List<Habit>> getUserHabits() {
        return this.userHabits;
    }

    public void onSignInChanged(@NonNull FirebaseAuth newAuth) {
        if (newAuth.getCurrentUser() != null) {
            User currentUser = new User(this.users.document(newAuth.getCurrentUser().getUid()));
            currentUser.observeProperties((sender, property) -> {
                if (property == User.Properties.HABITS) {
                    this.onCurrentUserHabitsChanged(((User) sender).getHabits());
                }
            });
        }
    }

    public void onCurrentUserHabitsChanged(List<String> newHabitIds) {
        List<Habit> newHabits = new ArrayList<>();
        if (newHabitIds != null) {
            for (String id : newHabitIds) {
                Habit habit = new Habit(this.habits.document(id));
                newHabits.add(habit);
            }
        }
        this.userHabits.setValue(newHabits);
    }
}
