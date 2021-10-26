package ca.cmput301f21t22.nabu.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

import ca.cmput301f21t22.nabu.model.Habit;

public class HabitListAdapter extends ArrayAdapter {

    private ArrayList<Habit> habits;
    private Context context;

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
            view  = LayoutInflater.from(context).inflate(R.layout.content, parent,
                    false);
        }

        Habit habit = habits.get(position);

        TextView habitTitle = view.findViewById((R.id.habit_title_Text);
        TextView datesOn = view.findViewById(R.id.dates_on_Text);
        TextView habitDescription = view.findViewById(R.id.habit_description_Text);
        TextView dateStarted  = view.findViewById(R.id.date_started_Text);

        habitTitle.setText(habit.getTitle());
        datesOn.setText(habit.getOccurrence().getStringRep());
        habitDescription.setText(habit.getReason());
        dateStarted.setText(habit.getStartDate().toString());

        return view;
    }
}
