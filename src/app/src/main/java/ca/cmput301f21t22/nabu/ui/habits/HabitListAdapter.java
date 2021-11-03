package ca.cmput301f21t22.nabu.ui.habits;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

import ca.cmput301f21t22.nabu.R;
import ca.cmput301f21t22.nabu.data.Habit;
import ca.cmput301f21t22.nabu.databinding.HabitCardBinding;

public class HabitListAdapter extends ArrayAdapter<Habit> {

    @Nullable
    ViewGroup container;
    private final ArrayList<Habit> habits;
    private final Context context;
    @Nullable
    private HabitCardBinding binding;

    public HabitListAdapter(Context context, ArrayList<Habit> habits) {
        super(context, 0, habits);

        this.habits = habits;
        this.context = context;
    }

    //Set up views for list objects
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        //return super.getView(position, convertView, parent);
        View view = convertView;

        if (view == null) {
            this.binding = HabitCardBinding.inflate(LayoutInflater.from(this.context));
        }

        Habit habit = this.habits.get(position);

        TextView habitTitle = this.binding.habitTitleText;
        TextView datesOn = this.binding.datesOnText;
        TextView habitDescription = this.binding.habitDescriptionText;
        TextView dateStarted = this.binding.dateStartedText;

        habitTitle.setText(habit.getTitle());
        datesOn.setText(habit.getOccurrence().toString());
        habitDescription.setText(habit.getReason());
        dateStarted.setText(habit.getStartDate().toString());

        final ImageButton habitsMenuButton = this.binding.habitsPopupImageButton;
        habitsMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu habitsPopupMenu = new PopupMenu(HabitListAdapter.this.context, habitsMenuButton);
                MenuInflater inflater = habitsPopupMenu.getMenuInflater();
                inflater.inflate(R.menu.habit_card_popup_menu, habitsPopupMenu.getMenu());
                habitsPopupMenu.show();
                final PopupMenu menu = habitsPopupMenu;
                menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getItemId() == habitsPopupMenu.getMenu().getItem(0).getItemId()) {
                            //TODO: Add call to switch to habit events fragment
                            return true;
                        } else if (item.getItemId() == habitsPopupMenu.getMenu().getItem(1).getItemId()) {
                            //Edit
                            //TODO: Add call for edit habit fragment
                            return true;
                        } else if (item.getItemId() == habitsPopupMenu.getMenu().getItem(2).getItemId()) {
                            //remove
                            HabitListAdapter.this.remove(HabitListAdapter.this.habits.get(position));
                            return true;
                        } else {
                            return false;
                        } //Default
                    }
                });
            }
        });

        return this.binding.getRoot();
    }

    @Override
    public void remove(@Nullable Habit habit) {
        super.remove(habit);
        //this.habits.remove(habit);
        this.notifyDataSetChanged();
    }

    public void edit(int position, @Nullable Habit habit) {
        this.remove(this.getItem(position));
        this.insert(habit, position);
        //this.habits.set(position, habit);
        this.notifyDataSetChanged();
    }
}
