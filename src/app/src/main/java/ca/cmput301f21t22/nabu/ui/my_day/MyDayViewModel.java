package ca.cmput301f21t22.nabu.ui.my_day;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import ca.cmput301f21t22.nabu.model.Collections;
import ca.cmput301f21t22.nabu.model.LiveHabit;
import ca.cmput301f21t22.nabu.model.LiveUser;

public class MyDayViewModel extends ViewModel {
    @NonNull
    public final static String TAG = "MyDayViewModel";
    @NonNull
    private final MutableLiveData<List<LiveHabit>> userHabits;
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

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        this.users = db.collection(Collections.USERS.getName());
        this.habits = db.collection(Collections.HABITS.getName());
        this.events = db.collection(Collections.EVENTS.getName());
        this.auth = FirebaseAuth.getInstance();
        this.auth.addAuthStateListener(this::onSignInChanged);
    }

    @NonNull
    public LiveData<List<LiveHabit>> getUserHabits() {
        return this.userHabits;
    }

    public void onSignInChanged(@NonNull FirebaseAuth newAuth) {
        if (newAuth.getCurrentUser() != null) {
            LiveUser currentUser = new LiveUser(this.users.document(newAuth.getCurrentUser().getUid()));
            currentUser.observeProperties((sender, property) -> {
                if (property == LiveUser.Properties.HABITS) {
                    this.onCurrentUserHabitsChanged(((LiveUser) sender).getHabits());
                }
            });
        }
    }

    public void onCurrentUserHabitsChanged(@Nullable List<String> newHabitIds) {
        List<LiveHabit> newHabits = new ArrayList<>();
        if (newHabitIds != null) {
            for (String id : newHabitIds) {
                LiveHabit habit = new LiveHabit(this.habits.document(id));
                newHabits.add(habit);
            }
        }
        this.userHabits.setValue(newHabits);
    }
}
