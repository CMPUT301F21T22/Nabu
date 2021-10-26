package ca.cmput301f21t22.nabu.model;

public class HabitListView {
    private HabitList habitList;
    private HabitListAdapter habitListAdapter;

    public HabitListView (HabitList habitlist, HabitListAdapter habitListAdapter) {
        this.habitList = habitList;
        this.habitListAdapter = habitListAdapter;
    }

    public void add(Habit habit) {
        this.habitList.add(habit);
        this.habitListAdapter.add(habit);
    }

}
