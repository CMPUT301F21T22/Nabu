package ca.cmput301f21t22.nabu;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.NoSuchElementException;
import java.util.TreeSet;

/**
 * A collection object holding an arbitrary number of unique Habits, with uniqueness determined by habit title. No two
 * Habits in the HabitList may have the same title.
 * <p>
 * HabitList is bound to a Firestore collection, and is fully responsible for ensuring the consistency between the local
 * instance of the class, and the remote collection.
 */
public class HabitList {
    @NonNull
    private final TreeSet<Habit> habits;

    public HabitList() {
        this.habits = new TreeSet<>(new HabitComparator());
    }

    public HabitList(@NonNull Collection<Habit> habits) {
        this();
        this.habits.addAll(habits);
    }

    /**
     * Indicates whether another Object is equivalent to this one.
     *
     * @param obj Other object.
     * @return Whether the Objects are equivalent.
     */
    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == null) {
            return false;
        } else if (!(obj instanceof HabitList)) {
            return false;
        } else {
            return this.equals((HabitList) obj);
        }
    }

    /**
     * Add an Habit if it doesn't already exist within the list.
     *
     * @param habit Habit to add.
     * @return Whether or not the Habit was successfully added.
     */
    public boolean add(@NonNull Habit habit) {
        return this.habits.add(habit);
    }

    /**
     * Remove all Habits.
     */
    public void clear() {
        this.habits.clear();
    }

    /**
     * Determines whether or not the list contains the provided Habit.
     *
     * @param habit Habit to compare against.
     * @return Whether or not the list contains the Habit.
     */
    public boolean contains(@NonNull Habit habit) {
        try {
            Habit retrieved = this.habits.tailSet(habit).first();
            return habit.equals(retrieved);
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    /**
     * Retrieves a read-only wrapper for reading Habits.
     *
     * @return An unmodifiable Collection of Habits.
     */
    @NonNull
    public Collection<Habit> habits() {
        return Collections.unmodifiableCollection(this.habits);
    }

    /**
     * Remove the Habit matching the provided Habit.
     *
     * @param habit Habit to remove.
     * @return Whether or not the Habit was removed.
     */
    public boolean remove(@NonNull Habit habit) {
        return this.habits.remove(habit);
    }

    /**
     * Replace a target Habit with a replacement Habit.
     *
     * @param target      Habit to find to be replaced.
     * @param replacement Habit to replace with.
     * @return Whether or not the Habit was replaced.
     */
    public boolean replace(@NonNull Habit target, @NonNull Habit replacement) {
        if (!this.habits.remove(target)) {
            return false;
        }

        this.habits.add(replacement);
        return true;
    }

    /**
     * Indicates whether another HabitList is associated with the same Firestore collection as this one.
     *
     * @param habitList Other habit list.
     * @return Whether the HabitLists are equivalent.
     */
    public boolean equals(@NonNull HabitList habitList) {
        return this == habitList;
    }

    /**
     * A comparator class for comparing two Habits by title.
     */
    public static class HabitComparator implements Comparator<Habit> {
        /**
         * Compare two habits by title.
         *
         * @param h1 First habit to compare.
         * @param h2 Second habit to compare.
         * @return 0 if the two habits have the same title; non-zero otherwise.
         * @see String#compareTo(String)
         */
        @Override
        public int compare(@NonNull Habit h1, @NonNull Habit h2) {
            return h1.getTitle().compareTo(h2.getTitle());
        }
    }
}
