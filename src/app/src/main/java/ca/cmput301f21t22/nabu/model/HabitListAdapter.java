package ca.cmput301f21t22.nabu.model;

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

import ca.cmput301f21t22.nabu.databinding.FragmentHabitsBinding;
import ca.cmput301f21t22.nabu.databinding.HabitCardBinding;
import ca.cmput301f21t22.nabu.model.Habit;

public class HabitListAdapter extends ArrayAdapter {

    private ArrayList<Habit> habits;
    private Context context;
    @Nullable
    private HabitCardBinding binding;
    @Nullable
    ViewGroup container;

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

        if(view == null){
            this.binding = HabitCardBinding.inflate(LayoutInflater.from(context));
        }

        Habit habit = habits.get(position);

        TextView habitTitle = this.binding.habitTitleText;
        TextView datesOn = this.binding.datesOnText;
        TextView habitDescription = this.binding.habitDescriptionText;
        TextView dateStarted  = this.binding.dateStartedText;

        habitTitle.setText(habit.getTitle());
        datesOn.setText(habit.getOccurrence().toString());
        habitDescription.setText(habit.getReason());
        dateStarted.setText(habit.getStartDate().toString());

        final ImageButton habitsMenuButton = this.binding.habitsPopupImageButton;
        habitsMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu habitsPopupMenu = new PopupMenu(context, habitsMenuButton);
                MenuInflater inflater = habitsPopupMenu.getMenuInflater();
                inflater.inflate(R.menu.actions, habitsPopupMenu.getMenu());
                habitsPopupMenu.show();

                final PopupMenu menu = binding.habit_card_popup_menu;
                menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public void onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.view_events:
                                //TODO: Add a way to switch to view_events
                                ;
                            case R.id.edit_habit:
                                //TODO: Add a way to create a edit_habit fragment
                                ;
                            case R.id.delete_habit:
                                //TODO: Add a way to delete a habit;
                                ;
                        }
                    }
                });
            }
        });

    }

        return this.binding.getRoot();
    }
}
