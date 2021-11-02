package ca.cmput301f21t22.nabu.dialogs.date_picker;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;
import java.util.Date;

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
    @NonNull
    private Date initial;
    @NonNull
    private Callback callback;
    @NonNull
    private final Calendar calendar;

    public DatePickerFragment(@NonNull Date initial, @NonNull Callback callback) {
        this.initial = initial;
        this.callback = callback;
        this.calendar = Calendar.getInstance();
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        this.calendar.set(Calendar.YEAR, year);
        this.calendar.set(Calendar.MONTH, month);
        this.calendar.set(Calendar.DAY_OF_MONTH, day);
        this.callback.onDateSelected(this, this.calendar.getTime());
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        this.calendar.setTime(this.initial);
        return new DatePickerDialog(this.getActivity(), this, this.calendar.get(Calendar.YEAR),
                                    this.calendar.get(Calendar.MONTH), this.calendar.get(Calendar.DAY_OF_MONTH));
    }

    public interface Callback {
        void onDateSelected(DatePickerFragment dialog, Date date);
    }
}
